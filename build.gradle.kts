plugins {
    application
    alias(libs.plugins.lombok)
    jacoco
}

group = "io.allitov"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

jacoco {
    toolVersion = "0.8.14"
}

dependencies {
    implementation(libs.log4j.slf4j)

    testImplementation(libs.assertj)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.run {
    mainClass.set("io.allitov.mpt.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        html.required.set(true)
    }
    dependsOn(tasks.test)
}