plugins {
    java
}

tasks.named("build") {
    dependsOn(tasks.javadoc)
}
