dependencies {
    implementation(project(":fcfs-coupon-common"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring data mongodb
    "api"("org.springframework.boot:spring-boot-starter-data-mongodb")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // spring mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // embedded mongodb
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.6.2")
}
