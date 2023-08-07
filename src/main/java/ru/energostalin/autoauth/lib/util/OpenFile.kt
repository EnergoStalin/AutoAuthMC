package ru.energostalin.autoauth.lib.util

import ru.energostalin.autoauth.lib.util.OSDetector.isLinux
import ru.energostalin.autoauth.lib.util.OSDetector.isMac
import ru.energostalin.autoauth.lib.util.OSDetector.isWindows
import java.awt.Desktop
import java.io.File


fun open(file: File): Boolean {
    return try {
        if (isWindows) {
            Runtime.getRuntime().exec(
                arrayOf(
                    "rundll32", "url.dll,FileProtocolHandler",
                    file.absolutePath
                )
            )
            true
        } else if (isLinux || isMac) {
            Runtime.getRuntime().exec(
                arrayOf(
                    "/usr/bin/open",
                    file.absolutePath
                )
            )
            true
        } else {
            // Unknown OS, try with desktop
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file)
                true
            } else {
                false
            }
        }
    } catch (e: Exception) {
        e.printStackTrace(System.err)
        false
    }
}