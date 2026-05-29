package dev.d4nilpzz.d2tech.blocks.custom;

import dev.d4nilpzz.d2tech.registry._Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class InterceptorAntennaController {

    public static int checkStructureLevel(Level level, BlockPos pos) {

        BlockPos[] level1 = new BlockPos[]{

                // capa inferior
                pos.offset(1, 0, 1),
                pos.offset(0, 0, 1),
                pos.offset(-1, 0, 1),

                pos.offset(1, 0, -1),
                pos.offset(0, 0, -1),
                pos.offset(-1, 0, -1),

                pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
        };

        BlockPos[] level2 = new BlockPos[]{

                // capa inferior
                pos.offset(1, -1, 1),

                pos.offset(1, 0, 1),
                pos.offset(0, 0, 1),
                pos.offset(-1, 0, 1),

                pos.offset(1, 0, -1),
                pos.offset(0, 0, -1),
                pos.offset(-1, 0, -1),

                pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
        };

        BlockPos[] level3 = new BlockPos[]{

                // capa inferior
                pos.offset(1, -1, 1),
                pos.offset(1, 2, 1),

                pos.offset(1, 0, 1),
                pos.offset(0, 0, 1),
                pos.offset(-1, 0, 1),

                pos.offset(1, 0, -1),
                pos.offset(0, 0, -1),
                pos.offset(-1, 0, -1),

                pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
        };

        for (BlockPos checkPos : level1) {
            if (!level.getBlockState(checkPos).is(_Blocks.ANTENNA_BLOCK.get())) {
                return 0;
            }
        }

        for (BlockPos checkPos : level2) {
            if (!level.getBlockState(checkPos).is(_Blocks.ANTENNA_BLOCK.get())) {
                return 1;
            }
        }

        for (BlockPos checkPos : level3) {
            if (!level.getBlockState(checkPos).is(_Blocks.ANTENNA_BLOCK.get())) {
                return 2;
            }
        }

        return 3;
    }
}
