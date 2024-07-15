package net.satisfy.camping.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.camping.client.CampingClient;

public class CampingClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CampingClient.preInitClient();
        CampingClient.onInitializeClient();
    }
}
