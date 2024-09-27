package io.github.kbiometric

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt.PromptInfo

object AndroidBiometricPromptManager {

    internal lateinit var activity: AppCompatActivity
    internal lateinit var manager: BiometricManager

    // Store the prompt info in a mutable variable
    internal lateinit var promptInfo: PromptInfo

    // Determine the authenticators based on the Android version
    internal val authenticators = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    } else {
        BIOMETRIC_STRONG
    }

    fun init(activity: AppCompatActivity, title: String, description: String) {
        this.activity = activity
        showBiometricPrompt(title, description)
    }

    private fun showBiometricPrompt(title: String, description: String) {
        manager = BiometricManager.from(activity)

        val promptInfoBuilder = PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
            .setConfirmationRequired(false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            promptInfoBuilder.setNegativeButtonText("Cancel")
        }

        promptInfo = promptInfoBuilder.build()
    }
}
