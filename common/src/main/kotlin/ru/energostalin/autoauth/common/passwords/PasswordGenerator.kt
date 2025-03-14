package ru.energostalin.autoauth.common.passwords

interface PasswordGenerator {
    fun generate(length: Int): String
}
