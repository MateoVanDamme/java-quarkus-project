plugins {
    id("sysdesign-service")
}

dependencies {
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-keycloak-authorization")
    implementation(project(":model"))
}
