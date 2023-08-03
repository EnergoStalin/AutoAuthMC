package energostalin.autoauth.lib.generators

interface PasswordGenerator {
    fun generate(length: Int): String
}