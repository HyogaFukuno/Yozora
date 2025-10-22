plugins {
    kotlin("jvm") version "2.2.20"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "net.orca"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Minestom
    implementation("net.minestom:minestom:2025.10.05-1.21.8")

    // Kotlin Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.apache.logging.log4j:log4j-core:2.25.2")
    implementation("org.apache.logging.log4j:log4j-api:2.25.2")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.2")

    // JNoise
    implementation("de.articdive:jnoise-pipeline:4.1.0")

    // Lamp
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.14")
    implementation("io.github.revxrsal:lamp.minestom:4.0.0-rc.14")

    // Polar
    implementation("dev.hollowcube:polar:1.14.7")

    // LAMP
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.14")
    implementation("io.github.revxrsal:lamp.minestom:4.0.0-rc.14")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-api:2.22.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-core:2.22.0")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        javaParameters = true
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.example.Main" // Change this to your main class
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix on the shadowjar file.
    }
}