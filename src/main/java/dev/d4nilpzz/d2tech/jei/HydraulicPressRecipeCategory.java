package dev.d4nilpzz.d2tech.jei;

import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipe;
import dev.d4nilpzz.d2tech.registry._Blocks;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class HydraulicPressRecipeCategory implements IRecipeCategory<HydraulicPressRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MODID, "hydraulic_press");
    public static final RecipeType<HydraulicPressRecipe> RECIPE_TYPE = new RecipeType<>(UID, HydraulicPressRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public HydraulicPressRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(140, 60);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(_Blocks.HYDRAULIC_PRESS));
    }

    @Override
    public @NotNull RecipeType<HydraulicPressRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.d2tech.hydraulic_press");
    }

    @Override
    public int getWidth() {
        return 140;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HydraulicPressRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 22)
                .addItemStacks(Arrays.asList(recipe.ingredient().getItems()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 22)
                .addItemStack(recipe.getResultItem(null));
    }
}
