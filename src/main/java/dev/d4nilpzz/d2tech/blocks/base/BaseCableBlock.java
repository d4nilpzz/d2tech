package dev.d4nilpzz.d2tech.blocks.base;

import dev.d4nilpzz.d2tech.registry._Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

public class BaseCableBlock extends Block {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");
    public static final BooleanProperty UP    = BooleanProperty.create("up");
    public static final BooleanProperty DOWN  = BooleanProperty.create("down");
    public static final BooleanProperty STRAIGHT = BooleanProperty.create("straight");
    public static final BooleanProperty MANUAL = BooleanProperty.create("manual");


    private static final VoxelShape CORE_6 =
            Block.box(5, 5, 5, 11, 11, 11);

    private static final VoxelShape NORTH_SHAPE =
            Block.box(5, 5, 0, 11, 11, 5);

    private static final VoxelShape SOUTH_SHAPE =
            Block.box(5, 5, 11, 11, 11, 16);

    private static final VoxelShape EAST_SHAPE =
            Block.box(11, 5, 5, 16, 11, 11);

    private static final VoxelShape WEST_SHAPE =
            Block.box(0, 5, 5, 5, 11, 11);

    private static final VoxelShape UP_SHAPE =
            Block.box(5, 11, 5, 11, 16, 11);

    private static final VoxelShape DOWN_SHAPE =
            Block.box(5, 0, 5, 11, 5, 11);

    private final TagKey<Block> connectableBlocks;


    /*
    * Waterlogear el cable para que pueda estar dentro de lava y agua.
    */
    public BaseCableBlock(BlockBehaviour.Properties properties, TagKey<Block> tagKey) {
        super(properties.forceSolidOn());
        this.connectableBlocks = tagKey;

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(STRAIGHT, false)
                .setValue(MANUAL, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, STRAIGHT, MANUAL);
    }

    private boolean canConnect(BlockGetter level, BlockPos pos, Direction dir) {
        BlockPos blockToConnect = pos.relative(dir);
        BlockState other = level.getBlockState(blockToConnect);
        return other.is(connectableBlocks) || ((Level) level).getCapability(Capabilities.EnergyStorage.BLOCK, blockToConnect, dir) != null;
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
        return updateConnections(ctx.getLevel(), ctx.getClickedPos(), defaultBlockState());
    }

    @Override
    protected @NotNull BlockState updateShape(
            BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos
    ) {
        if (state.getValue(MANUAL)) {
            return state;
        }

        if (level instanceof Level lvl) {
            return updateConnections(lvl, pos, state);
        }
        return state;
    }


    private BlockState updateConnections(Level level, BlockPos pos, BlockState state) {
        boolean n = canConnect(level, pos, Direction.NORTH);
        boolean s = canConnect(level, pos, Direction.SOUTH);
        boolean e = canConnect(level, pos, Direction.EAST);
        boolean w = canConnect(level, pos, Direction.WEST);
        boolean u = canConnect(level, pos, Direction.UP);
        boolean d = canConnect(level, pos, Direction.DOWN);

        int connections =
                (n ? 1 : 0) +
                        (s ? 1 : 0) +
                        (e ? 1 : 0) +
                        (w ? 1 : 0) +
                        (u ? 1 : 0) +
                        (d ? 1 : 0);

        boolean straight =
                connections == 2 && (
                        (n && s) ||
                                (e && w) ||
                                (u && d)
                );

        return state
                .setValue(NORTH, n)
                .setValue(SOUTH, s)
                .setValue(EAST, e)
                .setValue(WEST, w)
                .setValue(UP, u)
                .setValue(DOWN, d)
                .setValue(STRAIGHT, straight);

    }

    @Override
    public @NotNull VoxelShape getShape(
            BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        if (state.getValue(STRAIGHT)) {
            return Shapes.or(
                    CORE_6,
                    state.getValue(NORTH) ? NORTH_SHAPE : Shapes.empty(),
                    state.getValue(SOUTH) ? SOUTH_SHAPE : Shapes.empty(),
                    state.getValue(EAST)  ? EAST_SHAPE  : Shapes.empty(),
                    state.getValue(WEST)  ? WEST_SHAPE  : Shapes.empty(),
                    state.getValue(UP)    ? UP_SHAPE    : Shapes.empty(),
                    state.getValue(DOWN)  ? DOWN_SHAPE  : Shapes.empty()
            );
        }

        VoxelShape shape = CORE_6;

        if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(EAST))  shape = Shapes.or(shape, EAST_SHAPE);
        if (state.getValue(WEST))  shape = Shapes.or(shape, WEST_SHAPE);
        if (state.getValue(UP))    shape = Shapes.or(shape, UP_SHAPE);
        if (state.getValue(DOWN))  shape = Shapes.or(shape, DOWN_SHAPE);

        return shape;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack stack,
            BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit
    ) {
        if (state.getValue(STRAIGHT)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!stack.is(_Items.CONFIGURATOR.get())) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide) {

            Vec3 hitVec = hit.getLocation();
            double x = hitVec.x - pos.getX();
            double y = hitVec.y - pos.getY();
            double z = hitVec.z - pos.getZ();

            Direction dir = getTargetDirection(x, y, z);

            BooleanProperty prop = switch (dir) {
                case NORTH -> NORTH;
                case SOUTH -> SOUTH;
                case EAST  -> EAST;
                case WEST  -> WEST;
                case UP    -> UP;
                case DOWN  -> DOWN;
            };

            BlockState newState = state
                    .setValue(prop, !state.getValue(prop))
                    .setValue(MANUAL, true);

            level.setBlock(pos, newState, 2);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }


    private Direction getTargetDirection(double x, double y, double z) {

        double dx = Math.abs(x - 0.5);
        double dy = Math.abs(y - 0.5);
        double dz = Math.abs(z - 0.5);

        if (dx > dy && dx > dz) {
            return x > 0.5 ? Direction.EAST : Direction.WEST;
        } else if (dz > dx && dz > dy) {
            return z > 0.5 ? Direction.SOUTH : Direction.NORTH;
        } else {
            return y > 0.5 ? Direction.UP : Direction.DOWN;
        }
    }
}
