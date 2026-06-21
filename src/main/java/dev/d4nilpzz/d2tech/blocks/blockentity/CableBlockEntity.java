package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.energy.ModEnergyStorage;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CableBlockEntity extends BlockEntity {
    private static final int CAPACITY = 5000;
    private static final int TRANSFER = 1000;

    private final ModEnergyStorage energyStorage;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(_BlockEntities.CABLE_BE.get(), pos, state);
        this.energyStorage = new ModEnergyStorage(CAPACITY, TRANSFER, TRANSFER) {
            @Override
            public void onEnergyChanged() {
                setChanged();
            }
        };
    }

    public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
        return energyStorage;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;
        if (energyStorage.getEnergyStored() <= 0) return;

        int energyToSend = Math.min(energyStorage.getEnergyStored(), TRANSFER);

        for (Direction direction : Direction.values()) {
            if (energyToSend <= 0) break;

            BlockPos targetPos = pos.relative(direction);
            IEnergyStorage target = level.getCapability(Capabilities.EnergyStorage.BLOCK, targetPos, direction.getOpposite());
            if (target == null) continue;

            int sent = target.receiveEnergy(energyToSend, false);
            if (sent > 0) {
                energyStorage.extractEnergy(sent, false);
                energyToSend -= sent;
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
    }
}
