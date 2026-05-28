package dev.d4nilpzz.d2tech.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyUtils {
    public static void distributeNearly(BlockEntity blockEntity, ModEnergyStorage outputEnergyStorage, int transfer) {
        Level level = blockEntity.getLevel();
        if (level == null || level.isClientSide) return;
        
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = blockEntity.getBlockPos().relative(direction);

            IEnergyStorage energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, blockPos, direction);
            if (energyStorage != null) {
                int amount = energyStorage.receiveEnergy(transfer, false);
                
                if (amount > 0) {
                    outputEnergyStorage.extractEnergy(amount, false);
                    break;
                }
            }
        }
    }
}
