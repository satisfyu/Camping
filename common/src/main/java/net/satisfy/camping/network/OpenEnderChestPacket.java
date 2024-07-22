package net.satisfy.camping.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.satisfy.camping.registry.ObjectRegistry;

public class OpenEnderChestPacket {

	public static void register() {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, PacketHandler.OPEN_ENDER_CHEST_PACKET_ID, OpenEnderChestPacket::receive);
	}

	public static void receive(FriendlyByteBuf buffer, NetworkManager.PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();
			MinecraftServer server = player.getServer();

			if (server != null &&
					(player.getInventory().contains(ObjectRegistry.ENDERPACK_ITEM.get().getDefaultInstance()) ||
							player.getInventory().contains(ObjectRegistry.ENDERBAG_ITEM.get().getDefaultInstance()))) {
				player.openMenu(new SimpleMenuProvider((containerId, inventory, playerEntity) ->
						ChestMenu.threeRows(containerId, inventory, player.getEnderChestInventory()),
						Component.translatable("container.camping.enderpack")
				));
			}
		});
	}
}
