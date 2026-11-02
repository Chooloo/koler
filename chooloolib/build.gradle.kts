plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())
}

android {
    namespace = "com.chooloo.www.chooloolib"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }

    buildFeatures {
        compose = true
    }

    lint {
        warning += "MissingTranslation"
    }
}

dependencies {
    ksp(libs.hilt.compiler)

    api(platform(libs.compose.bom))
    api(libs.bundles.compose)
    api(libs.core.ktx)
    implementation(libs.hilt.android)
    api(libs.coroutines.android)
    implementation(libs.datastore.preferences)

    debugApi(libs.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
}
