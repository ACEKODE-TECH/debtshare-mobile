pluginManagement {
    includeBuild("build-logic")
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
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.develocity") version "4.5.0"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing.onlyIf { System.getenv("CI") != null }
        if (System.getenv("CI") != null) {
            tag("CI")
            tag(System.getenv("GITHUB_WORKFLOW"))
            System.getenv("GITHUB_REF_NAME")?.let { tag(it) }

            link("GitHub Actions Build", "https://github.com/${System.getenv("GITHUB_REPOSITORY")}/actions/runs/${System.getenv("GITHUB_RUN_ID")}")
        }
    }
}

rootProject.name = "Debtshare"
include(":app")
include(":iosApp")
include(":shared")
