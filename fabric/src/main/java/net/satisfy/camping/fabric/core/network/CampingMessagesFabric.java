package net.satisfy.camping.fabric.core.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Camping;
import net.satisfy.camping.fabric.core.network.packet.FabricOpenEnderPackC2SPacket;

public class CampingMessagesFabric {

    public static final ResourceLocation ENDER_PACK = Camping.identifier("ender_pack");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(new CustomPacketPayload.Type<>(ENDER_PACK), (customPacketPayload, context) -> {
            FabricOpenEnderPackC2SPacket.receive(context.player());
        });
    }
}
