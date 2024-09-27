package io.github.kbiometric

sealed interface BiometricResult {
    data object HardwareUnavailable : BiometricResult
    data object FeatureUnavailable : BiometricResult
    data class AuthenticationError(val error: String) : BiometricResult
    data object AuthenticationFailed : BiometricResult
    data object AuthenticationSuccess : BiometricResult
    data object AuthenticationNotSet : BiometricResult
}

expect class KBiometric() {
    fun auth(callback: (Result<BiometricResult>) -> Unit)
}