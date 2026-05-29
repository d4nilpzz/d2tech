package dev.d4nilpzz.d2tech.dataGenerators.builders;

import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class HydraulicPressRecipeBuilder {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int energy;
    private final int time;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public HydraulicPressRecipeBuilder(Ingredient ingredient, ItemStack result, int energy, int time) {
        this.ingredient = ingredient;
        this.result = result;
        this.energy = energy;
        this.time = time;
    }

    public static HydraulicPressRecipeBuilder press(Ingredient ingredient, ItemStack result, int energy, int time) {
        return new HydraulicPressRecipeBuilder(ingredient, result, energy, time);
    }

    public HydraulicPressRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public void save(@NotNull RecipeOutput output) {
        save(output, ResourceLocation.fromNamespaceAndPath(MODID, "hydraulic_press/" + result.getItem().builtInRegistryHolder().key().location().getPath()));
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
        output.accept(id, new HydraulicPressRecipe(ingredient, result, energy, time), advancement.build(id.withPrefix("recipes/")));
    }
}
