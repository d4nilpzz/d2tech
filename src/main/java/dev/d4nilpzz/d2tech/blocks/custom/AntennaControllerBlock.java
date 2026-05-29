package dev.d4nilpzz.d2tech.blocks.custom;

import com.mojang.serialization.MapCodec;
import dev.d4nilpzz.d2tech.blocks.blockentity.AntennaControllerBlockEntity;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AntennaControllerBlock extends BaseEntityBlock {

    public static final MapCodec<AntennaControllerBlock> CODEC = simpleCodec(AntennaControllerBlock::new);

    public AntennaControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AntennaControllerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;

        return createTickerHelper(blockEntityType, _BlockEntities.ANTENNA_CONTROLLER_BE.get(),
                ((level1, blockPos, blockState, antennaControllerBlockEntity) ->
                        antennaControllerBlockEntity.tick(level1, blockPos, blockState)));
    }
}
