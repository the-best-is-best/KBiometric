package io.github.kbiometric

import kotlinx.cinterop.ExperimentalForeignApi
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAError
import platform.LocalAuthentication.LAErrorBiometryLockout
import platform.LocalAuthentication.LAErrorBiometryNotAvailable
import platform.LocalAuthentication.LAErrorBiometryNotEnrolled
import platform.LocalAuthentication.LAErrorSystemCancel
import platform.LocalAuthentication.LAErrorUserCancel
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics

actual class KBiometric {
    @OptIn(ExperimentalForeignApi::class)
    actual fun auth(callback: (Result<BiometricResult>) -> Unit) {
        val context = LAContext()

        // Check if biometric authentication is available
        if (!context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)) {
            callback(Result.success(BiometricResult.HardwareUnavailable))
            return
        }

        // Perform authentication
        context.evaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            "Authenticate"
        ) { success, error ->
            if (success) {
                callback(Result.success(BiometricResult.AuthenticationSuccess))
            } else {
                // Handle errors
                if (error != null) {
                    val result = when ((error as? LAError)) {
                        LAErrorBiometryNotAvailable -> BiometricResult.FeatureUnavailable
                        LAErrorBiometryNotEnrolled -> BiometricResult.AuthenticationNotSet
                        LAErrorBiometryLockout -> BiometricResult.AuthenticationFailed
                        LAErrorUserCancel -> BiometricResult.AuthenticationFailed // User canceled
                        LAErrorSystemCancel -> BiometricResult.AuthenticationFailed // System canceled
                        else -> BiometricResult.AuthenticationError(error.localizedDescription)
                    }
                    callback(Result.success(result))
                } else {
                    callback(Result.success(BiometricResult.AuthenticationError("Unknown error occurred")))
                }
            }
        }
    }
}
