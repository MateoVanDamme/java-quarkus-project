// import gradle.kotlin.dsl.accessors._3b49af9815bdd3819056074c5deee3ed.implementation

/**
 * Plugin that defines a common library type project (that uses the Quarkus platform).
 * Add this plugin to a project to inherit preset configuration.
 */

plugins {
    `java-library`
    id("org.kordamp.gradle.jandex")
}

repositories {
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    testImplementation("io.quarkus:quarkus-junit5")
}

group = "org.example"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
