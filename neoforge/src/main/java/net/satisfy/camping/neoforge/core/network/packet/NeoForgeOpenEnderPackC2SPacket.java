package net.satisfy.camping.neoforge.core.network.packet;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.satisfy.camping.core.network.CampingPacketHandler;

/**
 * @author wdog5
 * For new network system
 * since neoforge reworked the network system
 * @see <a href="https://neoforged.net/news/20.4networking-rework">...</a>
 */
public class NeoForgeOpenEnderPackC2SPacket implements IPayloadHandler<EnderPackPayload> {

    public NeoForgeOpenEnderPackC2SPacket() {}

    @Override
    public void handle(EnderPackPayload arg, IPayloadContext iPayloadContext) {
        CampingPacketHandler.openEnderPackMenu(iPayloadContext.player());
    }
}
