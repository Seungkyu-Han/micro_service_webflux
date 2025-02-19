plugins {
    kotlin("jvm") version "1.9.25"
}

group = "seungkyu.msa.service"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:bson:5.3.1")


    implementation("org.jetbrains.kotlin:kotlin-reflect")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}