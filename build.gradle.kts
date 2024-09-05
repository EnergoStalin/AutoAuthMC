plugins {
    id("fabric-loom") version Versions.fabric_loom
    kotlin("jvm") version Versions.kotlin
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft(Dependencies.minecraft)
    mappings(Dependencies.yarn)
    modImplementation(Dependencies.fabric_loader)
    modImplementation(Dependencies.fabric_api)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Versions.java.toString()
    targetCompatibility = Versions.java.toString()
}

tasks.processResources {
    inputs.property("version", Versions.mod)
    inputs.property("minecraft_version", Versions.minecraft)
    inputs.property("loader_version", Versions.fabric_loader)

    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "version" to Versions.mod,
            "minecraft_version" to Versions.minecraft,
            "loader_version" to Versions.fabric_loader
        ))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.jar {
    version = Versions.mod
}

tasks.remapJar {
    archiveFileName.set(
        "${Properties.mod_name.lowercase()}-${Versions.minecraft}-fabric.jar"
    )
}