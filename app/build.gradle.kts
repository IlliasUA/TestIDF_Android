plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
    id("com.google.gms.google-services") // Добавить эту строку
    id("kotlin-parcelize")
}

android {
    namespace = "legOS.testidf"
    compileSdk = 35 // Обновлено до 36 для соответствия требованиям зависимостей

    defaultConfig {
        applicationId = "legOS.testidf"
        minSdk = 24
        targetSdk = 35 // Обновлено до 36 для согласованности с compileSdk
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.20"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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
    // Compose BOM для согласованности версий
    implementation("androidx.compose.ui:ui-graphics:1.6.8")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.compose.ui:ui-text")
    implementation(libs.androidx.runtime) // Заменено libs.androidx.ui.text на явную зависимость

    // Добавлены зависимости для локальных юнит-тестов
    testImplementation("junit:junit:4.13.2")

    // Добавлены зависимости для инструментальных тестов
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Firebase BOM (Bill of Materials) - управляет версиями
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    // Cloud Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Firebase Cloud Messaging (для уведомлений)
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Coroutines для Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}