package ru.energostalin.autoauth.common.provider.authme

import ru.energostalin.autoauth.common.controllers.MinecraftClientController
import ru.energostalin.autoauth.common.passwords.PasswordManagerFactory
import ru.energostalin.autoauth.common.AuthState

val SUCCESS_LOGIN_PATTERNS = listOf(
    Regex("Successful login!"),
)

val SERVER_LOGIN_REQUEST_PATTERNS = listOf(
    Regex("\\s/login\\s"),
    Regex("\\s/l\\s")
)

val SERVER_REGISTER_REQUEST_PATTERNS = listOf(
    Regex("\\s/register\\s"),
    Regex("\\s/reg\\s")
)

val CLIENT_LOGIN_REQUEST_PATTERNS = listOf(
    Regex("^/login "),
    Regex("^/l ")
)

class AuthMe(private val client: MinecraftClientController) {
    private val manager = PasswordManagerFactory.createDefault()
    private var state = AuthState.UNKNOWN

    private val address: String
        get() = client.currentServerAddress()
    private val playerName: String
        get() = client.currentPlayerName()

    private fun sendChatCommand(msg: String) {
        client.sendChatCommand(msg)
    }

    private fun savePasswordFromLoginCommand(cmd: String) {
        val pos = cmd.indexOfFirst { e -> e.isWhitespace() }
        val password = cmd.slice(pos until cmd.length)

        manager.savePassword(address, playerName, password)
    }

    private fun isServerLoginRequested(msg: String): Boolean {
        return SERVER_LOGIN_REQUEST_PATTERNS.any { it.containsMatchIn(msg) }
    }

    private fun isServerRegisterRequested(msg: String): Boolean {
        return SERVER_REGISTER_REQUEST_PATTERNS.any { it.containsMatchIn(msg) }
    }

    private fun isLoginSuccessful(msg: String): Boolean {
        return SUCCESS_LOGIN_PATTERNS.any { it.containsMatchIn(msg) }
    }

    private fun isLoginCommand(msg: String): Boolean {
        return CLIENT_LOGIN_REQUEST_PATTERNS.any { it.containsMatchIn(msg) }
    }

    private fun login() {
        if(state == AuthState.WAITING_FOR_ANSWER) return

        val pass = manager.getPassword(address, playerName)
        if (pass.isNullOrEmpty()) {
            client.sendErrorMessage("No password saved for current server. Required to login once manually.")
            //     Text.literal()
            //         .setStyle(Style.EMPTY.withColor(0xffec4f))
            // )
            state = AuthState.MANUAL_LOGIN_REQUIRED
        } else {
            sendChatCommand("login $pass")
            state = AuthState.WAITING_FOR_ANSWER
        }
    }

    private fun register() {
        if(state == AuthState.WAITING_FOR_ANSWER) return
        val pass = manager.generateRandomAndSaveOrGetSaved(address, playerName)
        sendChatCommand("register $pass $pass")
        state = AuthState.WAITING_FOR_ANSWER)
    }

    fun handleGameMessage(msg: String) {
        if(state == AuthState.LOGGED_IN || state == AuthState.TIMED_OUT) return

        if(state == AuthState.WAITING_FOR_ANSWER && isLoginSuccessful(msg)) {
            state = AuthState.LOGGED_IN
            return
        }

        if (state != AuthState.UNKNOWN) return

        val pass = manager.getPassword(address, playerName)
        if(!pass.isNullOrEmpty()) {
            login()
        }

        if(isServerLoginRequested(msg)) {
            if (state != AuthState.MANUAL_LOGIN_REQUIRED) login()
        } else if (isServerRegisterRequested(msg)) {
            register()
        }
    }

    fun handleChatCommand(msg: String) {
        if(state != AuthState.MANUAL_LOGIN_REQUIRED) return

        if (isLoginCommand(msg)) {
            savePasswordFromLoginCommand(msg)
        }
    }
}
