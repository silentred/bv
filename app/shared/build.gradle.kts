plugins {
    alias(gradleLibs.plugins.android.library)
    alias(gradleLibs.plugins.compose.compiler)
    alias(gradleLibs.plugins.firebase.crashlytics)
    alias(gradleLibs.plugins.google.ksp)
    alias(gradleLibs.plugins.google.services) apply false
    alias(gradleLibs.plugins.kotlin.android)
    alias(gradleLibs.plugins.kotlin.serialization)
}

android {
    namespace = AppConfiguration.appId
    compileSdk = AppConfiguration.compileSdk

    defaultConfig {
        minSdk = AppConfiguration.minSdk
        vectorDrawables {
            useSupportLibrary = true
        }

        buildTypes {
            buildConfigField(
                type = "int",
                name = "VERSION_CODE",
                value = "${AppConfiguration.versionCode}"
            )
            buildConfigField(
                type = "String",
                name = "VERSION_NAME",
                value = "\"${AppConfiguration.versionName}\""
            )
            buildConfigField(
                type = "String",
                name = "APPLICATION_ID",
                value = "\"${AppConfiguration.appId}\""
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("r8Test") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("alpha") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    lint{
        targetSdk = AppConfiguration.targetSdk
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    annotationProcessor(androidx.room.compiler)
    ksp(androidx.room.compiler)
    api(platform("${libs.firebase.bom.get()}"))
    api(androidx.activity.compose)
    api(androidx.core.ktx)
    api(androidx.core.splashscreen)
    api(androidx.compose.constraintlayout)
    api(androidx.compose.ui)
    api(androidx.compose.ui.util)
    api(androidx.compose.ui.tooling.preview)
    api(androidx.compose.material.icons)
    api(androidx.compose.material)
    api(androidx.compose.material3)
    api(androidx.compose.material3.adaptive)
    api(androidx.compose.material3.adaptive.layout)
    api(androidx.compose.material3.adaptive.navigation)
    api(androidx.compose.material3.adaptive.navigation.suit)
    api(androidx.compose.material3.window.size)
    api(androidx.compose.tv.foundation)
    api(androidx.compose.tv.material)
    api(androidx.datastore.typed)
    api(androidx.datastore.preferences)
    api(androidx.lifecycle.runtime.ktx)
    api(androidx.media3.common)
    api(androidx.media3.decoder)
    api(androidx.media3.exoplayer)
    api(androidx.media3.ui)
    api(androidx.navigation.compose)
    api(androidx.room.ktx)
    api(androidx.room.runtime)
    api(androidx.webkit)
    api(libs.accompanist.systemuicontroller)
    api(libs.akdanmaku)
    api(libs.coil.compose)
    api(libs.coil.gif)
    api(libs.coil.svg)
    api(libs.firebase.analytics.ktx)
    api(libs.firebase.crashlytics.ktx)
    api(libs.geetest.sensebot)
    api(libs.koin.android)
    api(libs.koin.compose)
    api(libs.koin.compose.navigation)
    api(libs.kotlinx.serialization)
    api(libs.ktor.client.cio)
    api(libs.koin.core)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.client.core)
    api(libs.ktor.client.encoding)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.client.serialization.kotlinx)
    api(libs.ktor.server.cio)
    api(libs.ktor.server.core)
    api(libs.logging)
    api(libs.lottie)
    api(libs.material)
    api(libs.qrcode)
    api(libs.rememberPreference)
    api(libs.slf4j.android.mvysny)
    api(project(mapOf("path" to ":bili-api")))
    api(project(mapOf("path" to ":bili-subtitle")))
    api(project(mapOf("path" to ":player")))
    api(project(mapOf("path" to ":utils")))
    testImplementation(androidx.room.testing)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(androidx.compose.ui.test.junit4)
    debugApi(androidx.compose.ui.test.manifest)
    debugApi(androidx.compose.ui.tooling)
}