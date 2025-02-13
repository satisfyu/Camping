package net.satisfy.camping.core.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.network.packet.FabricOpenEnderPackC2SPacket;

public class CampingMessagesFabric {

    public static final ResourceLocation ENDER_PACK = Camping.identifier("ender_pack");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ENDER_PACK, FabricOpenEnderPackC2SPacket::receive);
    }
}
