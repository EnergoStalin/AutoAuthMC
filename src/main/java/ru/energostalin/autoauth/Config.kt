package ru.energostalin.autoauth

import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

class Config {
    object Static {
        val dir: Path = FabricLoader.getInstance().configDir.resolve("AutoAuth")
    }

    companion object {
        fun ensure() {
            if(!Static.dir.exists())
                Static.dir.createDirectory()
        }
    }
}