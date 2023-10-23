plugins {
    id("sysdesign-service")
}

dependencies {
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-keycloak-authorization")
    implementation("io.quarkus:quarkus-rest-client-reactive-jackson")
    implementation(project(":model"))
}
