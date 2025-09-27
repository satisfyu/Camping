package net.satisfy.camping.core.world.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.satisfy.camping.core.world.BackpackContainer;
import net.satisfy.camping.platform.Services;
import org.jetbrains.annotations.NotNull;

public class ForgeBackpackScreenHandler extends BackpackScreenHandler {

    public ForgeBackpackScreenHandler(int id, Inventory inv, BackpackContainer container, BlockPos pos) {
        super(id, inv, container, pos);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Services.PLATFORM.getEquippedBackpack(player) != null && !Services.PLATFORM.getEquippedBackpack(player).isEmpty();
    }
}
