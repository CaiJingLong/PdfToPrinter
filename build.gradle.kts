import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish") version "0.22.0"
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

// about publish

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.S01, true)
    signAllPublications()

    pom {
        name.set("PdfToPrinter")
        description.set("Pdf format to pwg or pclm")
        inceptionYear.set("2022")
        url.set("https://github.com/CaiJingLong/PdfToPrinter")
        licenses {
            license {
                name.set("MIT Style")
                url.set("https://github.com/CaiJingLong/PdfToPrinter/blob/main/LICENSE")
                distribution.set("https://github.com/CaiJingLong/PdfToPrinter/blob/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("caijinglong")
                name.set("Cai Jing Long")
                url.set("https://github.com/caijinglong/")
            }
        }
        scm {
            url.set("https://github.com/CaiJingLong/PdfToPrinter")
            connection.set("https://github.com/CaiJingLong/PdfToPrinter.git")
            developerConnection.set("https://github.com/CaiJingLong/PdfToPrinter.git")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "LocalBuildDir"
            url = uri("${buildDir}/publishing-repository")
        }
    }
}
