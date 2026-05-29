package dev.d4nilpzz.d2tech.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class _Tags {
    public static class Blocks {
        public static final TagKey<Block> CONNECTABLE_CABLE = createTag("connectable_cable");
        public static final TagKey<Block> CONNECTABLE_DATA_CABLE = createTag("connectable_data_cable");
        public static final TagKey<Block> CONFIGURABLE = createTag("configurable_pipe");
        public static final TagKey<Block> CHARGEABLE = createTag("chargeable");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, name));
        }
    }
}
