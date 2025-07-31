package net.satisfy.camping.fabric.core.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.satisfy.camping.Camping;

/**
 * @author wdog5
 * For new network system
 * since minecraft reworked the network system
 */
public class EnderPackPayload implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<EnderPackPayload> TYPE = new Type<>(Camping.identifier("ender_pack"));
    public static final StreamCodec<FriendlyByteBuf, EnderPackPayload> STREAM_CODEC = StreamCodec.unit(new EnderPackPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
