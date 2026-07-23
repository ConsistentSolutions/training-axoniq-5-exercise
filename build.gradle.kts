plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.axoniq.training"
version = "0.0.1-SNAPSHOT"
description = "training-axoniq-5"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation(platform("io.axoniq.framework:axoniq-framework-bom:5.1.1"))
    implementation("io.axoniq.framework:axoniq-spring-boot-starter")
    implementation("io.axoniq.platform:axoniq-platform-spring-boot-starter:5.1.0")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")

    testImplementation("org.axonframework.extensions.spring:axon-spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:2.0.5")

    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Each @AxonSpringBootTest class gets its own Spring context. When several such contexts are
    // reused across classes in one JVM, Spring restarts the cached context between classes and Axon
    // re-registers its event-sourced entities on restart, failing with RepositoryAlreadyRegisteredException.
    // Running each test class in a fresh JVM keeps the contexts isolated and the suite deterministic.
    forkEvery = 1
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.readLines()
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .forEach { line ->
                val (key, value) = line.split("=", limit = 2)
                environment(key.trim(), value.trim())
            }
    }
}
