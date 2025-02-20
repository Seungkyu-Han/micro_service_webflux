plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {
    implementation(project(":common:common-domain"))
    implementation(project(":restaurant:restaurant-application"))
    implementation(project(":restaurant:restaurant-persistence"))
    implementation(project(":restaurant:restaurant-messaging"))
    implementation(project(":restaurant:restaurant-domain"))
    implementation(project(":infrastructure:kafka"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.confluent:kafka-avro-serializer:7.8.0")

    //SWAGGER
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.5.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}