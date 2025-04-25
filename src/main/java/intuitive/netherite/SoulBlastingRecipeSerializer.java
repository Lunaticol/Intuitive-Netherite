package intuitive.netherite;

import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SoulBlastingRecipeSerializer {
  public static final RecipeSerializer<SoulBlastingRecipe> SOUL_BLASTING = register(
      "soul_blasting",
      new CookingRecipeSerializer<>(SoulBlastingRecipe::new, 100));

  private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
    return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("intuneth", id), serializer);
  }
}
