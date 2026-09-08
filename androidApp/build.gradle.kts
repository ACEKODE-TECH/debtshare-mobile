import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.stability)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.wickkit)
    id("jacoco-convention")
    id("detekt-convention")
    id("spotless-convention")
}

android {
    namespace = "acekode.debtshare"
    compileSdk = 37

    defaultConfig {
        applicationId = "acekode.debtshare"
        minSdk = 24
        targetSdk = 37
        versionCode = rootProject.extra["appVersionCode"] as Int
        versionName = rootProject.extra["appVersionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    }

    signingConfigs {
        getByName("debug") {
            if (keystorePropertiesFile.exists()) {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAliasDebug")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAliasRelease")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isDebuggable = true

            val buildDate = project.findProperty("buildDate") as String?
            val buildNumberToday = project.findProperty("buildNumberToday") as String?
            versionNameSuffix =
                if (!buildDate.isNullOrBlank() && !buildNumberToday.isNullOrBlank()) {
                    "-Debug+$buildDate.$buildNumberToday"
                } else {
                    "-Debug"
                }
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            optimization {
                enable = true
            }

            val buildDate = project.findProperty("buildDate") as String?
            val buildNumberToday = project.findProperty("buildNumberToday") as String?
            versionNameSuffix =
                if (!buildDate.isNullOrBlank() && !buildNumberToday.isNullOrBlank()) {
                    "-Release+$buildDate.$buildNumberToday"
                } else {
                    "-Release"
                }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":app"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.wickkit.core)
    debugImplementation(libs.wickkit.network)
    debugImplementation(libs.wickkit.flags)
    debugImplementation(libs.wickkit.compose)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    releaseImplementation(libs.wickkit.no.op)
    releaseImplementation(libs.wickkit.compose.no.op)
}
