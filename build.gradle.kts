plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidLibraryKmp) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.stability) apply false
    id("jacoco")
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.register<Copy>("installGitHooks") {
    description = "Installs the git hooks from config/git-hooks to .git/hooks"
    group = "git hooks"

    from(layout.projectDirectory.dir("config/git-hooks"))
    into(layout.projectDirectory.dir(".git/hooks"))

    filePermissions {
        user {
            read = true
            write = true
            execute = true
        }
        group {
            read = true
            execute = true
        }
        other {
            read = true
            execute = true
        }
    }
}

allprojects {
    tasks.matching { it.name == "preBuild" }.configureEach {
        dependsOn(":installGitHooks")
    }
}

tasks.register("testAndroid") {
    group = "verification"
    description = "Runs all Android unit tests in androidApp and app modules"
    dependsOn(":androidApp:testDebugUnitTest")
    dependsOn(":app:testAndroidHostTest")
}

tasks.register("testIos") {
    group = "verification"
    description = "Runs all iOS tests in the app module (requires macOS)"
    dependsOn(":app:iosSimulatorArm64Test")
    dependsOn(":app:iosX64Test")
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
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacocoRootReport/jacocoRootReport.xml"))
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoRootReport/html"))
    }

    jacocoClasspath = configurations.getByName("jacocoAnt")

    subprojects.forEach { subproject ->
        subproject.plugins.withId("jacoco") {
            val jacocoTestReport = subproject.tasks.matching { it.name == "jacocoTestReport" }
            dependsOn(jacocoTestReport)

            jacocoTestReport.configureEach {
                val reportTask = this as JacocoReport
                this@register.sourceDirectories.from(reportTask.sourceDirectories)
                this@register.classDirectories.from(reportTask.classDirectories)
                this@register.executionData.from(reportTask.executionData.filter { it.exists() })
            }
        }
    }
}
