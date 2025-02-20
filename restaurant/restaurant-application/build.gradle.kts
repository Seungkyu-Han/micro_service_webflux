plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common:common-domain"))
    implementation(project(":restaurant:restaurant-domain"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}