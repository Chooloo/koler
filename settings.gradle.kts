pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
    }
    repositories {
        mavenCentral()
        jcenter()
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
        jcenter()
    }
}

include(":koler")
include(":chooloolib")
include(":kontacts")
