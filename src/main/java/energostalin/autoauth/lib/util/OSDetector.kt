package energostalin.autoauth.lib.util

import java.util.*


object OSDetector {
    var isWindows = false
        private set
    var isLinux = false
        private set
    var isMac = false
        private set

    init {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        isWindows = os.contains("win")
        isLinux = os.contains("nux") || os.contains("nix")
        isMac = os.contains("mac")
    }
}