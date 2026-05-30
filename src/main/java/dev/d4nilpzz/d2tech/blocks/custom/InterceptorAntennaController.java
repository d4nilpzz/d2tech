package dev.d4nilpzz.d2tech.blocks.custom;

import dev.d4nilpzz.d2tech.registry._Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class InterceptorAntennaController {

    public static int checkStructureLevel(Level level, BlockPos pos) {

        BlockPos[] level1 = new BlockPos[]{
                // === Y=0 (cruz interior) ===
                // Anillo interior
                pos.offset( 1, 0,  1),
                pos.offset( 0, 0,  1),
                pos.offset(-1, 0,  1),
                pos.offset( 1, 0, -1),
                pos.offset( 0, 0, -1),
                pos.offset(-1, 0, -1),
                pos.offset( 1, 0,  0),
                pos.offset(-1, 0,  0),

                // Fila z=-2
                pos.offset(-1, 0, -2),
                pos.offset( 0, 0, -2),
                pos.offset( 1, 0, -2),

                // Fila z=+2
                pos.offset(-1, 0,  2),
                pos.offset( 0, 0,  2),
                pos.offset( 1, 0,  2),

                // Columna x=-2
                pos.offset(-2, 0, -1),
                pos.offset(-2, 0,  0),
                pos.offset(-2, 0,  1),

                // Columna x=+2
                pos.offset( 2, 0, -1),
                pos.offset( 2, 0,  0),
                pos.offset( 2, 0,  1),

                // === Y=1 (aro exterior sin esquinas) ===
                // Fila z=-3
                pos.offset(-1, 1, -3),
                pos.offset( 0, 1, -3),
                pos.offset( 1, 1, -3),

                // Fila z=+3
                pos.offset(-1, 1,  3),
                pos.offset( 0, 1,  3),
                pos.offset( 1, 1,  3),

                // Columna x=-3
                pos.offset(-3, 1, -1),
                pos.offset(-3, 1,  0),
                pos.offset(-3, 1,  1),

                // Columna x=+3
                pos.offset( 3, 1, -1),
                pos.offset( 3, 1,  0),
                pos.offset( 3, 1,  1),

                // Esquinas a distancia 2 en y=1
                pos.offset(-2, 1, -2),
                pos.offset( 2, 1, -2),
                pos.offset(-2, 1,  2),
                pos.offset( 2, 1,  2),
        };

        BlockPos[] level2 = new BlockPos[]{

                // === Y=0 (cruz interior) ===
                pos.offset( 1, 0,  1),
                pos.offset( 0, 0,  1),
                pos.offset(-1, 0,  1),
                pos.offset( 1, 0, -1),
                pos.offset( 0, 0, -1),
                pos.offset(-1, 0, -1),
                pos.offset( 1, 0,  0),
                pos.offset(-1, 0,  0),

                pos.offset(-1, 0, -2),
                pos.offset( 0, 0, -2),
                pos.offset( 1, 0, -2),

                pos.offset(-1, 0,  2),
                pos.offset( 0, 0,  2),
                pos.offset( 1, 0,  2),

                pos.offset(-2, 0, -1),
                pos.offset(-2, 0,  0),
                pos.offset(-2, 0,  1),

                pos.offset( 2, 0, -1),
                pos.offset( 2, 0,  0),
                pos.offset( 2, 0,  1),

                // === Y=1 (aro medio) ===
                // Fila z=-3
                pos.offset(-2, 1, -3),
                pos.offset(-1, 1, -3),
                pos.offset( 0, 1, -3),
                pos.offset( 1, 1, -3),
                pos.offset( 2, 1, -3),

                // Fila z=+3
                pos.offset(-2, 1,  3),
                pos.offset(-1, 1,  3),
                pos.offset( 0, 1,  3),
                pos.offset( 1, 1,  3),
                pos.offset( 2, 1,  3),

                // Columna x=-3
                pos.offset(-3, 1, -2),
                pos.offset(-3, 1, -1),
                pos.offset(-3, 1,  0),
                pos.offset(-3, 1,  1),
                pos.offset(-3, 1,  2),

                // Columna x=+3
                pos.offset( 3, 1, -2),
                pos.offset( 3, 1, -1),
                pos.offset( 3, 1,  0),
                pos.offset( 3, 1,  1),
                pos.offset( 3, 1,  2),

                // Esquinas y=1
                pos.offset(-2, 1, -2),
                pos.offset( 2, 1, -2),
                pos.offset(-2, 1,  2),
                pos.offset( 2, 1,  2),

                // === Y=2 (aro exterior sin esquinas) ===
                // Fila z=-4
                pos.offset(-2, 2, -4),
                pos.offset(-1, 2, -4),
                pos.offset( 0, 2, -4),
                pos.offset( 1, 2, -4),
                pos.offset( 2, 2, -4),

                // Fila z=+4
                pos.offset(-2, 2,  4),
                pos.offset(-1, 2,  4),
                pos.offset( 0, 2,  4),
                pos.offset( 1, 2,  4),
                pos.offset( 2, 2,  4),

                // Columna x=-4
                pos.offset(-4, 2, -2),
                pos.offset(-4, 2, -1),
                pos.offset(-4, 2,  0),
                pos.offset(-4, 2,  1),
                pos.offset(-4, 2,  2),

                // Columna x=+4
                pos.offset( 4, 2, -2),
                pos.offset( 4, 2, -1),
                pos.offset( 4, 2,  0),
                pos.offset( 4, 2,  1),
                pos.offset( 4, 2,  2),

                // Esquinas y=2
                pos.offset(-3, 2, -3),
                pos.offset( 3, 2, -3),
                pos.offset(-3, 2,  3),
                pos.offset( 3, 2,  3),
        };

        BlockPos[] level3 = new BlockPos[]{

                // === Y=0 (cruz interior) ===
                pos.offset( 1, 0,  1),
                pos.offset( 0, 0,  1),
                pos.offset(-1, 0,  1),
                pos.offset( 1, 0, -1),
                pos.offset( 0, 0, -1),
                pos.offset(-1, 0, -1),
                pos.offset( 1, 0,  0),
                pos.offset(-1, 0,  0),

                pos.offset(-1, 0, -2),
                pos.offset( 0, 0, -2),
                pos.offset( 1, 0, -2),

                pos.offset(-1, 0,  2),
                pos.offset( 0, 0,  2),
                pos.offset( 1, 0,  2),

                pos.offset(-2, 0, -1),
                pos.offset(-2, 0,  0),
                pos.offset(-2, 0,  1),

                pos.offset( 2, 0, -1),
                pos.offset( 2, 0,  0),
                pos.offset( 2, 0,  1),

                // === Y=1 (aro medio) ===
                // Fila z=-3
                pos.offset(-2, 1, -3),
                pos.offset(-1, 1, -3),
                pos.offset( 0, 1, -3),
                pos.offset( 1, 1, -3),
                pos.offset( 2, 1, -3),

                // Fila z=+3
                pos.offset(-2, 1,  3),
                pos.offset(-1, 1,  3),
                pos.offset( 0, 1,  3),
                pos.offset( 1, 1,  3),
                pos.offset( 2, 1,  3),

                // Columna x=-3
                pos.offset(-3, 1, -2),
                pos.offset(-3, 1, -1),
                pos.offset(-3, 1,  0),
                pos.offset(-3, 1,  1),
                pos.offset(-3, 1,  2),

                // Columna x=+3
                pos.offset( 3, 1, -2),
                pos.offset( 3, 1, -1),
                pos.offset( 3, 1,  0),
                pos.offset( 3, 1,  1),
                pos.offset( 3, 1,  2),

                // Esquinas y=1
                pos.offset(-2, 1, -2),
                pos.offset( 2, 1, -2),
                pos.offset(-2, 1,  2),
                pos.offset( 2, 1,  2),

                // === Y=2 (aro exterior sin esquinas) ===
                // Fila z=-4
                pos.offset(-2, 2, -4),
                pos.offset(-1, 2, -4),
                pos.offset( 0, 2, -4),
                pos.offset( 1, 2, -4),
                pos.offset( 2, 2, -4),

                // Fila z=+4
                pos.offset(-2, 2,  4),
                pos.offset(-1, 2,  4),
                pos.offset( 0, 2,  4),
                pos.offset( 1, 2,  4),
                pos.offset( 2, 2,  4),

                // Columna x=-4
                pos.offset(-4, 2, -2),
                pos.offset(-4, 2, -1),
                pos.offset(-4, 2,  0),
                pos.offset(-4, 2,  1),
                pos.offset(-4, 2,  2),

                // Columna x=+4
                pos.offset( 4, 2, -2),
                pos.offset( 4, 2, -1),
                pos.offset( 4, 2,  0),
                pos.offset( 4, 2,  1),
                pos.offset( 4, 2,  2),

                // Esquinas y=2
                pos.offset(-3, 2, -3),
                pos.offset( 3, 2, -3),
                pos.offset(-3, 2,  3),
                pos.offset( 3, 2,  3),
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
