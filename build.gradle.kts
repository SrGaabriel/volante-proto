plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.kotlinx.dataframe") version "0.11.1"
    application
}

group = "io.github.volanteweb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:dataframe:0.11.1")
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.4")
    implementation("it.skrape:skrapeit:1.3.0-alpha.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}