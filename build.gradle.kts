plugins {
    java
}

group = "dev.blac.auth_freeze"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.blac.auth_freeze.App"
    }

    // Optionally include compileClasspath in the JAR
    // This is not recommended for Spigot, usually just use 'compileOnly'
    // To shade dependencies instead, use the Shadow plugin
}
