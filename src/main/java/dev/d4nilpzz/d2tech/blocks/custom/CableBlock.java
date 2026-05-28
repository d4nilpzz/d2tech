package dev.d4nilpzz.d2tech.blocks.custom;

import dev.d4nilpzz.d2tech.blocks.base.BaseCableBlock;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CableBlock extends BaseCableBlock {

    public CableBlock(Properties properties, TagKey<Block> connectableBlocks) {
        super(properties.noOcclusion().dynamicShape(), connectableBlocks);
    }
}
