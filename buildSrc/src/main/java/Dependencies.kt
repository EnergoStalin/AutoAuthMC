@file:Suppress("MayBeConstant")

fun getEnvOrDefault(env: String, default: String = ""): String {
    return try {
        System.getenv(env)
    } catch(e: Exception) {
        default
    }
}

object Versions {
    val kotlin = "1.8.21"
    val fabric_loom = "1.2.7"

    val minecraft = "1.20.1"
    val yarn_mappings = "1.20.1+build.10"
    val fabric_loader = "0.14.22"

    val mod = getEnvOrDefault("GITHUB_REF_NAME", "v1.1").replace("v", "")
    val fabric = "0.86.1+1.20.1"

    val java = 17
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