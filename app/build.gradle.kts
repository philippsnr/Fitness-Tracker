plugins {
    alias(libs.plugins.android.application)
    id("checkstyle") // Checkstyle-Plugin aktivieren
}

android {
    namespace = "com.example.fitnesstracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fitnesstracker"
        minSdk = 24
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
}

// ✅ Checkstyle-Tasks für Main- und Test-Code erstellen
tasks.register<Checkstyle>("checkstyleMain") {
    group = "verification"
    description = "Checkstyle-Analyse für den Hauptquellcode"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")

    source = fileTree("src/main/java") {
        include("**/*.java") // Nur Java-Dateien prüfen
    }

    classpath = files()

    reports {
        xml.required.set(false)
        html.required.set(true) // HTML-Report für bessere Lesbarkeit
    }
}

tasks.register<Checkstyle>("checkstyleTest") {
    group = "verification"
    description = "Checkstyle-Analyse für Testcode"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")

    source = fileTree("src/test/java") {
        include("**/*.java")
    }

    classpath = files()

    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

// ✅ Checkstyle automatisch mit "check" ausführen
tasks.named("check") {
    dependsOn("checkstyleMain", "checkstyleTest")
}
