package dev.d4nilpzz.d2tech.jei;

import dev.d4nilpzz.d2tech.recipe.AdvancedCraftingRecipe;
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

import java.util.List;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class AdvancedCraftingTableRecipeCategory implements IRecipeCategory<AdvancedCraftingRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MODID, "advanced_crafting_table");
    public static final RecipeType<AdvancedCraftingRecipe> RECIPE_TYPE = new RecipeType<>(UID, AdvancedCraftingRecipe.class);

    private static final int ENERGY_COST = 1000;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public AdvancedCraftingTableRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(130, 90);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(_Blocks.ADVANCED_CRAFTING_TABLE));
        this.arrow = guiHelper.createAnimatedRecipeArrow(100);
    }

    @Override
    public @NotNull RecipeType<AdvancedCraftingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.d2tech.advanced_crafting_table");
    }

    @Override
    public int getWidth() {
        return 130;
    }

    @Override
    public int getHeight() {
        return 90;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AdvancedCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        // Memory item slot (top left)
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 4)
                .addItemStack(recipe.recipeMemory())
                .setStandardSlotBackground();

        // 3x3 grid starting below the memory item
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                char c = recipe.pattern().get(row).charAt(col);
                ItemStack stack = recipe.key().getOrDefault(c, ItemStack.EMPTY);
                int x = 4 + col * 18;
                int y = 26 + row * 18;

                if (!stack.isEmpty()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                            .addItemStack(stack)
                            .setStandardSlotBackground();
                }
            }
        }

        // Output slot (right of the grid)
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 40)
                .addItemStack(recipe.getResultItem(null))
                .setOutputSlotBackground();
    }

    @Override
    public void draw(AdvancedCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 72, 40);

        var font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, "§8" + ENERGY_COST + " FE", 4, 80, 0xFFFFFFFF);
    }
}
