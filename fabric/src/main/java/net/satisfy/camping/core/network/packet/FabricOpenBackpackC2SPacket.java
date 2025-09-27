package net.satisfy.camping.core.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleMenuProvider;
import net.satisfy.camping.Camping;
import net.satisfy.camping.core.world.BackpackContainer;
import net.satisfy.camping.core.world.inventory.BackpackScreenHandler;
import net.satisfy.camping.core.world.item.BackpackBlockItem;
import net.satisfy.camping.core.registry.CampingItems;
import org.jetbrains.annotations.NotNull;

public class FabricOpenBackpackC2SPacket implements CustomPacketPayload {
    public static final Type<FabricOpenBackpackC2SPacket> TYPE = new Type<>(Camping.identifier("open_backpack"));
    public static final FabricOpenBackpackC2SPacket INSTANCE = new FabricOpenBackpackC2SPacket();
    public static final StreamCodec<FriendlyByteBuf, FabricOpenBackpackC2SPacket> CODEC = StreamCodec.unit(INSTANCE);

    public static void receive(ServerPlayer player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.is(CampingItems.ENDERPACK) || chest.is(CampingItems.ENDERBAG)) {
            Component title = chest.getHoverName();
            player.openMenu(new SimpleMenuProvider((id, inv, p) -> ChestMenu.threeRows(id, inv, player.getEnderChestInventory()), title));
            player.awardStat(Stats.OPEN_ENDERCHEST);
            return;
        }
        if (!(chest.getItem() instanceof BackpackBlockItem)) return;
        openPortable(player, chest.getHoverName());
    }

    private static void openPortable(ServerPlayer player, Component title) {
        BackpackContainer container = BackpackContainer.forEquipment(player, EquipmentSlot.CHEST);
        MenuConstructor ctor = (sync, inv, p) -> new BackpackScreenHandler(sync, inv, container, BlockPos.ZERO);
        MenuProvider provider = new MenuProvider() {
            @Override public @NotNull Component getDisplayName() { return title; }
            @Override public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, Player p) { return ctor.createMenu(id, inv, p); }
        };
        player.openMenu(provider);
    }

    @Override public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}
