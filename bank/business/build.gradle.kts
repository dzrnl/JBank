import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "ru.dzrnl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("org.springframework:spring-context:6.1.14")

    implementation("org.springframework.kafka:spring-kafka")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.javadoc)
}
