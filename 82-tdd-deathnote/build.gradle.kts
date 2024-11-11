import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("org.danilopianini.gradle-java-qa") version "1.74.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.3")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
}

tasks.withType<Test> {
    // Use junit platform for unit tests
    useJUnitPlatform()
    testLogging {
        events(*(TestLogEvent.values())) // events("passed", "skipped", "failed")
    }
    testLogging.showStandardStreams = true
}
