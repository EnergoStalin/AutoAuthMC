package ru.energostalin.autoauth.lib.storages

import java.nio.file.Path

interface Storage {
    data class ServerRecord(val ip: String, val user: String, var pass: String)
    fun getOne(ip: String, name: String): ServerRecord?

    val uri: Path
}