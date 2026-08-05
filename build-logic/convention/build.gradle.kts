plugins {
    `kotlin-dsl`
}

group = "acekode.debtshare.buildlogic"

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.spotless.plugin.gradle)
    implementation(libs.jacoco.core)
}
