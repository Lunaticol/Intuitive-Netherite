package intuitive.netherite;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;

public class SoulBlastingRecipe extends AbstractCookingRecipe {
  public SoulBlastingRecipe(Identifier id, String group, CookingRecipeCategory category, Ingredient input,
      ItemStack output, float experience, int cookTime) {
    super(RecipeType.BLASTING, id, group, category, input, output, experience, cookTime);
  }

  @Override
  public ItemStack createIcon() {
    return new ItemStack(Blocks.BLAST_FURNACE);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return SoulBlastingRecipeSerializer.SOUL_BLASTING;
  }

  @Override
  public boolean matches(Inventory inventory, World world) {
    if (inventory instanceof AbstractFurnaceBlockEntity furnace) {
      if (furnace instanceof SoulChargeFurnace soulFurnace) {
        ItemStack fuelSlot = furnace.getStack(1);
        boolean hasSoulChargeInFuelSlot = fuelSlot.isOf(IntunethItems.SOUL_CHARGE);
        return (soulFurnace.isUsingSoulCharge() || hasSoulChargeInFuelSlot) && super.matches(inventory, world);
      }
    }
    return false;
  }
}
