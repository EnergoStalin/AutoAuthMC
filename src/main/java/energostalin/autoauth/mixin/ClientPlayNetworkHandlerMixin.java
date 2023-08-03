package energostalin.autoauth.mixin;

import energostalin.autoauth.lib.PasswordManager;
import energostalin.autoauth.lib.PasswordManagerFactory;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract void sendChatCommand(String command);

    @Shadow public abstract boolean sendCommand(String command);

    @Inject(method = "onGameMessage", at = @At("RETURN"))
    private void onGameMessage(net.minecraft.network.packet.s2c.play.GameMessageS2CPacket packet, CallbackInfo ci) {
        assert client.player != null;
        if(!client.player.groundCollision && !client.player.isInsideWaterOrBubbleColumn()) {
            return;
        }
        String msg = packet.content().getString();

        if(msg.contains("/login") || msg.contains("/register")) {
            PasswordManager manager = PasswordManagerFactory.INSTANCE.createDefault();
            String address = Objects.requireNonNull(client.getCurrentServerEntry()).address;
            String playerName = client.player.getName().getString();

            if(msg.contains("/login")) {
                Log.info(LogCategory.MIXIN, "Login prompt received.");
                String pass = manager.getPassword(address, playerName);
                if(pass == null) {
                    sendChatCommand("msg @s No login saved for current server");
                    return;
                }

                sendChatCommand("login " + pass);

                return;
            }

            if(msg.contains("/register")) {
                Log.info(LogCategory.MIXIN, "Register prompt received.");
                String pass = manager.generateRandomAndSaveOrGetSavedForServer(address, playerName);
                sendChatCommand("register " + pass + " " + pass );
            }
        }
    }
}
