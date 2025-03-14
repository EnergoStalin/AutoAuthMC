package ru.energostalin.autoauth.builder.conventions

import ru.energostalin.autoauth.builder.utils.*

data class Versions(
    val constraint: String,
    val mixin: String,
    val java: Int = 21
)

fun Project.setupForMinecraftVersion(minecraftVersion: String, versions: Versions) {
    val fabric = FabricVersionFetcher(minecraftVersion)

    apply(plugin = "fabric-loom")
    apply(plugin = "jvm")

    val modVersion = getEnvOrDefault("GITHUB_REF_NAME", "v1.1").replace("v", "")

    dependencies {
        minecraft()
        mappings()
        modImplementaion()
    }

    tasks."processResources" {
        val versionProperties =
            mapOf(
                "mixin" to versions.mixin,
                "version" to modVersion,
                "minecraft_version" to versions.constraint,
                "loader_version" to fabric.loader
            )

        inputs.properties(versionProperties)
        filteringCharset = "UTF-8"
        filesMatching("fabric.mod.json") { expand(versionProperties) }
        filesMatching("autoauth.mixin.json") { expand(versionProperties) }
    }

    tasks."remapJar" {
        archiveFileName.set("${Properties.mod_name.lowercase()}-${Versions.constraint}-fabric.jar")
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = Versions.java.toString()
        targetCompatibility = Versions.java.toString()
        options.encoding = "UTF-8"
    }
}
