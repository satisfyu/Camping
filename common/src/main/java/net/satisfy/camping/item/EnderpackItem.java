package net.satisfy.camping.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.camping.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class EnderpackItem extends BlockItem implements Equipable {
	protected final ArmorMaterial material;
	protected final ArmorItem.Type type;
	private final ResourceLocation enderpackTexture;

	public EnderpackItem(Block block, ArmorMaterial material, ArmorItem.Type type, ResourceLocation enderpackTexture, Properties properties) {
		super(block, properties.stacksTo(1));
		this.material = material;
		this.type = type;
		this.enderpackTexture = enderpackTexture;
	}

	public ResourceLocation getEnderpackTexture() {
		return enderpackTexture;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		BlockPos relativePos = clickedPos.relative(context.getClickedFace());

		BlockState blockState = null;

		if (context.getItemInHand().getItem() == ObjectRegistry.ENDERPACK_ITEM.get()) {
			blockState = ObjectRegistry.ENDERPACK.get().defaultBlockState();
		} else if (context.getItemInHand().getItem() == ObjectRegistry.ENDERBAG_ITEM.get()) {
			blockState = ObjectRegistry.ENDERBAG.get().defaultBlockState();
		}
		if (blockState != null && player != null && level.isEmptyBlock(relativePos)) {
			if (level.setBlockAndUpdate(relativePos, blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, player.getDirection().getOpposite()))) {
				player.playSound(SoundEvents.ENDER_CHEST_CLOSE, 0.5f, 0.5f);
				level.gameEvent(player, GameEvent.BLOCK_PLACE, relativePos);
				context.getItemInHand().shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return InteractionResult.FAIL;
	}

	@Override
	public @NotNull EquipmentSlot getEquipmentSlot() {
		return this.type.getSlot();
	}

	@Override
	public @NotNull SoundEvent getEquipSound() {
		return this.material.getEquipSound();
	}
}
