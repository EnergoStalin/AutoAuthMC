package ru.energostalin.autoauth.mixin;

import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import ru.energostalin.autoauth.AutoAuthKt;
import ru.energostalin.autoauth.lib.AuthState;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;


@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    @Unique
    @Mutable
    @Final private final MinecraftClient client;

    protected ClientPlayNetworkHandlerMixin(MinecraftClient client) {
        this.client = client;
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        AuthState.INSTANCE.setState(AuthState.State.UNKNOWN);

        AutoAuthKt.getLogger().info("joined to the server");
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
