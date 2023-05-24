dependencies {
    implementation(project(":core"))
    implementation(project(":coupon"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring data redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // embedded redis
    implementation("it.ozimov:embedded-redis:0.7.2")

    // rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.0")
}
