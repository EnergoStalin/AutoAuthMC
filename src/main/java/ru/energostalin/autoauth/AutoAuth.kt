package ru.energostalin.autoauth

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.energostalin.autoauth.lib.PasswordManagerFactory
import ru.energostalin.autoauth.lib.osutil.open
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

val logger: Logger = LoggerFactory.getLogger(AutoAuth::class.java)

class AutoAuth : ModInitializer {

    override fun onInitialize() {
        if(!Config.Static.dir.exists())
            Config.Static.dir.createDirectory()

        ClientCommandRegistrationCallback.EVENT.register(fun(dispatcher, _) {
            dispatcher.register(
                literal("aapasswords").executes(fun (_): Int {
                    open(PasswordManagerFactory.createDefault().getStorage().uri.toFile())
                    return 0
                })
            )
        })

        logger.info("Initialized AutoAuth")
    }
}