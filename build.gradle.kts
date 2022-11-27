import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
}

group = "top.kikt"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // jipp
    implementation("com.hp.jipp:jipp-core:0.7.14")
    implementation("com.hp.jipp:jipp-pdl:0.7.14")

    // pdfbox
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}