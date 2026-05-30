package dev.d4nilpzz.d2tech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedCraftingRecipeSerializer implements RecipeSerializer<AdvancedCraftingRecipe> {

    private static final MapCodec<AdvancedCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("recipe_memory").forGetter(AdvancedCraftingRecipe::recipeMemory),
                    Codec.STRING.listOf().fieldOf("pattern").forGetter(AdvancedCraftingRecipe::pattern),
                    Codec.unboundedMap(Codec.STRING, ItemStack.CODEC).fieldOf("key").forGetter(r -> {
                        Map<String, ItemStack> stringMap = new HashMap<>();
                        r.key().forEach((c, stack) -> stringMap.put(String.valueOf(c), stack));
                        return stringMap;
                    }),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.getResultItem(null))
            ).apply(instance, (memory, pattern, keyMap, result) -> {
                Map<Character, ItemStack> charMap = new HashMap<>();
                keyMap.forEach((s, stack) -> charMap.put(s.charAt(0), stack));
                return new AdvancedCraftingRecipe(memory, pattern, charMap, result);
            })
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, AdvancedCraftingRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        ItemStack.STREAM_CODEC.encode(buf, recipe.recipeMemory());
                        buf.writeVarInt(recipe.pattern().size());
                        for (String s : recipe.pattern()) {
                            buf.writeUtf(s);
                        }
                        buf.writeVarInt(recipe.key().size());
                        recipe.key().forEach((c, stack) -> {
                            buf.writeChar(c);
                            ItemStack.STREAM_CODEC.encode(buf, stack);
                        });
                        ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(null));
                    },
                    buf -> {
                        ItemStack memory = ItemStack.STREAM_CODEC.decode(buf);
                        int patternSize = buf.readVarInt();
                        List<String> pattern = buf.readUtf().lines().toList(); // workaround
                        String[] patternArr = new String[patternSize];
                        for (int i = 0; i < patternSize; i++) {
                            patternArr[i] = buf.readUtf();
                        }
                        int keySize = buf.readVarInt();
                        Map<Character, ItemStack> key = new HashMap<>();
                        for (int i = 0; i < keySize; i++) {
                            char c = buf.readChar();
                            ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
                            key.put(c, stack);
                        }
                        ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                        return new AdvancedCraftingRecipe(memory, Arrays.asList(patternArr), key, result);
                    }
            );

    @Override
    public MapCodec<AdvancedCraftingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AdvancedCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
