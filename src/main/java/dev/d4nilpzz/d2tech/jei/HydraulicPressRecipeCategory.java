package dev.d4nilpzz.d2tech.jei;

import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipe;
import dev.d4nilpzz.d2tech.registry._Blocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    private final IDrawableAnimated arrow;

    public HydraulicPressRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(140, 70);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(_Blocks.HYDRAULIC_PRESS));
        this.arrow = guiHelper.createAnimatedRecipeArrow(100);
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
        return 70;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HydraulicPressRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 22)
                .addItemStacks(Arrays.asList(recipe.ingredient().getItems()))
                .setStandardSlotBackground();

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 22)
                .addItemStack(recipe.getResultItem(null))
                .setOutputSlotBackground();
    }

    @Override
    public void draw(HydraulicPressRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 50, 22);

        var font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, "§8" + recipe.energy() + " FE", 4, 52, 0xFFFFFFFF);
        guiGraphics.drawString(font, "§8" + String.format("%.1f", recipe.time() / 20.0) + "s", 4, 62, 0xFFFFFFFF);
    }
}
