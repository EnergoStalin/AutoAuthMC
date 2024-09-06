package ru.energostalin.autoauth.lib.storages

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.Base64
import java.nio.file.Files
import java.nio.file.Path
import ru.energostalin.autoauth.Config

class StorageAdapter(dataDir: Path) : MutableStorage {
    val plain: JsonStorage = JsonStorage(dataDir)
    val encrypted: EncryptedJsonStorage = EncryptedJsonStorage(dataDir)
    val config: Config = Config()
    
    override val uri: Path = dataDir

    override fun addOne(record: Storage.ServerRecord) {
        if (config.mod.useEncryptedStorage) {
            encrypted.addOne(record)
        } else {
            plain.addOne(record)
        }
    }

    override fun getOne(ip: String, name: String): Storage.ServerRecord? {
        val record = plain.getOne(ip, name)
        if (record == null) return record


        return if (record.encrypted != null) {
            encrypted.decrypt(record)
        } else {
            record
        }
    }
}
