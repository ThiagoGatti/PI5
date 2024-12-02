pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/dev")
        }
        maven {
            url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/public")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/dev")
        }
        maven {
            url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/public")
        }
    }
}

rootProject.name = "EducaAnalytics"
include(":app")