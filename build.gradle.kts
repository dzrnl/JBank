plugins {
    java
}

tasks.build {
    dependsOn(tasks.javadoc)
}
