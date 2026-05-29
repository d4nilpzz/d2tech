package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.blocks.base.BaseGeneratorBlockEntity;
import dev.d4nilpzz.d2tech.energy.EnergyUtils;
import dev.d4nilpzz.d2tech.item.custom.BatteryItem;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.screen.custom.CoalGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CoalGeneratorBlockEntity extends BaseGeneratorBlockEntity implements MenuProvider {
    private static final int BATTERY_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    
    private final ContainerData containerData;
    private int maxBurnTime;
    private int burnTime;

    @Override
    protected boolean isItemValidForSlot(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case BATTERY_SLOT -> stack.getItem() instanceof BatteryItem;
            case FUEL_SLOT -> FurnaceBlockEntity.isFuel(stack);
            default -> true;
        };
    }

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(_BlockEntities.COAL_GENERATOR_BE.get(), pos, state, 20000, 0, 200, 2, "coal_generator");
        
        this.containerData = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> CoalGeneratorBlockEntity.this.burnTime;
                    case 1 -> CoalGeneratorBlockEntity.this.maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> CoalGeneratorBlockEntity.this.burnTime = i1;
                    case 1 -> CoalGeneratorBlockEntity.this.maxBurnTime = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.d2tech.coal_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new CoalGeneratorMenu(id, inventory, this, containerData);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        maxBurnTime = tag.getInt(sid + ".burn_max_time");
        burnTime = tag.getInt(sid + ".burn_time");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(sid + ".burn_max_time", maxBurnTime);
        tag.putInt(sid + ".burn_time", burnTime);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        if (burnTime > 0) {
            ENERGY_STORAGE.internalReceive(50, false);
            burnTime--;
        } else {
            ItemStack stack = inventory.getStackInSlot(FUEL_SLOT);
            if (FurnaceBlockEntity.isFuel(stack)) {
                burnTime = stack.getBurnTime(null);
                maxBurnTime = burnTime;
                stack.shrink(1);
            }
        }

        chargeBattery();

        EnergyUtils.distributeNearly(this, ENERGY_STORAGE, 100);
    }

    private void chargeBattery() {
        ItemStack batteryStack = inventory.getStackInSlot(BATTERY_SLOT);
        if (batteryStack.isEmpty()) return;

        IEnergyStorage batteryCap = Capabilities.EnergyStorage.ITEM.getCapability(batteryStack, null);
        if (batteryCap == null) return;

        int canExtract = Math.min(ENERGY_STORAGE.getEnergyStored(), BatteryItem.MAX_TRANSFER);
        int received = batteryCap.receiveEnergy(canExtract, true);
        if (received > 0) {
            ENERGY_STORAGE.internalExtract(received, false);
            batteryCap.receiveEnergy(received, false);
        }
    }
}