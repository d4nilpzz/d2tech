package dev.d4nilpzz.d2tech.blocks.custom;

import com.mojang.serialization.MapCodec;
import dev.d4nilpzz.d2tech.blocks.base.BaseCableBlock;
import dev.d4nilpzz.d2tech.blocks.blockentity.CableBlockEntity;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._Tags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CableBlock extends BaseCableBlock {

    public static final MapCodec<CableBlock> CODEC = simpleCodec(properties ->
            new CableBlock(properties, _Tags.Blocks.CONNECTABLE_CABLE));

    public CableBlock(Properties properties, TagKey<Block> connectableBlocks) {
        super(properties.noOcclusion().dynamicShape(), connectableBlocks);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CableBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;
        return createTickerHelper(blockEntityType, _BlockEntities.CABLE_BE.get(),
                (level1, blockPos, blockState, cableBlockEntity) ->
                        cableBlockEntity.tick(level1, blockPos, blockState));
    }
}
