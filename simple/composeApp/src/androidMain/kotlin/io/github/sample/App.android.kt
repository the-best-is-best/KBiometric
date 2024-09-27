package io.github.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.kbiometric.AndroidBiometricPromptManager

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AndroidBiometricPromptManager.init(this, "Demo App Auth", "Demo Biometric")
        setContent { App() }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
