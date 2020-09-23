plugins {
    kotlin("multiplatform") version "1.4.0"
    id("com.android.library")
    id("kotlin-android-extensions")
    kotlin("plugin.serialization") version "1.4.0"
}
group = "com.fluentbuild"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/") // soon will be just jcenter()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
}
kotlin {
    jvm()
    android()
    iosX64("ios") {
        binaries {
            framework {
                baseName = "library"
            }
        }
    }
    mingwX64()
    macosX64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-native:0.20.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("io.mockk:mockk-common:1.9.3")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("com.jakewharton.timber:timber-jdk:5.0.0-SNAPSHOT")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit")
                implementation("io.mockk:mockk:1.9.3")
            }
        }

        val androidMain by getting {
            dependencies {
                dependsOn(jvmMain)
                implementation("androidx.core:core-ktx:1.3.1")
                implementation("com.jakewharton.timber:timber-jdk:5.0.0-SNAPSHOT")
            }
        }
        val androidTest by getting
        val iosMain by getting {
            dependencies {
            }
        }
        val iosTest by getting
        val mingwX64Main by getting
        val mingwX64Test by getting
        val macosX64Main by getting
        val macosX64Test by getting
    }
}
android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}