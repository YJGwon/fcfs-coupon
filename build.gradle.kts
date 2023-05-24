plugins {
    `java-library`
    id("org.springframework.boot") version "3.0.6" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

allprojects {
    group = "com.coupop"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java-library")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":coupon"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // spring data redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // spring data mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // spring mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // embedded redis
    implementation("it.ozimov:embedded-redis:0.7.2")

    // embedded mongodb
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.6.2")

    // rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.0")
}
