package net.satisfy.camping.fabric.core.network.packet;

import net.minecraft.server.level.ServerPlayer;
import net.satisfy.camping.core.network.CampingPacketHandler;

public class FabricOpenEnderPackC2SPacket {

    public static void receive(ServerPlayer player) {
        CampingPacketHandler.openEnderPackMenu(player);
    }
}
