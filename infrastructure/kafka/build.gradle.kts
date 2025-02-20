import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

plugins {
    kotlin("jvm")
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.avro:avro:1.12.0")
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

tasks.named("compileJava").configure {
    dependsOn(generateAvro)
}