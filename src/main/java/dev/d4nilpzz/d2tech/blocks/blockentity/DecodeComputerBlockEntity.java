package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.blocks.custom.DecodeComputerBlock;
import dev.d4nilpzz.d2tech.energy.ModEnergyStorage;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._Items;
import dev.d4nilpzz.d2tech.screen.custom.DecodeComputerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DecodeComputerBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private int receivedFrequency = -1;
    private int recipeIndex = -1;
    private int antennaLevel = 0;

    private final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(20000, 200, 0) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                assert getLevel() != null;
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    public DecodeComputerBlockEntity(BlockPos pos, BlockState blockState) {
        super(_BlockEntities.DECODE_COMPUTER_BE.get(), pos, blockState);
    }

    public void setFrequency(int frequency) {
        System.out.println("[DC] setFrequency=" + frequency + " antLv=" + antennaLevel + " at " + getBlockPos());
        this.receivedFrequency = frequency;
        this.recipeIndex = Math.abs(frequency) % 4;
        this.antennaLevel = 0;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.setBlock(getBlockPos(), getBlockState().setValue(DecodeComputerBlock.ACTIVE, false), 3);
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public void receiveFrequency(int frequency) {
        setFrequency(frequency);
    }

    public void receiveAntennaLevel(int level) {
        System.out.println("[DC] receiveAntennaLevel=" + level + " (was " + this.antennaLevel + ") freq=" + receivedFrequency + " at " + getBlockPos());
        this.antennaLevel = level;
        if (receivedFrequency >= 1) {
            this.recipeIndex = Math.abs(receivedFrequency) % 4;
        }
        setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            boolean active = isFrequencyInRange();
            System.out.println("[DC] ACTIVE=" + active + " (lv=" + antennaLevel + " freq=" + receivedFrequency + ")");
            this.level.setBlock(getBlockPos(), getBlockState().setValue(DecodeComputerBlock.ACTIVE, active), 3);
            this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isFrequencyInRange() {
        if (antennaLevel <= 0 || receivedFrequency < 1) return false;
        return switch (antennaLevel) {
            case 1 -> receivedFrequency >= 5 && receivedFrequency <= 10;
            case 2 -> receivedFrequency >= 3 && receivedFrequency <= 5;
            case 3 -> receivedFrequency >= 1 && receivedFrequency <= 3;
            default -> false;
        };
    }

    public boolean hasRecipe() {
        return recipeIndex >= 0 && isFrequencyInRange();
    }

    public int getRecipeIndex() {
        return recipeIndex;
    }

    public int getReceivedFrequency() {
        return receivedFrequency;
    }

    public int getAntennaLevel() {
        return antennaLevel;
    }

    public boolean tryCreateRecipe() {
        if (!hasRecipe()) return false;
        if (level == null || level.isClientSide()) return false;

        ItemStack input = inventory.getStackInSlot(0);
        if (!input.is(_Items.RECIPE_MEMORY.get())) return false;

        if (!inventory.getStackInSlot(1).isEmpty()) return false;

        Item resultItem = switch (recipeIndex) {
            case 0 -> _Items.SATELLITE_ENGINE_MEMORY.get();
            case 1 -> _Items.SATELLITE_BODY_RECIPE_MEMORY.get();
            case 2 -> _Items.SATELLITE_ADVANCED_SPACE_CHIP_MEMORY.get();
            case 3 -> _Items.SATELLITE_SOLAR_PANEL_RECIPE_MEMORY.get();
            default -> null;
        };

        if (resultItem == null) return false;

        input.shrink(1);
        inventory.setStackInSlot(1, new ItemStack(resultItem));

        recipeIndex = -1;
        level.setBlock(getBlockPos(), getBlockState().setValue(DecodeComputerBlock.ACTIVE, false), 3);
        setChanged();

        return true;
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("decode_computer.energy", ENERGY_STORAGE.getEnergyStored());
        tag.putInt("decode_computer.frequency", receivedFrequency);
        tag.putInt("decode_computer.recipe_index", recipeIndex);
        tag.putInt("decode_computer.antenna_level", antennaLevel);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(tag.getInt("decode_computer.energy"));
        receivedFrequency = tag.getInt("decode_computer.frequency");
        recipeIndex = tag.getInt("decode_computer.recipe_index");
        antennaLevel = tag.getInt("decode_computer.antenna_level");

        if (level != null && !level.isClientSide()) {
            boolean shouldBeActive = recipeIndex >= 0 && isFrequencyInRange();
            if (getBlockState().getValue(DecodeComputerBlock.ACTIVE) != shouldBeActive) {
                level.setBlock(getBlockPos(), getBlockState().setValue(DecodeComputerBlock.ACTIVE, shouldBeActive), 3);
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.d2tech.decode_computer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new DecodeComputerMenu(i, inventory, this);
    }

    public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
        return this.ENERGY_STORAGE;
    }

    public IItemHandler getItemHandler(Direction direction) {
        return this.inventory;
    }

    public void tick(Level level, BlockPos blockPos, BlockState state) {
    }
}
