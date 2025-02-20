import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.avro:avro:1.12.0")
    implementation(project(":common:common-domain"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.projectreactor.kafka:reactor-kafka")
    implementation("io.confluent:kafka-avro-serializer:7.8.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}


val generateAvro:TaskProvider<GenerateAvroJavaTask> = tasks.register("generateAvro", GenerateAvroJavaTask::class.java) {
    source("src/main/resources/avro")
    setOutputDir(file("src/main/java"))
    stringType.set("String")
    enableDecimalLogicalType = true
}

tasks.named("compileKotlin") {
    dependsOn("generateAvro")
}
