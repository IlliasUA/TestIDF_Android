import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Load local.properties file
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { stream ->
        localProperties.load(stream)
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
    id("com.google.gms.google-services") // Google Services для Firebase
    id("kotlin-parcelize")
}

android {
    namespace = "legOS.testidf"
    compileSdk = 36

    defaultConfig {
        applicationId = "legOS.testidf"
        minSdk = 24
        targetSdk = 36
        versionCode = 43  // Увеличена версия для исправления Android 15 edge-to-edge
        versionName = "1.3.0"  // Обновлена версия для AI Assistant
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ========================================
        // КРИТИЧНО: Указываем все поддерживаемые языки в правильном порядке
        // Первый язык = язык по умолчанию
        // ========================================
        // Add Gemini API Key to BuildConfig
        buildConfigField("String", "GEMINI_API_KEY", "\"${localProperties.getProperty("GEMINI_API_KEY", "")}\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true  // ВАЖНО: Включаем BuildConfig для доступа к константам
    }

    androidResources {
        localeFilters += listOf("fr", "en", "es", "pt", "cn")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isShrinkResources = false
        }
    }

    // ========================================
    // ИСПРАВЛЕНО: Улучшенные настройки для Android App Bundle
    // ========================================
    bundle {
        language {
            // КРИТИЧНО: Отключаем разделение по языкам
            // Все языки должны быть включены в каждый APK
            enableSplit = false
        }

        density {
            // Отключаем разделение по плотности экрана для стабильности
            enableSplit = false
        }

        abi {
            // Отключаем разделение по архитектуре
            enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // Compose BOM для согласованности версий
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size.class1)
    implementation(libs.androidx.compose.material.icons.extended)

    // ОБНОВЛЕНО: Activity Compose с полной поддержкой edge-to-edge для Android 15
    implementation(libs.androidx.activity.compose)

    // ДОБАВЛЕНО: Core для поддержки WindowCompat и системных отступов
    implementation(libs.androidx.core.ktx)

    // ===== ИСПРАВЛЕНИЕ GOOGLE PLAY WARNINGS =====

    // ИСПРАВЛЕНИЕ 1: Явное указание актуальной версии androidx.fragment
    // Устраняет предупреждение: "В вашем приложении используется устаревшая версия SDK androidx.fragment:fragment"
    // Обновлено с 1.0.0 до 1.8.5 (стабильная версия на декабрь 2024)
    implementation(libs.androidx.fragment.ktx)

    // ИСПРАВЛЕНИЕ 2: Явное указание актуальной версии reCAPTCHA Enterprise
    // Устраняет КРИТИЧЕСКУЮ уязвимость безопасности
    // Google требует версию 18.4.0+, используем последнюю стабильную 18.6.1
    implementation(libs.recaptcha)

    // ============================================

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Coil для загрузки изображений
    implementation(libs.coil.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // Firebase BOM (Bill of Materials) - управляет версиями
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)

    // Google Play Billing для подписок
    implementation(libs.billing.ktx)

    // ===== НОВОЕ: Google Gemini AI для военного ассистента =====
    implementation(libs.generativeai)

    // Тесты
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug tools
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
