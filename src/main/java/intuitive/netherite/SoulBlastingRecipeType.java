package intuitive.netherite;

import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SoulBlastingRecipeType {
  public static final RecipeType<SoulBlastingRecipe> SOUL_BLASTING = register("soul_blasting");

  private static <T extends Recipe<?>> RecipeType<T> register(String id) {
    return Registry.register(Registries.RECIPE_TYPE, new Identifier("intuneth", id), new RecipeType<T>() {
      public String toString() {
        return id;
      }
    });
  }
}
