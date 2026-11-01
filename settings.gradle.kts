pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

include(":koler")
include(":chooloolib")
include(":kontacts")
