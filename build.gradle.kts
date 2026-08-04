// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidLibraryKmp) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

tasks.register<Copy>("installGitHooks") {
    description = "Installs the git hooks from config/git-hooks to .git/hooks"
    group = "git hooks"

    from(layout.projectDirectory.dir("config/git-hooks"))
    into(layout.projectDirectory.dir(".git/hooks"))
}

subprojects {
    afterEvaluate {
        tasks.findByName("preBuild")?.dependsOn(":installGitHooks")
    }
}


subprojects {
    plugins.apply("io.gitlab.arturbosch.detekt")

    detekt {
        toolVersion = "1.23.8"
        config.setFrom(files("$rootDir/config/detekt.yml"))
        buildUponDefaultConfig = true
    }

    dependencies {
        add("detektPlugins", "io.nlopez.compose.rules:detekt:0.6.3")
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
            ),
        )
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}
