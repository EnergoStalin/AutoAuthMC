package ru.energostalin.autoauth.lib.storages

interface EncryptedMutableStorage : MutableStorage {
    fun decrypt(record: Storage.ServerRecord): Storage.ServerRecord
}
