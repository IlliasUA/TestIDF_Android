plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
    id("com.google.gms.google-services") // Google Services для Firebase
    id("kotlin-parcelize")
}

android {
    namespace = "legOS.testidf"
    compileSdk = 35

    defaultConfig {
        applicationId = "legOS.testidf"
        minSdk = 24
        targetSdk = 35
        versionCode = 7  // Увеличена версия для нового релиза с исправлениями
        versionName = "1.2.1"  // Обновлена версия
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
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-text")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.material:material-icons-extended")

    // ОБНОВЛЕНО: Activity Compose с полной поддержкой edge-to-edge для Android 15
    implementation("androidx.activity:activity-compose:1.9.3")

    // ДОБАВЛЕНО: Core для поддержки WindowCompat и системных отступов
    implementation("androidx.core:core-ktx:1.15.0")

    // ===== ИСПРАВЛЕНИЕ GOOGLE PLAY WARNINGS =====

    // ИСПРАВЛЕНИЕ 1: Явное указание актуальной версии androidx.fragment
    // Устраняет предупреждение: "В вашем приложении используется устаревшая версия SDK androidx.fragment:fragment"
    // Обновлено с 1.0.0 до 1.8.5 (стабильная версия на декабрь 2024)
    implementation("androidx.fragment:fragment-ktx:1.8.5")

    // ИСПРАВЛЕНИЕ 2: Явное указание актуальной версии reCAPTCHA Enterprise
    // Устраняет КРИТИЧЕСКУЮ уязвимость безопасности
    // Google требует версию 18.4.0+, используем последнюю стабильную 18.6.1
    implementation("com.google.android.recaptcha:recaptcha:18.6.1")

    // ============================================

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Coil для загрузки изображений
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Firebase BOM (Bill of Materials) - управляет версиями
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Google Play Billing для подписок
    implementation("com.android.billingclient:billing-ktx:7.1.1")

    // Тесты
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.12.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}