package ru.energostalin.autoauth.common.password

import ru.energostalin.autoauth.common.password.PasswordGenerator
import ru.energostalin.autoauth.common.storages.MutableStorage
import ru.energostalin.autoauth.common.storages.Storage

class PasswordManager(private val mutableStorage: MutableStorage, private val generator: PasswordGenerator) {
    fun generateRandomAndSaveOrGetSaved(ip: String, name: String): String {
        val entry = mutableStorage.getOne(ip, name)
        if(entry != null) return entry.pass

        val pass = generator.generate(24)
        mutableStorage.addOne(Storage.ServerRecord(ip, name, pass, null))
        return pass
    }

    fun getPassword(ip: String, name: String): String? {
        return mutableStorage.getOne(ip, name)?.pass
    }

    fun savePassword(ip: String, name: String, pass: String) {
        return mutableStorage.addOne(Storage.ServerRecord(ip, name, pass, null))
    }

    fun getStorage(): Storage {
        return mutableStorage
    }
}
