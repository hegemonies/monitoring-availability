import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    kotlin("kapt") version "1.5.0" apply true
}

group = "org.bravo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.influxdb:influxdb-client-kotlin:2.2.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.hibernate:hibernate-validator:7.0.1.Final")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.5.0")

    // Json
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")

    // Native
    implementation("net.java.dev.jna:jna:4.5.0")

    // Annotation properties
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
