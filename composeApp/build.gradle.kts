import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        // Dependencias específicas de Android
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.runtime.compose) // Solo para Android
            implementation(libs.androidx.lifecycle.viewmodel) // Solo para Android
            implementation(libs.kotlinx.coroutines.android) // Solo para Android
        }

        // Dependencias comunes para todas las plataformas
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Ktor Dependencies (Multiplataforma)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentnegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)

            // Dependencias comunes, como coroutines core, que son multiplataforma
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.cio)

        }

        // Dependencias específicas para iOS (si las hay)
        iosMain.dependencies {
            // Aquí puedes agregar dependencias específicas de iOS si es necesario
        }
    }
}

android {
    namespace = "com.grupoct.gestionalmacen"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.grupoct.gestionalmacen"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    debugImplementation(compose.uiTooling)
}
