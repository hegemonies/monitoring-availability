import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    kotlin("kapt") version "1.5.0" apply true
    id("com.google.cloud.tools.jib") version "3.0.0"
    id("org.springframework.experimental.aot") version "0.10.0-SNAPSHOT"
}

group = "org.bravo"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
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

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
//    this.builder = "openjdk:11.0.11-jre"
    this.builder = "hegemonies/native-builder:latest"
    this.imageName = "bravo/monitoringavailability:latest"
    environment = mapOf("BP_NATIVE_IMAGE" to "true")
}

tasks.withType<org.gradle.jvm.tasks.Jar> {
    manifest {
        attributes("Main-Class" to "org.bravo.monitoringavailability.MonitoringAvailabilityApplicationKt")
    }
}

springBoot {
    mainClassName = "org.bravo.monitoringavailability.MonitoringAvailabilityApplicationKt"
}

jib {
    container {
        this.mainClass = "org.bravo.monitoringavailability.MonitoringAvailabilityApplicationKt"
    }

    from {
        image = "openjdk:11.0.11-jre"
    }

    to {
        this.image = "bravo/monitoringavailability:latest"
    }
}
