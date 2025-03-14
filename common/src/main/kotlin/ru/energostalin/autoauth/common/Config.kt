package ru.energostalin.autoauth.common

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import java.io.Closeable

class Config(val directory: Path, val configFilePath: Path) : Closeable {
    data class Mod(val useEncryptedStorage: Boolean) {}

    val mod: Mod

    init {
        if (!directory.exists()) {
            directory.createDirectory()
        }

        this.mod = loadModConfig()
    }

    private fun loadModConfig(): Config.Mod {
        var mod: Config.Mod? = try {
            Files.newInputStream(configFilePath)?.use { ifs ->
                ifs.bufferedReader().use { br ->
                    JsonReader(br).use {
                        Gson().fromJson(it, Config.Mod::class.java)
                    }
                }
            }
        } catch (err: Exception) { null }

        if (mod != null) {
            return mod
        }

        mod = Config.Mod(true)

        saveModConfig(mod)

        return mod
    }

    private fun saveModConfig(config: Config.Mod) {
        Files.newOutputStream(configFilePath).use { ofs ->
            ofs.bufferedWriter().use { bw ->
                JsonWriter(bw).use {
                    Gson().toJson(config, Config.Mod::class.java, it)
                }
            }
        }
    }

    override fun close() {
        saveModConfig(this.mod)
    }
}

