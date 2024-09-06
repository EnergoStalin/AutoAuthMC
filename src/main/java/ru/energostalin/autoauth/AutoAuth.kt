package ru.energostalin.autoauth

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import ru.energostalin.autoauth.lib.PasswordManagerFactory
import ru.energostalin.autoauth.lib.osutil.open

class AutoAuth : ModInitializer {

    override fun onInitialize() {
        Config.ensure()

        ClientCommandRegistrationCallback.EVENT.register(fun(dispatcher, _) {
            dispatcher.register(
                literal("aapasswords").executes(fun (_): Int {
                    open(PasswordManagerFactory.createDefault().getStorage().uri.toFile())
                    return 0
                })
            )
        })
    }
}
