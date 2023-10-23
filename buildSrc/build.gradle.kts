/**
 * Gradle build file defining the 'buildSrc' project.
 * 'buildSrc' is a built-in Gradle construct that allows implementing common build logic.
 */

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.kordamp.gradle:jandex-gradle-plugin:0.13.2")
    implementation("io.quarkus:gradle-application-plugin:2.13.0.Final")
}
