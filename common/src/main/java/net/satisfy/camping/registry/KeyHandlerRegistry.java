package net.satisfy.camping.registry;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.satisfy.camping.block.satpack.OpenBackpackPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class KeyHandlerRegistry {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CATEGORY = "key.camping.category";
    private static final String OPEN_BACKPACK_KEY = "key.camping.open_backpack";
    private static final KeyMapping open_backpack = new KeyMapping(OPEN_BACKPACK_KEY, InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY);

    static {
        KeyMappingRegistry.register(open_backpack);
        registerKeyHandler();
    }

    private static void registerKeyHandler() {
        ClientTickEvent.CLIENT_POST.register(client -> {
            if (open_backpack.consumeClick()) {
                Player player = client.player;
                if (player != null) {
                    LOGGER.info("Key B pressed by player: {}", player.getName().getString());
                    BlockPos pos = player.blockPosition();
                    LOGGER.info("Player position: {}", pos);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    new OpenBackpackPacket(pos).toBytes(buf);
                    NetworkManager.sendToServer(OpenBackpackPacket.ID, buf);
                    LOGGER.info("OpenBackpackPacket sent to server.");
                } else {
                    LOGGER.warn("Player is null when key B pressed.");
                }
            }
        });
    }
}
