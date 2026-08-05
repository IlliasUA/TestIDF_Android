import org.gradle.api.tasks.Delete

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.4")
    }
}

plugins {
    id("com.android.application") version "8.12.3" apply false
    id("com.android.library") version "8.12.3" apply false
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
