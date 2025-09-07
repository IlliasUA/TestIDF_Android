plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
}

android {
    namespace = "legOS.testidf"
    compileSdk = 35

    defaultConfig {
        applicationId = "legOS.testidf"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Для инструментальных тестов
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.20" // Обновлено до версии, совместимой с плагином Kotlin
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
    // Основные зависимости
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-graphics:1.8.3")
    implementation("androidx.appcompat:appcompat:1.7.0") // Заменил libs.androidx.appcompat, проверьте необходимость
    implementation("androidx.compose.material3:material3:1.3.0") // Заменил libs.material3, уже есть в BOM
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")
    implementation("androidx.navigation:navigation-compose:2.9.3")

    // Зависимости для тестирования
    testImplementation("junit:junit:4.13.2") // Для юнит-тестов
    androidTestImplementation("androidx.test.ext:junit:1.2.1") // Для инструментальных тестов
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1") // Для UI-тестов
    androidTestImplementation("androidx.test:runner:1.6.2") // Раннер для тестов
    androidTestImplementation("androidx.test:core:1.6.1") // Ядро тестов
}