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
    implementation(project(":bank:business"))
    implementation(project(":bank:data"))

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.11.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    implementation("org.springframework:spring-context:6.1.14")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.javadoc)
}
