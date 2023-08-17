package ru.energostalin.autoauth.lib

import ru.energostalin.autoauth.Config
import ru.energostalin.autoauth.lib.generators.PlainPasswordGenerator
import ru.energostalin.autoauth.lib.storages.JsonStorage

object PasswordManagerFactory {
    fun createDefault(): PasswordManager {
        val folder = Config.Static.dir

        val storage = JsonStorage(folder)
        return PasswordManager(storage, PlainPasswordGenerator())
    }
}