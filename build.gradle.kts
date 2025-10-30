plugins {
    kotlin("jvm") version "2.2.20"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "net.orca"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Minestom
    implementation("net.minestom:minestom:2025.07.14-1.21.7")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.apache.logging.log4j:log4j-core:2.25.2")
    implementation("org.apache.logging.log4j:log4j-api:2.25.2")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.2")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-api:2.22.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-core:2.22.0")

    // MinestomPvP
    implementation("com.github.TogAr2:MinestomPvP:56a831b41c")
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
            attributes["Main-Class"] = "net.orca.MainKt" // Change this to your main class
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