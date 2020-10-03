plugins {
    maven
    id("maven-publish")
    kotlin("multiplatform") version "1.4.10"
    id("com.android.library")
    id("kotlin-android-extensions")
    kotlin("plugin.serialization") version "1.4.10"
}

group = "com.fluentbuild"
version = "1.0-SNAPSHOT"
val serializationVersion = "1.0.0-RC"

repositories {
    gradlePluginPortal()
    google()
    maven(url = "https://kotlin.bintray.com/kotlinx/") // For kotlinx.datetime: soon will be just jcenter()
    jcenter()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    android {
        publishLibraryVariants("release", "debug")
    }

    /*iosX64("ios") {
        binaries {
            framework {
                baseName = "library"
            }
        }
    }

    mingwX64()

    macosX64()*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${serializationVersion}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${serializationVersion}")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
                api("io.ktor:ktor-io:1.4.1")
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
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("com.jakewharton.timber:timber-jdk:5.0.0-SNAPSHOT")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
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
                implementation("com.jakewharton.timber:timber-android:5.0.0-SNAPSHOT")
            }
        }

        val androidTest by getting

        /*val iosMain by getting {
            dependencies {
            }
        }

        val iosTest by getting

        val mingwX64Main by getting

        val mingwX64Test by getting

        val macosX64Main by getting

        val macosX64Test by getting*/
    }
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}