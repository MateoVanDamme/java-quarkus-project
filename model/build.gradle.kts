plugins {
    id("sysdesign-commonlib")
}

dependencies {
    // Dependency on the Panache MongoDB library, allows declaring Topic as an Active Record
    // Note that 'api' is used as dependency type instead of 'implementation', enabling modules depending on 'model' to use the Panache API as well.
    api("io.quarkus:quarkus-mongodb-panache")
}
