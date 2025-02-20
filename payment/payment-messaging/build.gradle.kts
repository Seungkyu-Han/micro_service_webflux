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
    implementation(project(":payment:payment-domain"))

    implementation(project(":infrastructure:kafka"))
    implementation("org.apache.avro:avro:1.12.0")

    //KAFKA
    implementation("io.projectreactor.kafka:reactor-kafka")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}