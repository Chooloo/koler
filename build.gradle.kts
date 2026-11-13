plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint) apply false
}

val ktlintPluginId = libs.plugins.ktlint.get().pluginId
val ktlintVersion = libs.versions.ktlint.cli.get()

subprojects {
    apply(plugin = ktlintPluginId)

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(ktlintVersion)
    }

    tasks.matching { it.name == "preBuild" }.configureEach {
        dependsOn("ktlintCheck")
    }
}
