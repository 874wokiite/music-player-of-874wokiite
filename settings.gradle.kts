pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // FAIL_ON_PROJECT_REPOSに変更
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "music-player-of-874wokiite"
include(":app")
include(":infrastructure")
