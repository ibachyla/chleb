import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    checkstyle
    pmd
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.5"
    id("net.ltgt.errorprone") version "3.1.0"
    id("com.github.spotbugs") version "6.0.19"
}

group = "com.github.ibachyla"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("commons-validator:commons-validator:1.8.0")

    compileOnly("org.projectlombok:lombok")
    compileOnly("com.google.code.findbugs:annotations:3.0.1u2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:spring-mock-mvc")
    testImplementation("net.datafaker:datafaker:2.2.2")

    errorprone("com.google.errorprone:error_prone_core:2.28.0")
    errorprone("tech.picnic.error-prone-support:error-prone-contrib:0.16.1")
    errorprone("tech.picnic.error-prone-support:refaster-runner:0.16.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.errorprone.errorproneArgs = listOf(
        "-Xep:LexicographicalAnnotationAttributeListing:OFF",
        "-Xep:LexicographicalAnnotationListing:OFF"
    )
}

checkstyle {
    toolVersion = "10.14.0"
}

pmd {
    toolVersion = "6.55.0"
}

spotbugs {
    toolVersion = "4.8.3"
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
    }
}