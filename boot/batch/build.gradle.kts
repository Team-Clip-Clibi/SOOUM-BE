plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.clip"
version = "0.0.1-SNAPSHOT"
description = "batch"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":data"))
    implementation(project(":infra"))
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.batch:spring-batch-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(("org.springframework.boot:spring-boot-starter-data-redis"))
    //firebase
    implementation("com.google.firebase:firebase-admin:9.4.1")

    //tsid generator
    implementation("com.github.f4b6a3:tsid-creator:5.2.6")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
