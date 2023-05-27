dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // mock web server
    testImplementation("com.squareup.okhttp3:mockwebserver")
}
