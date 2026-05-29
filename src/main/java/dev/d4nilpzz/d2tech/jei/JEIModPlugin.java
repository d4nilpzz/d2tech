package dev.d4nilpzz.d2tech.jei;

import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipe;
import dev.d4nilpzz.d2tech.registry._RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(MODID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new HydraulicPressRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        List<HydraulicPressRecipe> recipes = minecraft.level.getRecipeManager()
                .getAllRecipesFor(_RecipeTypes.HYDRAULIC_PRESS_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();

        registration.addRecipes(HydraulicPressRecipeCategory.RECIPE_TYPE, recipes);
    }
}
