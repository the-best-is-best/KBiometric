package io.github.kbiometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import io.github.kbiometric.AndroidBiometricPromptManager.activity
import io.github.kbiometric.AndroidBiometricPromptManager.authenticators
import io.github.kbiometric.AndroidBiometricPromptManager.promptInfo

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


        // Build the PromptInfo

        // Start authentication
        println("Starting biometric authentication...")
        prompt.authenticate(promptInfo)
    }
}
