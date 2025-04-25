package intuitive.netherite.mixin;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.state.property.Properties;

import intuitive.netherite.SoulChargeFurnace;

@Mixin(FurnaceBlock.class)
public class FurnaceBlockMixin {

  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    if ((Boolean) state.get(Properties.LIT)) {
      double d = pos.getX() + 0.5;
      double e = pos.getY();
      double f = pos.getZ() + 0.5;
      if (random.nextDouble() < 0.1) {
        world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
      }

      Direction direction = state.get(AbstractFurnaceBlock.FACING);
      Direction.Axis axis = direction.getAxis();
      double h = random.nextDouble() * 0.6 - 0.3;
      double i = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52 : h;
      double j = random.nextDouble() * 6.0 / 16.0;
      double k = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : h;

      boolean isSoulCharged = state.get(SoulChargeFurnace.SOUL_CHARGED);
      DefaultParticleType particleType = isSoulCharged ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

      world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
      world.addParticle(particleType, d + i, e + j, f + k, 0.0, 0.0, 0.0);
    }
  }
}