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
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {
    implementation(project(":common:common-domain"))
    implementation(project(":customer:customer-domain"))

    implementation(project(":infrastructure:kafka"))
    implementation(project(":infrastructure:outbox"))
    implementation("org.apache.avro:avro:1.12.0")
    implementation("org.mongodb:bson:5.3.1")

    //KAFKA
    implementation("io.projectreactor.kafka:reactor-kafka")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.confluent:kafka-avro-serializer:7.8.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}