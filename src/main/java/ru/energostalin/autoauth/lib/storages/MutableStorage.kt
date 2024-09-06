package ru.energostalin.autoauth.lib.storages

interface MutableStorage : Storage {
    interface ServerRecordAdapter {
        fun save(record: Storage.ServerRecord): Storage.ServerRecord
        fun load(record: Storage.ServerRecord?): Storage.ServerRecord?
    }

    fun addOne(record: Storage.ServerRecord)
}
