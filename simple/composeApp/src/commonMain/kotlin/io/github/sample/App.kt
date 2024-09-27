package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kbiometric.BiometricResult
import io.github.kbiometric.KBiometric
import io.github.sample.theme.AppTheme
import kotlinx.coroutines.launch


@Composable
internal fun App() = AppTheme {
    val scope = rememberCoroutineScope()
    var label by remember { mutableStateOf("Please auth first") }
    val kBiometric = KBiometric()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            scope.launch {
                kBiometric.auth { result ->
                    result.onSuccess {
                        label = when (it) {
                            is BiometricResult.AuthenticationError -> it.error
                            BiometricResult.AuthenticationFailed -> "Auth failed"
                            BiometricResult.AuthenticationNotSet -> "Auth not set"
                            BiometricResult.AuthenticationSuccess -> "Auth success"
                            BiometricResult.FeatureUnavailable -> "Feature unavailable"
                            BiometricResult.HardwareUnavailable -> "Hardware unavailable"
                        }
                    }.onFailure {
                        label = "Failure"
                    }
                }

            }
        }) {
            Text("Auth Bio")
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(label)
    }
}
