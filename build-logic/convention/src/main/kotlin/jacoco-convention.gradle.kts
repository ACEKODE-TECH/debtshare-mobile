import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    id("jacoco")
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val fileFilter = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    "**/*$[0-9]*.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/*\$Lambda\$*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*",
    "**/*Composable*.*",
    "**/*Compose*.*",
    "**/*Activity*.*",
    "**/*Fragment*.*",
    "**/*View*.*",
    "**/*Wrapper*.*",
    "**/*Screen*.*",
    "**/*Preview*.*",
    "**/*Navigation*.*",
    "**/*NavHost*.*",
    "**/*Route*.*"
)

val debugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
    exclude(fileFilter)
}

val mainSrc = "$projectDir/src/main/java"
val commonSrc = "$projectDir/src/commonMain/kotlin"
val androidSrc = "$projectDir/src/androidMain/kotlin"

tasks.register<JacocoReport>("jacocoTestReport") {
    group = "Reporting"
    description = "Generate Jacoco coverage reports"

    val testTaskNames = listOf("testDebugUnitTest", "testAndroidHostTest")
    val availableTestTasks = testTaskNames.mapNotNull { tasks.findByName(it) }
    dependsOn(availableTestTasks)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    sourceDirectories.setFrom(files(mainSrc, commonSrc, androidSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "jacoco/testDebugUnitTest.exec",
                "jacoco/testAndroidHostTest.exec",
                "outputs/unit_test_code_coverage/androidHostTest/testAndroidHostTest.exec"
            )
        }
    )
}
