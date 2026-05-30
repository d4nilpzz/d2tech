package dev.d4nilpzz.d2tech.recipe;

import dev.d4nilpzz.d2tech.registry._RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AdvancedCraftingRecipe implements Recipe<AdvancedCraftingRecipeInput> {

    private final ItemStack recipeMemory;
    private final List<String> pattern;
    private final Map<Character, ItemStack> key;
    private final ItemStack result;

    public AdvancedCraftingRecipe(ItemStack recipeMemory, List<String> pattern, Map<Character, ItemStack> key, ItemStack result) {
        this.recipeMemory = recipeMemory;
        this.pattern = pattern;
        this.key = key;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull AdvancedCraftingRecipeInput input, @NotNull Level level) {
        if (!ItemStack.isSameItem(input.memory(), recipeMemory)) return false;
        if (input.grid().length != 9) return false;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                ItemStack expected = key.getOrDefault(pattern.get(row).charAt(col), ItemStack.EMPTY);
                ItemStack actual = input.grid()[row * 3 + col];
                if (!ItemStack.isSameItem(expected, actual)) return false;
                if (!expected.isEmpty() && actual.getCount() < expected.getCount()) return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull AdvancedCraftingRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w >= 3 && h >= 3;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return _RecipeTypes.ADVANCED_CRAFTING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return _RecipeTypes.ADVANCED_CRAFTING_TYPE.get();
    }

    public ItemStack recipeMemory() {
        return recipeMemory;
    }

    public List<String> pattern() {
        return pattern;
    }

    public Map<Character, ItemStack> key() {
        return key;
    }
}
