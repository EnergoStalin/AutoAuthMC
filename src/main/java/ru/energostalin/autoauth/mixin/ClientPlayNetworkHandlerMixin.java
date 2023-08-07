package ru.energostalin.autoauth.mixin;

import ru.energostalin.autoauth.lib.PasswordManager;
import ru.energostalin.autoauth.lib.PasswordManagerFactory;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
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
                Log.debug(LogCategory.MIXIN, "Login prompt received.");
                String pass = manager.getPassword(address, playerName);

                if(pass == null) {
                    client.player.sendMessage(
                            Text.literal("No password saved for current server. Saving entry for this server with empty password. To specify it manually click here.")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/aapasswords")).withUnderline(true).withColor(0xffec4f)));
                    manager.saveEmpty(address, playerName);
                } else if(pass.isEmpty()) {
                    client.player.sendMessage(
                            Text.literal("Empty password saved for current server. To specify it manually click here.")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/aapasswords")).withUnderline(true).withColor(0xb2a537)));
                } else {
                    sendChatCommand("login " + pass);
                }
            } else if(msg.contains("/register")) {
                Log.debug(LogCategory.MIXIN, "Register prompt received.");
                String pass = manager.generateRandomAndSaveOrGetSaved(address, playerName);
                sendChatCommand("register " + pass + " " + pass );
            }
        }
    }
}
