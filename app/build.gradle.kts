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

    // MPAndroidChart für Diagramme
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(libs.fragment.testing)
    implementation(libs.androidx.junit)

    // Lokale Unit Tests (laufen auf der JVM)
    testImplementation(libs.junit)
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core:1.5.0") // Aktualisierte Test Core Version für bessere Kompatibilität
    testImplementation("androidx.fragment:fragment-testing:1.6.2") // Neueste Version für FragmentScenario

    // Instrumented Tests (laufen auf Android-Gerät/Emulator)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.fragment:fragment-testing:1.6.2") // Für FragmentScenario
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("org.mockito:mockito-android:5.3.1") // Für Mocking (falls benötigt)
}
