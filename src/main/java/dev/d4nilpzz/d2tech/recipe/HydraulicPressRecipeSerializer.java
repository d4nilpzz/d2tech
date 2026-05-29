package dev.d4nilpzz.d2tech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HydraulicPressRecipeSerializer implements RecipeSerializer<HydraulicPressRecipe> {

    private static final MapCodec<HydraulicPressRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HydraulicPressRecipe::ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.getResultItem(null)),
                    Codec.INT.fieldOf("energy").forGetter(HydraulicPressRecipe::energy),
                    Codec.INT.fieldOf("time").forGetter(HydraulicPressRecipe::time)
            ).apply(instance, HydraulicPressRecipe::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, HydraulicPressRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        buf.writeVarInt(recipe.ingredient().getItems().length);
                        for (ItemStack stack : recipe.ingredient().getItems()) {
                            ItemStack.STREAM_CODEC.encode(buf, stack);
                        }
                        ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(null));
                        buf.writeInt(recipe.energy());
                        buf.writeInt(recipe.time());
                    },
                    buf -> {
                        int len = buf.readVarInt();
                        ItemStack[] stacks = new ItemStack[len];
                        for (int i = 0; i < len; i++) {
                            stacks[i] = ItemStack.STREAM_CODEC.decode(buf);
                        }
                        Ingredient ingredient = Ingredient.of(stacks);
                        ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                        int energy = buf.readInt();
                        int time = buf.readInt();
                        return new HydraulicPressRecipe(ingredient, result, energy, time);
                    }
            );

    @Override
    public MapCodec<HydraulicPressRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, HydraulicPressRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
