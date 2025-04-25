package intuitive.netherite.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.item.ItemStack;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceAccessor {

  @Accessor("inventory")
  DefaultedList<ItemStack> getInventory();

  @Accessor("cookTime")
  int getCookTime();

  @Accessor("cookTime")
  void setCookTime(int cookTime);

  @Accessor("cookTimeTotal")
  int getCookTimeTotal();

  @Accessor("cookTimeTotal")
  void setCookTimeTotal(int cookTimeTotal);

  @Accessor("burnTime")
  int getBurnTime();

  @Accessor("burnTime")
  void setBurnTime(int burnTime);

}
