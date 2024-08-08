package ru.energostalin.autoauth

import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

class Config {
    object Static {
        val dir: Path = FabricLoader.getInstance().configDir.resolve("AutoAuth")
    }
}