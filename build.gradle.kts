plugins {
    java
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.coupop"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring data redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // spring mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // embedded-redis
    implementation("it.ozimov:embedded-redis:0.7.2")

    // rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
