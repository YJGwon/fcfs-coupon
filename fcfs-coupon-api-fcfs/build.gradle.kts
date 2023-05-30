dependencies {
    implementation(project(":fcfs-coupon-common"))
    implementation(project(":fcfs-coupon-domain-fcfs"))
    implementation(project(":fcfs-coupon-domain-history"))
    implementation(project(":fcfs-coupon-client-coupon"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.0")

    testImplementation(project(":fcfs-coupon-client-coupon"))
}
