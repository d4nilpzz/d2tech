package dev.d4nilpzz.d2tech.recipe;

import dev.d4nilpzz.d2tech.registry._RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HydraulicPressRecipe implements Recipe<HydraulicPressRecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int energy;
    private final int time;

    public HydraulicPressRecipe(Ingredient ingredient, ItemStack result, int energy, int time) {
        this.ingredient = ingredient;
        this.result = result;
        this.energy = energy;
        this.time = time;
    }

    @Override
    public boolean matches(@NotNull HydraulicPressRecipeInput input, @NotNull Level level) {
        return ingredient.test(input.input());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull HydraulicPressRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return _RecipeTypes.HYDRAULIC_PRESS_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return _RecipeTypes.HYDRAULIC_PRESS_TYPE.get();
    }

    public Ingredient ingredient() {
        return ingredient;
    }

    public int energy() {
        return energy;
    }

    public int time() {
        return time;
    }
}
