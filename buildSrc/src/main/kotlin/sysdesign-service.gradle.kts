/**
 * Plugin that defines a launchable service type project (that uses the Quarkus platform).
 * Add this plugin to a project to inherit preset configuration, including the dependencies that are
 * typically used for this type of service: REST API utilities, Kafka client, JUnit, etc.
 */

plugins {
    java
    id("io.quarkus")
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
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-container-image-jib")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-test-security")
    testImplementation("io.rest-assured:rest-assured")
}

group = "org.example"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.compileTestJava {
    dependsOn("jandex")
}
