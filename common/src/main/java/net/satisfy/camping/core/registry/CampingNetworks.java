package net.satisfy.camping.core.registry;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.network.CampingPacketHandler;

@SuppressWarnings("removal")
public class CampingNetworks {

    public static final ResourceLocation ENDER_PACK_ID = Camping.identifier("ender_pack");

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ENDER_PACK_ID, (buf, context) -> {
            Player player = context.getPlayer();
            CampingPacketHandler.openEnderPackMenu(player);
        });
    }
}
