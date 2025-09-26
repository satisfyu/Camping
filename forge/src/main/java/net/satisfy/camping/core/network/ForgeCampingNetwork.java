package net.satisfy.camping.core.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.satisfy.camping.Constants;
import net.satisfy.camping.core.network.packet.ForgeOpenBackpackC2SPacket;

public class ForgeCampingNetwork {
    

    private static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    @SuppressWarnings("removal")
    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Constants.MOD_ID, "camping_network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(ForgeOpenBackpackC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ForgeOpenBackpackC2SPacket::new)
                .encoder(ForgeOpenBackpackC2SPacket::toBytes)
                .consumerMainThread(ForgeOpenBackpackC2SPacket::handle)
                .add();
    }
    
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
