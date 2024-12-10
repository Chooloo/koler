plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.android.application)
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
    namespace = "com.chooloo.www.koler"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        versionCode = 81
        versionName = "v1.6.0"
        applicationId = "com.chooloo.www.koler"
        targetSdk = libs.versions.sdk.get().toInt()
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

    lint {
        checkReleaseBuilds = false
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    kapt(libs.hilt.compiler)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.room.testing)

    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.test.ui.junit4)
    androidTestImplementation(libs.test.espresso.core)

    annotationProcessor(libs.dagger.hilt.compiler)

    implementation(project(":chooloolib"))
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.ktx)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coroutines)
    implementation(libs.rxjava)
    implementation(libs.picasso)
    implementation(libs.material)
    implementation(libs.rxandroid)
    implementation(libs.app.compat)
    implementation(libs.preference)
    implementation(libs.accompanist)
    implementation(libs.storio.content.resolver)
    implementation(libs.accompanist.pager.indicators)
}
