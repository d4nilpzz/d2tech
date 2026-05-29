package dev.d4nilpzz.d2tech.registry;

import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipe;
import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class _RecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<HydraulicPressRecipe>> HYDRAULIC_PRESS_TYPE =
            RECIPE_TYPES.register("hydraulic_press", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "hydraulic_press")));

    public static final DeferredHolder<RecipeSerializer<?>, HydraulicPressRecipeSerializer> HYDRAULIC_PRESS_SERIALIZER =
            RECIPE_SERIALIZERS.register("hydraulic_press", HydraulicPressRecipeSerializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
