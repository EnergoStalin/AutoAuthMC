package ru.energostalin.autoauth

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import net.fabricmc.loader.api.FabricLoader
import java.io.Closeable

class Config : Closeable {
    data class Mod(val useEncryptedStorage: Boolean) {}

    object Static {
        val dir: Path = FabricLoader.getInstance().configDir.resolve("AutoAuth")
        val configPath: Path = dir.resolve("config.json")
    }

    val mod: Mod

    init {
        Config.ensure()

        this.mod = loadModConfig()
    }

    private fun loadModConfig(): Config.Mod {
        var mod: Config.Mod? = try {
            Files.newInputStream(Config.Static.configPath)?.use { ifs ->
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
        Files.newOutputStream(Config.Static.configPath).use { ofs ->
            ofs.bufferedWriter().use { bw ->
                JsonWriter(bw).use {
                    Gson().toJson(config, Config.Mod::class.java, it)
                }
            }
        }
    }

    companion object {
        fun ensure() {
            if (!Static.dir.exists()) {
                Static.dir.createDirectory()
            }
        }
    }

    override fun close() {
        saveModConfig(this.mod)
    }
}

