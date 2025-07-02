package net.satisfy.camping.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.camping.core.world.item.ModSpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

@Mixin(Mob.class)
public class FabricMobMixin {

    @Unique
    private Player playerReference;

    @Unique
    private InteractionHand handReference;

    @Inject(method = "checkAndHandleImportantInteractions", at = @At("HEAD"))
    private void camping$checkAndHandleImportantInteractionsHead(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        playerReference = player;
        handReference = hand;
    }

    @ModifyVariable(method = "checkAndHandleImportantInteractions", at = @At("STORE"), ordinal = 0)
    private Optional<Mob> camping$checkAndHandleImportantInteractionsStore(Optional<Mob> mobOptional) {
        return mobOptional.isPresent() ? mobOptional : getOptional(playerReference, handReference);
    }

    @Unique
    private Optional<Mob> getOptional(Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (!(item instanceof ModSpawnEggItem modSpawnEgg) || !(player.level() instanceof ServerLevel serverLevel)) return Optional.empty();

        Mob self = (Mob) (Object) this;
        return modSpawnEgg.spawnOffspringFromSpawnEgg(player, self, modSpawnEgg.getDefaultType().get(), serverLevel, self.position(), stack);
    }

    @Inject(method = "checkAndHandleImportantInteractions", at = @At("TAIL"))
    private void camping$checkAndHandleImportantInteractionsTail(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        playerReference = null;
    }
}
