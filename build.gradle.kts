// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidLibraryKmp) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
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
