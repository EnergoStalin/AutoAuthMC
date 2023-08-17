package ru.energostalin.autoauth

import net.fabricmc.loader.api.FabricLoader

class Config {
    object Static {
        val dir = FabricLoader.getInstance().configDir.resolve("AutoAuth")
    }
}