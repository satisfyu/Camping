package net.satisfy.camping.core.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.network.packet.FabricOpenBackpackC2SPacket;

public class FabricCampingNetwork {
    public static class Packets {
        public static final ResourceLocation OPEN_BACKPACK = Camping.identifier("open_backpack");
    }

    public static void registerServerPacketReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.OPEN_BACKPACK, (server, player, handler, buf, responseSender) -> server.execute(() -> FabricOpenBackpackC2SPacket.receive(player)));
    }
}
