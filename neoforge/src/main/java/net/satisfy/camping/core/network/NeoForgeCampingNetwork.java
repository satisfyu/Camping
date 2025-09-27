package net.satisfy.camping.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.network.packet.NeoForgeBackpackMenuOpener;
import net.satisfy.camping.core.network.packet.NeoForgeOpenBackpackC2SPacket;

public class NeoForgeCampingNetwork {

    public static void register(IEventBus bus) {
        bus.addListener(NeoForgeCampingNetwork::onRegisterPayloadHandlers);
    }

    private static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Constants.MOD_ID).versioned("1");
        registrar.playToServer(NeoForgeOpenBackpackC2SPacket.TYPE, NeoForgeOpenBackpackC2SPacket.STREAM_CODEC, NeoForgeCampingNetwork::handleOpenBackpack);
    }

    private static void handleOpenBackpack(NeoForgeOpenBackpackC2SPacket payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                NeoForgeBackpackMenuOpener.open(player);
            }
        });
    }

    public static void sendToServer(CustomPacketPayload payload) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(payload));
        }
    }

    public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) {
        player.connection.send(new ClientboundCustomPayloadPacket(payload));
    }
}
