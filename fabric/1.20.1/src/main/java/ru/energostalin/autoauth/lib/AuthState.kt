package ru.energostalin.autoauth.lib

import net.minecraft.client.MinecraftClient
import ru.energostalin.autoauth.lib.provider.AuthMe
import java.util.concurrent.atomic.AtomicReference

object AuthState {

    enum class State {
        LOGGED_IN,
        UNKNOWN,
        TIMED_OUT,
        WAITING_FOR_ANSWER,
        MANUAL_LOGIN_REQUIRED
    }

    val provider = AuthMe(MinecraftClient.getInstance())
    val state: AtomicReference<State> = AtomicReference(State.UNKNOWN)
}