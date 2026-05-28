package dev.d4nilpzz.d2tech.blocks;

import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeUtils {
    public static VoxelShape rotateShapeY(VoxelShape shape, int degrees) {
        VoxelShape[] buffer = new VoxelShape[]{shape, net.minecraft.world.phys.shapes.Shapes.empty()};
        int times = (degrees / 90) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = net.minecraft.world.phys.shapes.Shapes.or(buffer[1],
                            net.minecraft.world.phys.shapes.Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = net.minecraft.world.phys.shapes.Shapes.empty();
        }
        return buffer[0];
    }
}
