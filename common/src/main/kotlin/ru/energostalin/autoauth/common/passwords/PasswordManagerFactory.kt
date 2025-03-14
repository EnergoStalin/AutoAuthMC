package ru.energostalin.autoauth.common.passwords

import ru.energostalin.autoauth.Config
import ru.energostalin.autoauth.common.generators.PlainPasswordGenerator
import ru.energostalin.autoauth.common.storages.JsonStorage
import ru.energostalin.autoauth.common.storages.StorageAdapter

object PasswordManagerFactory {
    fun createDefault(): PasswordManager {
        val folder = Config.Static.dir

        val storage = StorageAdapter(folder)
        return PasswordManager(storage, PlainPasswordGenerator())
    }
}
