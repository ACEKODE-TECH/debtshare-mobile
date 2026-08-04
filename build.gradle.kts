import org.jetbrains.kotlin.gradle.internal.builtins.StandardNames.FqNames.target

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
    jacoco
}

val jacocoVersion = libs.versions.jacoco.get()

subprojects {
    apply(plugin = "jacoco")
    
    extensions.configure<JacocoPluginExtension> {
        toolVersion = jacocoVersion
    }
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


tasks.register("testAndroid") {
    group = "verification"
    description = "Runs all Android unit tests in app and shared modules"
    dependsOn(":app:testDebugUnitTest")
    dependsOn(":shared:testAndroidHostTest")
}

tasks.register("testIos") {
    group = "verification"
    description = "Runs all iOS tests in the shared module (requires macOS)"
    dependsOn(":shared:iosSimulatorArm64Test")
    dependsOn(":shared:iosX64Test")
}

tasks.register("testAll") {
    group = "verification"
    description = "Runs all tests in all modules (Android and iOS)"
    dependsOn("testAndroid")
    dependsOn("testIos")
}

tasks.register<JacocoReport>("jacocoRootReport") {
    group = "Reporting"
    description = "Combined Jacoco coverage report for all modules"

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val jacocoTasks = subprojects.mapNotNull { it.tasks.findByName("jacocoTestReport") as? JacocoReport }
    
    dependsOn(jacocoTasks)

    sourceDirectories.setFrom(files(jacocoTasks.map { it.sourceDirectories }))
    classDirectories.setFrom(files(jacocoTasks.map { it.classDirectories }))
    executionData.setFrom(files(jacocoTasks.map { it.executionData }))
}
