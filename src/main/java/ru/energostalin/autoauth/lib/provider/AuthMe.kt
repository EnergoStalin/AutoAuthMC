package ru.energostalin.autoauth.lib.provider

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Style
import net.minecraft.text.Text
import ru.energostalin.autoauth.lib.AuthState
import ru.energostalin.autoauth.lib.PasswordManagerFactory.createDefault

class AuthMe(private val client: MinecraftClient) {

    private val manager = createDefault()

    private val address: String
        get() = client.currentServerEntry!!.address
    private val playerName: String
        get() = client.player!!.name.string

    private fun sendChatCommand(msg: String) {
        client.networkHandler!!.sendChatCommand(msg)
    }

    private fun savePassword(password: String) {
        manager.savePassword(address, playerName, password)
    }

    private fun login() {
        if(AuthState.state == AuthState.State.WAITING_FOR_ANSWER) return

        val pass = manager.getPassword(address, playerName)
        if (pass.isNullOrEmpty()) {
            client.player!!.sendMessage(
                Text.literal("No password saved for current server. Required to login once manually.")
                    .setStyle(Style.EMPTY.withColor(0xffec4f))
            )
            AuthState.state = AuthState.State.MANUAL_LOGIN_REQUIRED
        } else {
            sendChatCommand("login $pass")
            AuthState.state = AuthState.State.WAITING_FOR_ANSWER
        }
    }

    private fun register() {
        if(AuthState.state == AuthState.State.WAITING_FOR_ANSWER) return
        val pass = manager.generateRandomAndSaveOrGetSaved(address, playerName)
        sendChatCommand("register $pass $pass")
        AuthState.state = AuthState.State.WAITING_FOR_ANSWER
    }

    fun handleGameMessage(msg: String) {
        if(AuthState.state == AuthState.State.LOGGED_IN) return
        else if (AuthState.state == AuthState.State.UNKNOWN) {
            val pass = manager.getPassword(address, playerName)
            if(!pass.isNullOrEmpty()) {
                login()
            }

            if ((msg.contains("/login") || msg.contains("/l") || msg.contains("/register") || msg.contains("/reg"))) {
                if (msg.contains("/login") && AuthState.state != AuthState.State.MANUAL_LOGIN_REQUIRED || msg.contains("/l") && AuthState.state != AuthState.State.MANUAL_LOGIN_REQUIRED)  {
                    login()
                } else if (msg.contains("/register") || msg.contains("/reg")) {
                    register()
                }
            } else if (msg.contains("Successful login!")) {
                AuthState.state = AuthState.State.LOGGED_IN
            }
        }
    }

    fun handleChatCommand(msg: String) {
        if(AuthState.state == AuthState.State.LOGGED_IN) return

        if (msg.contains("login") && AuthState.state == AuthState.State.MANUAL_LOGIN_REQUIRED || msg.contains("l") && AuthState.state == AuthState.State.MANUAL_LOGIN_REQUIRED) {
            savePassword(msg.split(' ')[1])
        }
    }
}