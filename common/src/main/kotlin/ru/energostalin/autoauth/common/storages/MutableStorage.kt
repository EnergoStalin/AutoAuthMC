package ru.energostalin.autoauth.common.storages

interface MutableStorage : Storage {
    fun addOne(record: Storage.ServerRecord)
}
