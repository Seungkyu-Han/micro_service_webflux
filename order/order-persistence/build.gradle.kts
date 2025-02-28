plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "seungkyu.msa.service"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common:common-domain"))
    implementation(project(":order:order-domain"))
    implementation(project(":infrastructure:outbox"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}