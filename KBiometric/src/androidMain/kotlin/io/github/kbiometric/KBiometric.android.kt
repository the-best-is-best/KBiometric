package io.github.kbiometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import io.github.kbiometric.AndroidBiometricPromptManager.activity
import io.github.kbiometric.AndroidBiometricPromptManager.authenticators

actual class KBiometric {

    actual fun auth(callback: (Result<BiometricResult>) -> Unit) {
        // Check biometric availability
        when (AndroidBiometricPromptManager.manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                callback(Result.success(BiometricResult.HardwareUnavailable))
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                callback(Result.success(BiometricResult.AuthenticationNotSet))
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                callback(Result.success(BiometricResult.FeatureUnavailable))
                return
            }

            BiometricManager.BIOMETRIC_SUCCESS -> {
                // This is expected if biometrics are available
                println("Biometrics are available and can be used.")
            }

            else -> {
                println("Unknown BiometricManager state.")
                return
            }
        }

        // Ensure `activity` is valid
        if (activity == null) {
            println("Error: Activity is null. Cannot start biometric authentication.")
            callback(Result.success(BiometricResult.FeatureUnavailable))
            return
        }

        // Create BiometricPrompt
        val prompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    println("Authentication error: $errString")
                    callback(Result.success(BiometricResult.AuthenticationError(errString.toString())))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    println("Authentication failed")
                    callback(Result.success(BiometricResult.AuthenticationFailed))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    println("Authentication succeeded")
                    callback(Result.success(BiometricResult.AuthenticationSuccess))
                }
            }
        )

        // Create PromptInfo
        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication Required")
            .setSubtitle("Please authenticate using your biometrics or device credentials")
            .setDescription("This app requires authentication for access")
            .setAllowedAuthenticators(authenticators)

        // Only set the negative button if DEVICE_CREDENTIAL is not used
        if ((authenticators and BiometricManager.Authenticators.DEVICE_CREDENTIAL) == 0) {
            promptInfoBuilder.setNegativeButtonText("Cancel")
        }

        // Build the PromptInfo
        val promptInfo = promptInfoBuilder.build()

        // Start authentication
        println("Starting biometric authentication...")
        prompt.authenticate(promptInfo)
    }
}
