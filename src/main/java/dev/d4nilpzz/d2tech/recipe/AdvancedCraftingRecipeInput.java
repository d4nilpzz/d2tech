package dev.d4nilpzz.d2tech.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record AdvancedCraftingRecipeInput(ItemStack memory, ItemStack[] grid) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        if (i == 0) return memory;
        if (i >= 1 && i <= 9) return grid[i - 1];
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 10;
    }
}
