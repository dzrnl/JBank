plugins {
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

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.11.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    implementation("org.springframework:spring-context:6.1.14")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.javadoc)
}
