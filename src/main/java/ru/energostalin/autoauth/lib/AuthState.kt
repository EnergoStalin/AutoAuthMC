package ru.energostalin.autoauth.lib

import net.minecraft.client.MinecraftClient
import ru.energostalin.autoauth.lib.provider.AuthMe

object AuthState {

    enum class State {
        LOGGED_IN,
        UNKNOWN,
        WAITING_FOR_ANSWER,
        MANUAL_LOGIN_REQUIRED
    }

    val provider = AuthMe(MinecraftClient.getInstance())
    var state: State = State.UNKNOWN
}