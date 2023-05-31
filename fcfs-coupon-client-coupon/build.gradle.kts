dependencies {
    implementation(project(":fcfs-coupon-api-coupon-dto"))
    implementation(project(":fcfs-coupon-common"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // mock web server
    testImplementation("org.mock-server:mockserver-netty:5.15.0")
}
