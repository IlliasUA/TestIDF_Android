import org.gradle.api.tasks.Delete

plugins {
    id("com.android.application") version "8.12.2" apply false
    id("com.android.library") version "8.12.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}