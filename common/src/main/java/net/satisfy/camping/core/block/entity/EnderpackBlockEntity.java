package net.satisfy.camping.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.camping.core.registry.EntityTypeRegistry;

public class EnderpackBlockEntity extends BlockEntity implements BlockEntityTicker<EnderpackBlockEntity> {
  
  public EnderpackBlockEntity(BlockPos pos, BlockState blockState) {
    super(EntityTypeRegistry.ENDERPACK_BLOCK_ENTITY.get(), pos, blockState);
  }
  
  @Override
  public void tick(Level level, BlockPos blockPos, BlockState blockState, EnderpackBlockEntity blockEntity) {
    
    float random = level.random.nextFloat();
    
    if (random <= 0.00001f) {
      level.addFreshEntity(new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(Items.ENDER_PEARL)));
    }
  }
}
