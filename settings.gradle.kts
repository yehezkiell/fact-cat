pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "edison_android_exercise"

include(":app")
include(":features:fact")
include(":core:design-system")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":core:datastore")
include(":core:util")
include(":core:subviewmodel")
include(":core:testing")
