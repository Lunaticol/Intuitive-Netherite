package intuitive.netherite.mixin;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import intuitive.netherite.IntunethItems;
import intuitive.netherite.SoulChargeFurnace;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements SoulChargeFurnace {

  @Unique
  private static final Logger LOGGER = LogManager.getLogger("SoulChargeFurnace");

  @Unique
  private ItemStack lastFuelSnapshot = ItemStack.EMPTY;

  @Unique
  private ItemStack lastInputSnapshot = ItemStack.EMPTY;

  @Unique
  private boolean usingSoulCharge = false;

  @Override
  public ItemStack getLastFuelSnapshot() {
    return lastFuelSnapshot;
  }

  @Override
  public void setLastFuelSnapshot(ItemStack stack) {
    this.lastFuelSnapshot = stack;
  }

  @Override
  public boolean isUsingSoulCharge() {
    return usingSoulCharge;
  }

  @Override
  public void setUsingSoulCharge(boolean value) {
    this.usingSoulCharge = value;
  }

  @Inject(method = "tick", at = @At("HEAD"))
  private static void onTick(World world, BlockPos pos, BlockState state,
      AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {

    if (world.isClient)
      return;

    FurnaceAccessor furnace = (FurnaceAccessor) blockEntity;
    SoulChargeFurnace soulFurnace = (SoulChargeFurnace) blockEntity;
    DefaultedList<ItemStack> inventory = furnace.getInventory();

    ItemStack fuelSlot = inventory.get(1);
    ItemStack input = inventory.get(0);
    ItemStack output = inventory.get(2);

    // Cache fuel
    if (furnace.getBurnTime() <= 0 && furnace.getCookTime() == 0 && !fuelSlot.isEmpty()) {
      soulFurnace.setLastFuelSnapshot(fuelSlot.copy());
    }

    // Soul Charge state
    if (furnace.getBurnTime() <= 0) {
      furnace.setCookTime(0);
      soulFurnace.setUsingSoulCharge(false);
      if (state.get(SoulChargeFurnace.SOUL_CHARGED)) {
        world.setBlockState(pos, state.with(SoulChargeFurnace.SOUL_CHARGED, false));
      }
    } else {
      ItemStack cachedFuel = soulFurnace.getLastFuelSnapshot();
      boolean isSoul = cachedFuel.isOf(IntunethItems.SOUL_CHARGE);
      soulFurnace.setUsingSoulCharge(isSoul);
      if (state.get(SoulChargeFurnace.SOUL_CHARGED) != isSoul) {
        world.setBlockState(pos, state.with(SoulChargeFurnace.SOUL_CHARGED, isSoul));
      }
    }

    // Last fuel
    if (furnace.getBurnTime() == 1 && !fuelSlot.isEmpty()) {
      soulFurnace.setLastFuelSnapshot(fuelSlot.copy());
    }
    // Smelting
    if (!input.isEmpty()) {
      Optional<? extends AbstractCookingRecipe> smeltingRecipeOpt = world.getRecipeManager()
          .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(input),
              world);

      smeltingRecipeOpt.ifPresent(recipe -> {
        ItemStack result = recipe.getOutput(world.getRegistryManager());
        boolean canInsert = output.isEmpty() ||
            (ItemStack.canCombine(output, result) && output.getCount() + result.getCount() <= output.getMaxCount());

        if (canInsert) {
          int cookTime = furnace.getCookTime();
          int cookTimeTotal = recipe.getCookTime();

          if (soulFurnace.isUsingSoulCharge()) {
            cookTime += 1;
          }

          if (cookTime >= cookTimeTotal) {
            if (output.isEmpty()) {
              inventory.set(2, result.copy());
            } else {
              output.increment(result.getCount());
            }
            input.decrement(1);
            cookTime = 0;
          }

          furnace.setCookTime(cookTime);
          furnace.setCookTimeTotal(cookTimeTotal);
        }
      });

    }

  }
}
