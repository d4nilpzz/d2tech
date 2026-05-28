package dev.d4nilpzz.d2tech.energy;

import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;

public abstract class ModEnergyStorage extends EnergyStorage {
    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if(extractedEnergy != 0) {
            onEnergyChanged();
        }

        return extractedEnergy;
    }
    
    public int internalExtract(int toExtract, boolean simulate) {
        if (toExtract > 0) {
            int energyExtracted = Math.min(this.energy, toExtract);
            if (!simulate) {
                this.energy -= energyExtracted;
            }

            onEnergyChanged();
            return energyExtracted;
        } else {
            return 0;
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if(receiveEnergy != 0) {
            onEnergyChanged();
        }

        return receiveEnergy;
    }
    
    public int internalReceive(int toReceive, boolean simulate) {
        if (toReceive > 0) {
            int energyReceived = Mth.clamp(this.capacity - this.energy, 0, toReceive);
            if (!simulate) {
                this.energy += energyReceived;
            }

            onEnergyChanged();
            return energyReceived;
        } else {
            return 0;
        }
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }

    public abstract void onEnergyChanged();
}
