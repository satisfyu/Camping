package net.satisfy.camping.core.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.camping.Constants;
import org.jetbrains.annotations.NotNull;

public enum NeoForgeOpenBackpackC2SPacket implements CustomPacketPayload {
    INSTANCE;

    public static final Type<NeoForgeOpenBackpackC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "open_backpack"));
    public static final StreamCodec<FriendlyByteBuf, NeoForgeOpenBackpackC2SPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
