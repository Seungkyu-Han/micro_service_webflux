plugins {
    kotlin("jvm") version "1.9.25"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common:common-domain"))
    implementation("io.projectreactor:reactor-core:3.7.3")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}