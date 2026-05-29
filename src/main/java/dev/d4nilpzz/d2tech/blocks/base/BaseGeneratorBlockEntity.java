package dev.d4nilpzz.d2tech.blocks.base;

import dev.d4nilpzz.d2tech.energy.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class BaseGeneratorBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory;
    protected final ModEnergyStorage ENERGY_STORAGE;
    protected String sid;

    public BaseGeneratorBlockEntity(BlockEntityType<?> type,
                                    BlockPos pos,
                                    BlockState state,
                                    int energyCapacity,
                                    int maxReceive,
                                    int maxExtract,
                                    int inventorySize,
                                    String sid) {
        super(type, pos, state);

        this.inventory = new ItemStackHandler(inventorySize) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return BaseGeneratorBlockEntity.this.isItemValidForSlot(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                sync();
            }
        };
        this.sid = sid;

        this.ENERGY_STORAGE = new ModEnergyStorage(energyCapacity, maxReceive, maxExtract) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                sync();
            }
        };
    }

    protected void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt(sid+".energy", ENERGY_STORAGE.getEnergyStored());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(tag.getInt(sid+".energy"));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
        return ENERGY_STORAGE;
    }

    public IItemHandler getItemHandler(@Nullable Direction direction) {
        return inventory;
    }

    protected boolean isItemValidForSlot(int slot, @NotNull ItemStack stack) {
        return true;
    }

    public abstract void tick(Level level, BlockPos pos, BlockState state);
}
