plugins {
    kotlin("jvm") version "2.3.0-Beta2"
    kotlin("plugin.serialization") version "2.2.21"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "net.orca"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2025.10.31-1.21.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Logger
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.20")

    // Console
    implementation("org.jline:jline-terminal-jansi:3.21.0")
    implementation("net.minecrell:terminalconsoleappender:1.3.0")
    implementation("org.jline:jline-terminal:3.9.0")
    implementation("org.jline:jline-reader:3.9.0")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-api:2.22.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-core:2.22.0")

    // Lamp
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.14")
    implementation("io.github.revxrsal:lamp.minestom:4.0.0-rc.14")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25)) // Minestom has a minimum Java version of 25
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "net.orca.MainKt" // Change this to your main class
        }

        from("src/main/resources") {
            include("**/*.*")
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