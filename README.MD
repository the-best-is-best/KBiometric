<h1 align="center">KBiometric</h1><br>
<div align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href="https://android-arsenal.com/api?level=21" rel="nofollow">
    <img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" style="max-width: 100%;">
</a>
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
  <img src="https://img.shields.io/badge/Platform-iOS%20%2F%20macOS-lightgrey.svg?logo=apple" alt="Badge iOS" />

<a href="https://github.com/the-best-is-best/"><img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/></a>
</div>

### This package for Android and iOS devices to allow local authentication via fingerprint or touch ID

<hr>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kbiometric)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kbiometric)

KApp Auth is available on `mavenCentral()`.

```kotlin
implementation("io.github.the-best-is-best:kbiometric:1.0.0")

 // u need add
```

### Install

```toml
[versions]
appcompat = "1.7.0"
....
[libraries]
....
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }

```

```kotlin
    androidMain.dependencies {
    ....
    implementation(libs.androidx.appcompat)
}

```

## Need add this in pod file if not exist run ` pod init `

```swift
pod 'BiometricAuthentication' , '3.1.3'
```

## How to use it

### First in androidMain

<br>

```kotlin
class AppActivity : AppCompatActivity() { // change this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AndroidBiometricPromptManager.init(this, "Demo App Auth", "Demo Biometric") // add this
        setContent { App() }
    }
}
```

### in commonMain

```kotlin
val scope = rememberCoroutineScope()
var label by remember { mutableStateOf("Please auth first") }

val kBiometric = KBiometric()

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

```