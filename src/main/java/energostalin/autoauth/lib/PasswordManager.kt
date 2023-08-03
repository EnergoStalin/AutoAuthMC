package energostalin.autoauth.lib

import energostalin.autoauth.lib.generators.PasswordGenerator
import energostalin.autoauth.lib.storages.Storage

class PasswordManager(private val storage: Storage, private val generator: PasswordGenerator) {
    fun generateRandomAndSaveOrGetSavedForServer(ip: String, name: String): String {
        val entry = storage.getOne(ip, name)
        if(entry != null) return entry.pass

        val pass = generator.generate(24)
        storage.addOne(Storage.ServerRecord(ip, pass, name))
        return pass
    }

    fun getPassword(ip: String, name: String): String? {
        return storage.getOne(ip, name)?.pass
    }
}