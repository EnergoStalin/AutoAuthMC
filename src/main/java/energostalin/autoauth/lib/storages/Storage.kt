package energostalin.autoauth.lib.storages

import java.nio.file.Path

interface Storage {
    data class ServerRecord(val ip: String, val pass: String, val user: String)
    fun addOne(record: ServerRecord)
    fun getOne(ip: String, name: String): ServerRecord?

    val uri: Path
}