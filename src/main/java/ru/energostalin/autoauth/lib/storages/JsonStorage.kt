package ru.energostalin.autoauth.lib.storages

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.nio.file.Files
import java.nio.file.Path


class JsonStorage(private val datadir: Path) : MutableStorage {

    override val uri: Path = datadir.resolve("passwords.json")

    private val gson: Gson = Gson()

    init {
        if(!Files.exists(uri))
            Files.createFile(uri)
    }

    override fun addOne(record: Storage.ServerRecord) {
        val passwords = readList()
        passwords.add(record)

        saveList(passwords)
    }

    override fun getOne(ip: String, name: String): Storage.ServerRecord? {
        return readList().find { e -> e.ip == ip && e.user == name; }
    }

    private fun readList(): MutableList<Storage.ServerRecord> {
        @Suppress("UNNECESSARY_SAFE_CALL")
        return try {
            Files.newInputStream(uri)?.use { ifs ->
                ifs.bufferedReader().use {br ->
                    JsonReader(br).use {
                        gson.fromJson(it, object : TypeToken<ArrayList<Storage.ServerRecord>>() {}.type)
                    }
                }
            } ?: mutableListOf()
        } catch (err: Exception) {
            mutableListOf()
        }
    }

    private fun saveList(passwords: MutableList<Storage.ServerRecord>) {
        Files.newOutputStream(uri).use { ofs ->
            ofs.bufferedWriter().use { bw ->
                JsonWriter(bw).use {
                    gson.toJson(passwords, passwords.javaClass, it)
                }
            }
        }
    }
}