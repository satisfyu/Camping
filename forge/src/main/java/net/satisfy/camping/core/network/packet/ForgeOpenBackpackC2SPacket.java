package net.satisfy.camping.core.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeOpenBackpackC2SPacket {

    public ForgeOpenBackpackC2SPacket() {}
    public ForgeOpenBackpackC2SPacket(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        OpenBackpackC2SPacketHandler.handle(supplier.get().getSender());
        return true;
    }
}
