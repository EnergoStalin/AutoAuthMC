package ru.energostalin.autoauth.common.controllers

public interface MinecraftClientController {
    currentServerAddress(): String
    currentPlayerName(): String
    sendChatCommand(msg: String): String
    sendMessage(msg: String): String
    sendErrorMessage(msg: String): String
}
