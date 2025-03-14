package ru.energostalin.autoauth.build.utils

fun getEnvOrDefault(env: String, default: String = ""): String {
    return try {
        System.getenv(env)
    } catch (e: Exception) {
        default
    }
}
