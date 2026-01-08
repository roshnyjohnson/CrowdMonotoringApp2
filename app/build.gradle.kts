plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.crowdmonitoringapp2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.crowdmonitoringapp2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    // CameraX
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")

<<<<<<< Updated upstream
    // ML Kit
    implementation("com.google.mlkit:object-detection:17.0.1")
=======
    // ML Kit Face Detection (Much better for counting people)
    implementation("com.google.mlkit:face-detection:16.1.6")
>>>>>>> Stashed changes

    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
}
