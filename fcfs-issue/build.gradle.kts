dependencies {
    implementation(project(":core"))
    implementation(project(":coupon"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring data redis
    "api"("org.springframework.boot:spring-boot-starter-data-redis")

    // embedded redis
    implementation("it.ozimov:embedded-redis:0.7.2")
}
