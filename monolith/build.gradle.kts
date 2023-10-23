plugins {
    id("sysdesign-service")
}

dependencies {
    implementation(project(":analytics-service"))
    implementation(project(":rating-service"))
    implementation(project(":catalogus-service"))
    implementation(project(":staff-service"))
    implementation(project(":ticket-service"))
}
