@file:Suppress("MayBeConstant")

fun getEnvOrDefault(env: String, default: String = ""): String {
    return try {
        System.getenv(env)
    } catch(e: Exception) {
        default
    }
}

object Versions {
    val kotlin = "2.0.20"
    val fabric_loom = "1.7.3"

    val minecraft = "1.21"
    val yarn_mappings = "1.21+build.9"
    val fabric_loader = "0.16.4"

    val mod = getEnvOrDefault("GITHUB_REF_NAME", "v1.1").replace("v", "")
    val fabric = "0.102.0+1.21"

    val java = 21
}

object Dependencies {
    val minecraft = "com.mojang:minecraft:${Versions.minecraft}"
    val yarn = "net.fabricmc:yarn:${Versions.yarn_mappings}:v2"
    val fabric_loader = "net.fabricmc:fabric-loader:${Versions.fabric_loader}"
    val fabric_api = "net.fabricmc.fabric-api:fabric-api:${Versions.fabric}"
}

object Properties {
    val maven_group = "ru.energostalin"
    val mod_name = "AutoAuth"
}