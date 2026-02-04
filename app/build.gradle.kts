plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    // Add the Google services Gradle plugin
//    id("com.google.gms.google-services")
    alias(libs.plugins.google.devtools.ksp)
    kotlin("kapt")  // <-- kalau pakai Kotlin
    id ("com.google.dagger.hilt.android") // <-- ini wajib
}

android {
    namespace = "com.serona.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.serona.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // isMinifyEnabled tetap false untuk debugging, ubah ke true saat sudah rilis final
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Pastikan versi Kotlin di proyekmu adalah 1.9.24 agar cocok dengan 1.5.14 ini
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}


dependencies {
    // Firebase BOM - mengelola semua versi Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-auth")

    // Core & Runtime - VERSI YANG KONSISTEN DENGAN compileSdk 34
    implementation("androidx.core:core-ktx:1.13.1")  // Ganti dari 1.12.0 dan 1.16.0
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")  // Update dari 2.8.0
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose & UI
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")

    // Lifecycle Compose - konsisten dengan lifecycle-runtime-ktx
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")  // Update dari 2.8.0
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

    // UI Tools & Splash
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Navigation & Hilt
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // AppCompat & Material
    implementation("androidx.appcompat:appcompat:1.7.0")  // Update dari 1.6.1
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // RecyclerView dan CardView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")  // Update dari 1.0.0

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    // Face Detection
    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // ML Kit & Tools
    implementation(libs.mlkit.face.detection)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)

    // ROOM DATABASE
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
}

