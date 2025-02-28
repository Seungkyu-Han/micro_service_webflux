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
    implementation(project(":infrastructure:saga"))
    implementation(project(":infrastructure:outbox"))
    implementation("org.mongodb:bson:5.3.1")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-tx:6.2.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}