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

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Robolectric für lokale Android-Tests in der JVM
    testImplementation("org.robolectric:robolectric:4.10.3")

    // AndroidX Test Core (macht u.a. ApplicationProvider verfügbar)
    testImplementation("androidx.test:core:1.4.0")

    // FragmentScenario (zum Testen von Fragments in local unit tests)
    testImplementation("androidx.fragment:fragment-testing:1.5.6")

    // JUnit4-Erweiterung für AndroidX Tests
    testImplementation("androidx.test.ext:junit:1.1.5")
}
