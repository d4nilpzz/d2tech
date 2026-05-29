package dev.d4nilpzz.d2tech.blocks.custom;

import dev.d4nilpzz.d2tech.blocks.base.BaseCableBlock;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DataCableBlock extends BaseCableBlock {

    public DataCableBlock(Properties properties, TagKey<Block> connectableBlocks) {
        super(properties.noOcclusion().dynamicShape(), connectableBlocks);
    }
}
