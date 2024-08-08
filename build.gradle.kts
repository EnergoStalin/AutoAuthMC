import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("org.slf4j:slf4j-api:1.7.30")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.13.3")
    implementation("org.apache.logging.log4j:log4j-api:2.13.3")
    implementation("org.apache.logging.log4j:log4j-core:2.13.3")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Versions.java.toString()
    targetCompatibility = Versions.java.toString()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = Versions.java.toString()
    }
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

val archiveName = "${Properties.mod_name.lowercase()}-${Versions.minecraft}-fabric"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.jar {
    version = Versions.mod
}

tasks.build {
    version = Versions.mod
}