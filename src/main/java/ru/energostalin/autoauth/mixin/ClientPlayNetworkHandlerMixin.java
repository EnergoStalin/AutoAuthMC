package ru.energostalin.autoauth.mixin;

import net.minecraft.network.packet.s2c.play.*;
import ru.energostalin.autoauth.AutoAuthKt;
import ru.energostalin.autoauth.lib.AuthState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.Timer;
import java.util.TimerTask;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    private final Timer timer = new Timer();

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        AuthState.INSTANCE.getState().set(AuthState.State.UNKNOWN);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(AuthState.INSTANCE.getState().get() == AuthState.State.LOGGED_IN) return;
                AuthState.INSTANCE.getState().set(AuthState.State.TIMED_OUT);
            }
        }, 30000);
    }

    @Inject(method = "sendChatCommand", at = @At("RETURN"))
    private void sendChatCommand(String command, CallbackInfo ci) {
        if(AuthState.INSTANCE.getState().get() == AuthState.State.LOGGED_IN) {
            return;
        }

        AuthState.INSTANCE.getProvider().handleChatCommand(command);
    }

    @Inject(method = "onGameMessage", at = @At("RETURN"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if(AuthState.INSTANCE.getState().get() == AuthState.State.LOGGED_IN) {
            return;
        }

        AuthState.INSTANCE.getProvider().handleGameMessage(packet.content().getString());
    }

}
