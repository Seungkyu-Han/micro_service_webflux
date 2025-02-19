plugins {
    kotlin("jvm") version "1.9.25"
}

group = "seungkyu.msa.service"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(":common:common-domain")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}