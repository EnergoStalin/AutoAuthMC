package ru.energostalin.autoauth.mixin;

import net.minecraft.network.packet.s2c.play.*;
import ru.energostalin.autoauth.lib.AuthState;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        AuthState.INSTANCE.setState(AuthState.State.UNKNOWN);
    }

    @Inject(method = "sendChatCommand", at = @At("RETURN"))
    private void sendChatCommand(String command, CallbackInfo ci) {
        if(AuthState.INSTANCE.getState() == AuthState.State.LOGGED_IN) {
            return;
        }
        AuthState.INSTANCE.getProvider().handleChatCommand(command);
    }

    @Inject(method = "onGameMessage", at = @At("RETURN"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if(AuthState.INSTANCE.getState() == AuthState.State.LOGGED_IN) {
            return;
        }
        AuthState.INSTANCE.getProvider().handleGameMessage(packet.content().getString());
    }
}
