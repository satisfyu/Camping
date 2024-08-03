package net.satisfy.camping.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Camping;

public class PacketHandler {

	public static final ResourceLocation OPEN_ENDER_CHEST_PACKET_ID = new ResourceLocation(Camping.MODID, "open_ender_chest");

	public static void registerC2SPackets() {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, OPEN_ENDER_CHEST_PACKET_ID, OpenEnderChestPacket::receive);
	}
}
