plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fitnesstracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fitnesstracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    // MPAndroidChart für Diagramme
    implementation(libs.mpandroidchart)
    //implementation(libs.fragment.testing)
    //implementation(libs.androidx.junit)
    // Lokale Unit Tests (laufen auf der JVM)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    testImplementation(libs.core) // Aktualisierte Test Core Version für bessere Kompatibilität
    testImplementation(libs.fragment.testing) // Neueste Version für FragmentScenario
    testImplementation(libs.core.testing)
    testImplementation(libs.mockito.core)


    // Instrumented Tests (laufen auf Android-Gerät/Emulator)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.jackson.databind)
    implementation (libs.jackson.databind)
    implementation (libs.jackson.annotations)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.rules)
    androidTestImplementation(libs.junit.v121)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.espresso.core.v361)
    androidTestImplementation(libs.fragment.testing.v162) // Für FragmentScenario
    androidTestImplementation(libs.core.v150)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.core.testing) // Für Mocking (falls benötigt)
}
