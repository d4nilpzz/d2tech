package dev.d4nilpzz.d2tech.dataGenerators.providers;

import dev.d4nilpzz.d2tech.dataGenerators.builders.AdvancedCraftingRecipeBuilder;
import dev.d4nilpzz.d2tech.dataGenerators.builders.HydraulicPressRecipeBuilder;
import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.registry._Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        List<ItemLike> STEEL_SMELTABLES = List.of(_Items.RAW_STEEL,
                _Blocks.STEEL_ORE, _Blocks.STEEL_DEEPSLATE_ORE);

        List<ItemLike> ALUMINUM_SMELTABLES = List.of(_Items.RAW_ALUMINUM,
                _Blocks.ALUMINUM_ORE, _Blocks.ALUMINUM_DEEPSLATE_ORE);

        List<ItemLike> PLASTIC_SMELTABLES = List.of(Items.SLIME_BALL);

        // Steel smelting
        oreSmelting(recipeOutput, STEEL_SMELTABLES, RecipeCategory.MISC, _Items.STEEL.get(), 0.25f, 200, "steel");
        oreBlasting(recipeOutput, STEEL_SMELTABLES, RecipeCategory.MISC, _Items.STEEL.get(), 0.25f, 100, "steel");

        // Aluminum smelting
        oreSmelting(recipeOutput, ALUMINUM_SMELTABLES, RecipeCategory.MISC, _Items.ALUMINUM.get(), 0.25f, 200, "aluminum");
        oreBlasting(recipeOutput, ALUMINUM_SMELTABLES, RecipeCategory.MISC, _Items.ALUMINUM.get(), 0.25f, 100, "aluminum");

        // Plastic
        oreBlasting(recipeOutput, PLASTIC_SMELTABLES, RecipeCategory.MISC, _Items.PLASTIC_PELLET.get(), 0.15f, 250, "plastic");

        // Structure Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.STRUCTURE_BLOCK.get())
                .pattern("BAB")
                .pattern("A A")
                .pattern("BAB")
                .define('A', _Items.STEEL.get())
                .define('B', _Items.ALUMINUM.get())
                .unlockedBy("has_steel", has(_Items.STEEL)).save(recipeOutput);

        // Chip
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Items.CHIP.get())
                .pattern(" C ")
                .pattern("CAC")
                .pattern(" C ")
                .define('A', _Items.PLASTIC_SHEET.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_plastic_sheet", has(_Items.PLASTIC_SHEET)).save(recipeOutput);

        // Advanced Crafting Table recipes
        AdvancedCraftingRecipeBuilder.advancedCrafting(
                        new ItemStack(_Items.SATELLITE_ADVANCED_SPACE_CHIP_MEMORY.get()),
                        List.of("CGC", "GSG", "CGC"),
                        Map.of('C', new ItemStack(_Items.CHIP.get()),
                                'G', new ItemStack(Items.GOLD_INGOT),
                                'S', new ItemStack(_Items.PLASTIC_SHEET.get())),
                        new ItemStack(_Items.ADVANCED_SPACE_SHIP.get())
                )
                .unlockedBy("has_chip", has(_Items.CHIP))
                .save(recipeOutput);

        // Battery
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Items.BATTERY.get())
                .pattern(" S ")
                .pattern("RCR")
                .pattern(" S ")
                .define('S', _Items.STEEL.get())
                .define('R', Items.REDSTONE)
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_steel", has(_Items.STEEL)).save(recipeOutput);

        // Machines
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.SOLAR_GENERATOR.get())
                .pattern("PPP")
                .pattern("CBG")
                .pattern("SSS")
                .define('P', Items.GLASS_PANE)
                .define('C', _Items.CHIP.get())
                .define('B', _Blocks.STRUCTURE_BLOCK.get())
                .define('S', _Items.STEEL.get())
                .define('G', _Items.BATTERY.get())
                .unlockedBy("has_chip", has(_Items.CHIP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.COAL_GENERATOR.get())
                .pattern("AFA")
                .pattern("ABG")
                .pattern("SSS")
                .define('S', _Items.STEEL.get())
                .define('F', Items.FURNACE)
                .define('A', _Items.CHIP.get())
                .define('B', _Blocks.STRUCTURE_BLOCK.get())
                .define('G', _Items.BATTERY.get())
                .unlockedBy("has_chip", has(_Items.CHIP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.HEAT_GENERATOR.get())
                .pattern("SBS")
                .pattern("SFS")
                .pattern("SBS")
                .define('S', _Items.STEEL.get())
                .define('B', Items.BLAST_FURNACE)
                .define('F', Items.FURNACE)
                .unlockedBy("has_steel", has(_Items.STEEL)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.DECODE_COMPUTER.get())
                .pattern("APP")
                .pattern("CBC")
                .pattern("SSS")
                .define('P', Items.GLASS_PANE)
                .define('C', _Items.CHIP.get())
                .define('A', _Items.ALUMINUM.get())
                .define('B', _Blocks.STRUCTURE_BLOCK.get())
                .define('S', _Items.STEEL.get())
                .unlockedBy("has_chip", has(_Items.CHIP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.ANTENNA_BLOCK.get())
                .pattern("BB")
                .pattern("BB")
                .define('B', _Items.ALUMINUM.get())
                .unlockedBy("has_aluminum", has(_Items.ALUMINUM)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.ANTENNA_CONTROLLER.get())
                .pattern("AAA")
                .pattern("BCB")
                .pattern("AAA")
                .define('A', _Items.ALUMINUM.get())
                .define('B', _Blocks.ANTENNA_BLOCK.get())
                .define('C', _Items.CHIP.get())
                .unlockedBy("has_antenna_block", has(_Blocks.ANTENNA_BLOCK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Items.CONFIGURATOR.get())
                .pattern("C")
                .pattern("B")
                .pattern("A")
                .define('A', _Items.ALUMINUM.get())
                .define('B', _Items.BATTERY.get())
                .define('C', _Items.STEEL.get())
                .unlockedBy("has_steel", has(_Items.STEEL)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.ADVANCED_CRAFTING_TABLE.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("SGS")
                .define('A', _Items.ALUMINUM.get())
                .define('B', _Items.CHIP.get())
                .define('C', Items.REDSTONE)
                .define('D', _Blocks.DECODE_COMPUTER.get())
                .define('S', _Items.STEEL.get())
                .define('G', _Items.BATTERY.get())
                .unlockedBy("has_decode_computer", has(_Blocks.DECODE_COMPUTER)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.HYDRAULIC_PRESS.get())
                .pattern("CAC")
                .pattern("CBG")
                .pattern("SSS")
                .define('A', Items.ANVIL)
                .define('C', _Items.ALUMINUM.get())
                .define('B', _Blocks.STRUCTURE_BLOCK.get())
                .define('S', _Items.STEEL.get())
                .define('G', _Items.BATTERY.get())
                .unlockedBy("has_steel", has(_Items.STEEL)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.CABLE.get(), 4)
                .pattern("SRS")
                .define('S', _Items.STEEL.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_steel", has(_Items.STEEL)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Blocks.DATA_CABLE.get(), 4)
                .pattern(" S ")
                .pattern("SRS")
                .pattern(" S ")
                .define('S', _Blocks.CABLE.get())
                .define('R', Items.COPPER_INGOT)
                .unlockedBy("has_cable", has(_Items.STEEL)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, _Items.RECIPE_MEMORY.get())
                .pattern(" S ")
                .pattern("CPC")
                .pattern(" S ")
                .define('C', _Items.CHIP.get())
                .define('P', _Items.PLASTIC_SHEET.get())
                .define('S', _Items.STEEL.get())
                .unlockedBy("has_chip", has(_Items.CHIP)).save(recipeOutput);

        // Hydraulic Press recipes
        HydraulicPressRecipeBuilder.press(
                        Ingredient.of(_Items.PLASTIC_PELLET.get()),
                        new ItemStack(_Items.PLASTIC_SHEET.get()),
                        1000, 100
                )
                .unlockedBy("has_plastic_pellet", has(_Items.PLASTIC_PELLET))
                .save(recipeOutput);
    }

    protected static void oreSmelting(@NotNull RecipeOutput recipeOutput, List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult,
                                      float pExperience, int pCookingTIme, @NotNull String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(@NotNull RecipeOutput recipeOutput, List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult,
                                      float pExperience, int pCookingTime, @NotNull String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(@NotNull RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.@NotNull Factory<T> factory,
                                                                       List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult, float pExperience, int pCookingTime, @NotNull String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput,MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
