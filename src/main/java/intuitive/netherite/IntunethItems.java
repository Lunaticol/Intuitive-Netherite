package intuitive.netherite;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IntunethItems {

  public static Item register(Item item, String id) {
    Identifier itemID = new Identifier(Intuneth.MOD_ID, id);
    Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
    return registeredItem;
  }

  public static final Item SOUL_CHARGE = register(
      new Item(new Item.Settings()),
      "soul_charge");

  public static void initialize() {

    FuelRegistry.INSTANCE.add(IntunethItems.SOUL_CHARGE, 200);

  }
}
