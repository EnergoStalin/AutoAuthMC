package ru.energostalin.autoauth.build.utils

import java.io.File
import java.net.HttpURLConnection
import java.net.URI

fun send_get(url: String): String {
    val connection = URI(url).toURL().openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            return connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            throw Exception("STATUS not ok")
        }
    } finally {
        connection.disconnect()
    }
}

data class FabricVersion(val yarn: String, val loader: String, val version: String)

class FabricVersionFetcher(val minecraft_version: String) {
    private fun findVersionsOf(name: String, lines: List<String>): List<String> {
        return lines.filter { it.contains(name) }
    }

    private fun findLatestVersion(name: String, lines: List<String>): String {
        return findVersionsOf(name, lines)
                .filter {
                    it.contains("+" + minecraft_version) || it.contains(minecraft_version + "+")
                }
                .sorted()
                .last()
                .replace(name + "-", "")
    }

    private fun findLatestLoaderVersion(lines: List<String>): String {
        return findVersionsOf("fabric-loader", lines).last().replace("fabric-loader-", "")
    }

    fun fetch(): FabricVersion {
        val vfile = File(System.getProperty("java.io.tmpdir"), "fabric-versions-cache.txt")
        var lines = mutableListOf("")
        if (!vfile.exists()) {
            val cache = send_get("https://maven.fabricmc.net/jdlist.txt")
            lines.addAll(cache.split("\n"))
            vfile.writeText(cache)
        } else {
            lines.addAll(vfile.readLines())
        }

        val versions =
                FabricVersion(
                        findLatestVersion("yarn", lines),
                        findLatestLoaderVersion(lines),
                        findLatestVersion("fabric-api", lines),
                )

        return versions
    }
}
