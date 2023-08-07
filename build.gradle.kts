import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("fabric-loom") version Versions.fabric_loom
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft(Dependencies.minecraft)
    mappings(Dependencies.yarn)
    modImplementation(Dependencies.fabric_loader)

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation(Dependencies.fabric_api)
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
            )
        )
    }
}

tasks.withType(JavaCompile::class).configureEach {
    this.options.encoding = "UTF-8"
}

tasks.build {
    archivesName.set("${Properties.mod_name.lowercase()}-${Versions.minecraft}-fabric")
    version = Versions.mod
}
