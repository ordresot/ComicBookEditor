import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            implementation(compose.materialIconsExtended)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("org.apache.commons:commons-compress:1.28.0")
        }
    }
}

android {
    namespace = "com.ordresot.cbeditor"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = "com.ordresot.cbeditor"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.ordresot.cbeditor.MainKt"

        nativeDistributions {
            // Настройки для Windows
            targetFormats(
                TargetFormat.Msi,
                TargetFormat.Exe
            )
            packageName = "ComicBookEditor"
            packageVersion = "1.0.0"
            description = "Comic editor for CBR/CBZ"
            vendor = "Konstantin Derishev"
            copyright = "© 2025 Konstantin Derishev. All rights reserved."

            // Настройки для Windows
            windows {
                menuGroup = "Comic Book Editor"
                // Укажите путь к иконке (создайте папку icons в src/desktopMain/resources)
                iconFile.set(project.file("src/desktopMain/resources/icons/icon.ico"))
                perUserInstall = true
            }

            // Настройки macOS
            macOS {
                bundleID = "com.yourname.comiceditor"
                iconFile.set(project.file("src/desktopMain/resources/icons/icon.icns"))
            }

            // Настройки Linux
            linux {
                iconFile.set(project.file("src/desktopMain/resources/icons/icon.png"))
            }
        }
    }
}
