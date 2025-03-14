package ru.energostalin.autoauth.lib

import ru.energostalin.autoauth.Config
import ru.energostalin.autoauth.lib.generators.PlainPasswordGenerator
import ru.energostalin.autoauth.lib.storages.JsonStorage
import ru.energostalin.autoauth.lib.storages.StorageAdapter

object PasswordManagerFactory {
    fun createDefault(): PasswordManager {
        val folder = Config.Static.dir

        val storage = StorageAdapter(folder)
        return PasswordManager(storage, PlainPasswordGenerator())
    }
}
