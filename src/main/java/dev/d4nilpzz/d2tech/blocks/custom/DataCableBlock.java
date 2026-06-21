package dev.d4nilpzz.d2tech.blocks.custom;

import com.mojang.serialization.MapCodec;
import dev.d4nilpzz.d2tech.blocks.base.BaseCableBlock;
import dev.d4nilpzz.d2tech.registry._Tags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataCableBlock extends BaseCableBlock {

    public static final MapCodec<DataCableBlock> CODEC = simpleCodec(properties ->
            new DataCableBlock(properties, _Tags.Blocks.CONNECTABLE_DATA_CABLE));

    public DataCableBlock(Properties properties, TagKey<Block> connectableBlocks) {
        super(properties.noOcclusion().dynamicShape(), connectableBlocks);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return null;
    }
}
