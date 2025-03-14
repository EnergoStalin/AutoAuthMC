package ru.energostalin.autoauth.common.provider.authme

enum class AuthState {
    LOGGED_IN,
    UNKNOWN,
    TIMED_OUT,
    WAITING_FOR_ANSWER,
    MANUAL_LOGIN_REQUIRED
}
