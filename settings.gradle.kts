pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "system-design-template"
include("model")

include("api-service")
include("processor-service")
include("rating-service")
include("ticket-service")
include("catalogus-service")
include("analytics-service")
include("staff-service")
include("monolith")