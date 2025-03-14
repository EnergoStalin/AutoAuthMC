package ru.energostalin.autoauth.lib.storages

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.Base64
import java.nio.file.Files
import java.nio.file.Path

class EncryptedJsonStorage(dataDir: Path) : EncryptedMutableStorage {

    val jsonStorage: JsonStorage = JsonStorage(dataDir)
    override val uri: Path = jsonStorage.uri

    override fun addOne(record: Storage.ServerRecord) {
        record.pass = Base64.getEncoder().encodeToString(record.pass.toByteArray())
        record.encrypted = "base64"

        jsonStorage.addOne(record)
    }

    override fun getOne(ip: String, name: String): Storage.ServerRecord? {
        val record = jsonStorage.getOne(ip, name)
        if (record == null) return record

        return decrypt(record)
    }

    override fun decrypt(record: Storage.ServerRecord): Storage.ServerRecord {
        record.pass = String(Base64.getDecoder().decode(record.pass))
        return record
    }
}
