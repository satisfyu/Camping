package net.satisfy.camping.core.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.satisfy.camping.core.network.packet.FabricOpenBackpackC2SPacket;

public final class FabricCampingNetwork {
    private static boolean commonDone;

    public static void registerCommon() {
        if (commonDone) return;
        commonDone = true;
        PayloadTypeRegistry.playC2S().register(FabricOpenBackpackC2SPacket.TYPE, FabricOpenBackpackC2SPacket.CODEC);
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(FabricOpenBackpackC2SPacket.TYPE, (p, c) -> c.server().execute(() -> FabricOpenBackpackC2SPacket.receive(c.player())));
    }

    public static void registerClient() {}
}
