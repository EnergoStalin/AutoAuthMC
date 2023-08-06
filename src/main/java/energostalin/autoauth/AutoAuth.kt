package energostalin.autoauth

import energostalin.autoauth.lib.PasswordManagerFactory
import energostalin.autoauth.lib.util.open
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

class AutoAuth : ModInitializer {
    override fun onInitialize() {
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