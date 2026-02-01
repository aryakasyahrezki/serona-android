// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    kotlin("jvm") version "1.9.10" apply false
//    kotlin("android") version "1.9.10" apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.kotlin.compose) apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.4" apply false

    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    alias(libs.plugins.google.devtools.ksp) apply false

}

subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion("1.9.24")
            }
        }
        // Tambahkan ini untuk memaksa metadata Kotlin agar tidak komplain
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24")
    }
}