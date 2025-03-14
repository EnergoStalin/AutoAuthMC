package ru.energostalin.autoauth.common.storages

interface EncryptedMutableStorage : MutableStorage {
    fun decrypt(record: Storage.ServerRecord): Storage.ServerRecord
}
