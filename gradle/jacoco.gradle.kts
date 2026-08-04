tasks.withType<Test> {
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
    "**/Manifest*.*",
    "**/*\$Lambda\$*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*",
    // Compose
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

val debugTree = fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
    exclude(fileFilter)
}
val mainSrc = "${project.projectDir}/src/main/java"
val commonSrc = "${project.projectDir}/src/commonMain/kotlin"
val androidSrc = "${project.projectDir}/src/androidMain/kotlin"

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
    executionData.setFrom(fileTree(project.layout.buildDirectory.get()) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "jacoco/testDebugUnitTest.exec",
            "jacoco/testAndroidHostTest.exec",
            "outputs/unit_test_code_coverage/androidHostTest/testAndroidHostTest.exec"
        )
    })
}
