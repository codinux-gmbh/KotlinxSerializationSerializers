pluginManagement {
    val kotlinVersion: String by settings


    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


rootProject.name = "kotlinx-serialization-serializers"
