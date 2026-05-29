package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.blocks.custom.InterceptorAntennaController;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class AntennaControllerBlockEntity extends BlockEntity {

    private int tickCounter = 0;
    private int nextSignalTick = 0;

    public AntennaControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(_BlockEntities.ANTENNA_CONTROLLER_BE.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % 20 != 0) return;

        int antennaLevel = InterceptorAntennaController.checkStructureLevel(level, pos);

        if (antennaLevel == 0) {
            tickCounter = 0;
            nextSignalTick = 0;
            return;
        }

        if (nextSignalTick == 0) {
            nextSignalTick = 600 + level.random.nextInt(300);
            System.out.println("[AC] Init timer=" + nextSignalTick + "s lv=" + antennaLevel + " at " + pos);
        }

        tickCounter++;

        if (tickCounter >= nextSignalTick || tickCounter % 10 == 0) {
            System.out.println("[AC] tick=" + tickCounter + "/" + nextSignalTick + " lv=" + antennaLevel + " at " + pos);
        }

        if (tickCounter >= nextSignalTick) {
            tickCounter = 0;
            nextSignalTick = 600 + level.random.nextInt(300);
            System.out.println("[AC] >>> TRANSMIT level=" + antennaLevel);
            transmitLevel(level, pos, antennaLevel);
        }
    }

    private void transmitLevel(Level level, BlockPos startPos, int antennaLevel) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            for (Direction dir : Direction.values()) {
                BlockPos next = current.relative(dir);
                if (visited.contains(next)) continue;
                visited.add(next);

                BlockState state = level.getBlockState(next);

                if (state.is(_Blocks.DECODE_COMPUTER.get())) {
                    if (level.getBlockEntity(next) instanceof DecodeComputerBlockEntity dc) {
                        dc.receiveAntennaLevel(antennaLevel);
                        return;
                    }
                }

                if (state.is(_Blocks.DATA_CABLE.get())) {
                    queue.add(next);
                }
            }

            if (visited.size() > 512) break;
        }
    }
}
