package dev.d4nilpzz.d2tech.dataGenerators.builders;

import dev.d4nilpzz.d2tech.recipe.AdvancedCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class AdvancedCraftingRecipeBuilder {
    private final ItemStack recipeMemory;
    private final List<String> pattern;
    private final Map<Character, ItemStack> key;
    private final ItemStack result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public AdvancedCraftingRecipeBuilder(ItemStack recipeMemory, List<String> pattern, Map<Character, ItemStack> key, ItemStack result) {
        this.recipeMemory = recipeMemory;
        this.pattern = pattern;
        this.key = key;
        this.result = result;
    }

    public static AdvancedCraftingRecipeBuilder advancedCrafting(ItemStack recipeMemory, List<String> pattern, Map<Character, ItemStack> key, ItemStack result) {
        return new AdvancedCraftingRecipeBuilder(recipeMemory, pattern, key, result);
    }

    public AdvancedCraftingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public void save(@NotNull RecipeOutput output) {
        save(output, ResourceLocation.fromNamespaceAndPath(MODID, "advanced_crafting/" + result.getItem().builtInRegistryHolder().key().location().getPath()));
    }

    public void save(@NotNull RecipeOutput output, String path) {
        save(output, ResourceLocation.fromNamespaceAndPath(MODID, path));
    }

    public void save(@NotNull RecipeOutput output, ResourceLocation id) {
        Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        output.accept(id, new AdvancedCraftingRecipe(recipeMemory, pattern, key, result), advancement.build(id.withPrefix("recipes/")));
    }
}
