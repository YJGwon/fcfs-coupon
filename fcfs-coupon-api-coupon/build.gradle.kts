dependencies {
    implementation(project(":fcfs-coupon-api-coupon-dto"))
    implementation(project(":fcfs-coupon-domain-coupon"))
    implementation(project(":fcfs-coupon-common"))
    implementation(project(":fcfs-coupon-log"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.0")

    // for history test double
    testImplementation(project(":fcfs-coupon-domain-history"))

}
