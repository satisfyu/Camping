package net.satisfy.camping.core.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.satisfy.camping.core.network.CampingPacketHandler;

import java.util.function.Supplier;

public class ForgeOpenEnderPackC2SPacket {

    public ForgeOpenEnderPackC2SPacket() {}
    public ForgeOpenEnderPackC2SPacket(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        CampingPacketHandler.openEnderPackMenu(supplier.get().getSender());
        return true;
    }
}
