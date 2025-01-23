@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


repositories {
    mavenCentral()
}


group = "net.codinux.kotlin.serialization"
version = "1.0.0-SNAPSHOT"


kotlin {
    jvmToolchain(11)

    jvm()


    js {
        moduleName = "kotlinx-serialization-serializers"
        binaries.executable()

        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }

        nodejs {
            testTask {
                useMocha {
                    timeout = "20s" // Mocha times out after 2 s, which is too short for some tests
                }
            }
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }
    }


    linuxX64()
    mingwX64()

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchosArm64()
    watchosSimulatorArm64()
    tvosArm64()
    tvosSimulatorArm64()

    applyDefaultHierarchyTemplate()


    val kotlinxSerializationVersion: String by project

    val assertKVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

            implementation("com.willowtreeapps.assertk:assertk:$assertKVersion")
        }

        jvmTest.dependencies {
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
            implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

            implementation("ch.qos.logback:logback-classic:$logbackVersion")
        }
    }
}