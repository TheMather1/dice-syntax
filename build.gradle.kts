import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    `maven-publish`
    `java-library`
}

group = "no.mather.ttrpg"
version = System.getenv("RELEASE_VERSION")

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/TheMather1/${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register("gpr", MavenPublication::class) {
            artifactId = "dice-syntax"
            from(components["java"])
            pom {
                name.set("dice-syntax")
                description.set("Advanced dice syntax and parsing.")
                url.set("https://github.com/TheMather1/${rootProject.name}")
                inceptionYear.set("2022")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        name.set("Mathias Sand Jahren")
                        email.set("the.mather1@gmail.com")
                        url.set("https://github.com/TheMather1")
                        timezone.set("CET")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/TheMather1/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com/TheMather1/${rootProject.name}.git")
                    url.set("https://github.com/TheMather1/${rootProject.name}/tree/main")
                }
            }
        }
    }
}

tasks{
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}
