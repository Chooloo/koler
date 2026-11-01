plugins {
    alias(libs.plugins.hilt)
    alias(libs.plugins.android.library)
    kotlin("android")
    kotlin("kapt")
}

kotlin {
    jvmToolchain(19)
}

hilt {
    enableAggregatingTask = true
}

android {
    namespace = "com.chooloo.www.chooloolib"
    compileSdk = libs.versions.sdk.get().toInt()

    lint {
        targetSdk = libs.versions.sdk.get().toInt()
    }

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    kapt(libs.hilt.compiler)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.test.ui.junit4)
    androidTestImplementation(libs.test.espresso.core)

    implementation(libs.androidx.material3.android)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.ktx)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coroutines)
}
