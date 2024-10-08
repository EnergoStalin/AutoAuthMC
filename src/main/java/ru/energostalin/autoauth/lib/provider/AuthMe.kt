package ru.energostalin.autoauth.lib.provider

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Style
import net.minecraft.text.Text
import ru.energostalin.autoauth.lib.AuthState
import ru.energostalin.autoauth.lib.PasswordManagerFactory.createDefault

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

class AuthMe(private val client: MinecraftClient) {

    private val manager = createDefault()

    private val address: String
        get() = client.currentServerEntry!!.address
    private val playerName: String
        get() = client.player!!.name.string

    private fun sendChatCommand(msg: String) {
        client.networkHandler!!.sendChatCommand(msg)
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
        if(AuthState.state.get() == AuthState.State.WAITING_FOR_ANSWER) return

        val pass = manager.getPassword(address, playerName)
        if (pass.isNullOrEmpty()) {
            client.player!!.sendMessage(
                Text.literal("No password saved for current server. Required to login once manually.")
                    .setStyle(Style.EMPTY.withColor(0xffec4f))
            )
            AuthState.state.set(AuthState.State.MANUAL_LOGIN_REQUIRED)
        } else {
            sendChatCommand("login $pass")
            AuthState.state.set(AuthState.State.WAITING_FOR_ANSWER)
        }
    }

    private fun register() {
        if(AuthState.state.get() == AuthState.State.WAITING_FOR_ANSWER) return
        val pass = manager.generateRandomAndSaveOrGetSaved(address, playerName)
        sendChatCommand("register $pass $pass")
        AuthState.state.set(AuthState.State.WAITING_FOR_ANSWER)
    }

    fun handleGameMessage(msg: String) {
        if(AuthState.state.get() == AuthState.State.LOGGED_IN || AuthState.state.get() == AuthState.State.TIMED_OUT) return

        if(AuthState.state.get() == AuthState.State.WAITING_FOR_ANSWER && isLoginSuccessful(msg)) {
            AuthState.state.set(AuthState.State.LOGGED_IN)
            return
        }

        if (AuthState.state.get() != AuthState.State.UNKNOWN) return

        val pass = manager.getPassword(address, playerName)
        if(!pass.isNullOrEmpty()) {
            login()
        }

        if(isServerLoginRequested(msg)) {
            if (AuthState.state.get() != AuthState.State.MANUAL_LOGIN_REQUIRED) login()
        } else if (isServerRegisterRequested(msg)) {
            register()
        }
    }

    fun handleChatCommand(msg: String) {
        if(AuthState.state.get() != AuthState.State.MANUAL_LOGIN_REQUIRED) return

        if (isLoginCommand(msg)) {
            savePasswordFromLoginCommand(msg)
        }
    }
}