package net.satisfy.camping.neoforge.core.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.satisfy.camping.Camping;

/**
 * @author wdog5
 * For new network system
 * since neoforge reworked the network system
 * @see <a href="https://neoforged.net/news/20.4networking-rework">...</a>
 */
public class EnderPackPayload implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<EnderPackPayload> TYPE = new Type<>(Camping.identifier("ender_pack"));
    public static final StreamCodec<FriendlyByteBuf, EnderPackPayload> STREAM_CODEC = StreamCodec.unit(new EnderPackPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
