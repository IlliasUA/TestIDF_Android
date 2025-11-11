# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ============================================
# КРИТИЧНО: BuildConfig для API ключа Gemini
# ============================================
-keep class legOS.testidf.BuildConfig { *; }
-keepclassmembers class legOS.testidf.BuildConfig {
    public static <fields>;
}

# ============================================
# AI Assistant / Gemini API
# ============================================
-keep class legOS.testidf.utils.GeminiApiClient { *; }
-keep class legOS.testidf.utils.ChatMessage { *; }
-keep class legOS.testidf.utils.NetworkUnavailableException { *; }
-keep class legOS.testidf.viewmodel.AIAssistantViewModel { *; }

# ============================================
# JSON parsing (org.json)
# ============================================
-keepattributes Signature
-keepattributes *Annotation*
-keep class org.json.** { *; }
-dontwarn org.json.**

# ============================================
# Kotlin metadata
# ============================================
-keep class kotlin.Metadata { *; }

# ============================================
# Firebase (если используется)
# ============================================
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }