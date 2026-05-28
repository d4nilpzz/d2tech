package dev.d4nilpzz.d2tech.blocks.custom;

import com.mojang.serialization.MapCodec;
import dev.d4nilpzz.d2tech.blocks.ShapeUtils;
import dev.d4nilpzz.d2tech.blocks.blockentity.DecodeComputerBlockEntity;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DecodeComputerBlock extends BaseEntityBlock {

    public static final MapCodec<DecodeComputerBlock> CODEC = simpleCodec(DecodeComputerBlock::new);

    public static final BooleanProperty GENERATING = BooleanProperty.create("generating");

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 6, 16);

    public DecodeComputerBlock(Properties properties) {
        super(properties.noOcclusion().dynamicShape());
        this.registerDefaultState(this.stateDefinition.any().setValue(GENERATING, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()) {
            if(level.getBlockEntity(pos) instanceof DecodeComputerBlockEntity decodeComputerBlockEntity) {
                decodeComputerBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction dir = state.getValue(FACING);

        return switch (dir) {
            case EAST -> ShapeUtils.rotateShapeY(SHAPE, 90);
            case SOUTH -> ShapeUtils.rotateShapeY(SHAPE, 180);
            case WEST -> ShapeUtils.rotateShapeY(SHAPE, 270);
            default -> SHAPE;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GENERATING).add(FACING);
    }

    @Override
    public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean moved) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 5);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        boolean shouldGenerate = level.isDay();

        if (state.getValue(GENERATING) != shouldGenerate) {
            level.setBlock(pos, state.setValue(GENERATING, shouldGenerate), 3);
        }


        level.scheduleTick(pos, this, 5);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DecodeComputerBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                                       @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof DecodeComputerBlockEntity decodeComputerBlockEntity) {
            if(!level.isClientSide()) {
                player.openMenu(new SimpleMenuProvider(decodeComputerBlockEntity, Component.literal("")), pos);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;

        return createTickerHelper(blockEntityType, _BlockEntities.DECODE_COMPUTER_BE.get(),
                ((level1, blockPos, blockState, decodeComputerBlockEntity) ->
                        decodeComputerBlockEntity.tick(level1, blockPos, blockState)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}