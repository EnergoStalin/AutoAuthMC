package ru.energostalin.autoauth.lib.storages

interface MutableStorage : Storage {
    fun addOne(record: Storage.ServerRecord)

}