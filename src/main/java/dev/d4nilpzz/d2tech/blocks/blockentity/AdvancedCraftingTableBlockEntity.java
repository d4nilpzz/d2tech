package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.blocks.custom.AdvancedCraftingTableBlock;
import dev.d4nilpzz.d2tech.energy.ModEnergyStorage;
import dev.d4nilpzz.d2tech.item.custom.BatteryItem;
import dev.d4nilpzz.d2tech.recipe.AdvancedCraftingRecipe;
import dev.d4nilpzz.d2tech.recipe.AdvancedCraftingRecipeInput;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._RecipeTypes;
import dev.d4nilpzz.d2tech.screen.custom.AdvancedCraftingTableMenu;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class AdvancedCraftingTableBlockEntity extends BlockEntity implements MenuProvider {

    private static final int CRAFTING_SLOT_START = 0;
    private static final int CRAFTING_SLOT_END = 8;
    private static final int MEMORY_SLOT = 9;
    private static final int OUTPUT_SLOT = 10;
    private static final int BATTERY_SLOT = 11;

    private static final int ENERGY_COST = 1000;

    public final ItemStackHandler inventory = new ItemStackHandler(12) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return slot == OUTPUT_SLOT ? 64 : 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case OUTPUT_SLOT -> false;
                case BATTERY_SLOT -> Capabilities.EnergyStorage.ITEM.getCapability(stack, null) != null;
                default -> true;
            };
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(50000, 500, 0) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                assert getLevel() != null;
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    private int progress;
    private int maxProgress;

    public AdvancedCraftingTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(_BlockEntities.ADVANCED_CRAFTING_TABLE_BE.get(), pos, blockState);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("advanced_crafting_table.energy", ENERGY_STORAGE.getEnergyStored());
        tag.putInt("advanced_crafting_table.progress", progress);
        tag.putInt("advanced_crafting_table.max_progress", maxProgress);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(tag.getInt("advanced_crafting_table.energy"));
        progress = tag.getInt("advanced_crafting_table.progress");
        maxProgress = tag.getInt("advanced_crafting_table.max_progress");
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
        return Component.translatable("block.d2tech.advanced_crafting_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new AdvancedCraftingTableMenu(i, inventory, this);
    }

    public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
        return this.ENERGY_STORAGE;
    }

    public IItemHandler getItemHandler(Direction direction) {
        return this.inventory;
    }

    private Optional<RecipeHolder<AdvancedCraftingRecipe>> getCurrentRecipe() {
        if (level == null) return Optional.empty();
        ItemStack memory = inventory.getStackInSlot(MEMORY_SLOT);
        if (memory.isEmpty()) return Optional.empty();

        ItemStack[] grid = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            grid[i] = inventory.getStackInSlot(CRAFTING_SLOT_START + i);
        }

        return level.getRecipeManager()
                .getRecipeFor(_RecipeTypes.ADVANCED_CRAFTING_TYPE.get(), new AdvancedCraftingRecipeInput(memory, grid), level);
    }

    private void chargeBattery() {
        ItemStack batteryStack = inventory.getStackInSlot(BATTERY_SLOT);
        if (batteryStack.isEmpty()) return;

        IEnergyStorage batteryCap = Capabilities.EnergyStorage.ITEM.getCapability(batteryStack, null);
        if (batteryCap == null) return;

        int machineRoom = ENERGY_STORAGE.getMaxEnergyStored() - ENERGY_STORAGE.getEnergyStored();
        if (machineRoom > 0) {
            int extracted = batteryCap.extractEnergy(Math.min(machineRoom, BatteryItem.MAX_TRANSFER), true);
            if (extracted > 0) {
                batteryCap.extractEnergy(extracted, false);
                ENERGY_STORAGE.internalReceive(extracted, false);
            }
            return;
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState state) {
        if (level.isClientSide) return;

        boolean active = progress > 0;
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);

        Optional<RecipeHolder<AdvancedCraftingRecipe>> recipeHolder = getCurrentRecipe();

        if (recipeHolder.isPresent()) {
            AdvancedCraftingRecipe recipe = recipeHolder.get().value();
            ItemStack result = recipe.getResultItem(null);

            if (maxProgress == 0) {
                maxProgress = 100;
            }

            boolean canOutput = output.isEmpty()
                    || (output.is(result.getItem()) && output.getCount() + result.getCount() <= output.getMaxStackSize());

            int energyPerTick = Math.max(1, ENERGY_COST / maxProgress);

            if (canOutput && ENERGY_STORAGE.getEnergyStored() >= energyPerTick) {
                ENERGY_STORAGE.internalExtract(energyPerTick, false);
                progress++;

                if (progress >= maxProgress) {
                    for (int i = 0; i < 9; i++) {
                        inventory.getStackInSlot(CRAFTING_SLOT_START + i).shrink(1);
                    }
                    if (output.isEmpty()) {
                        inventory.setStackInSlot(OUTPUT_SLOT, result.copy());
                    } else {
                        output.grow(result.getCount());
                    }
                    progress = 0;
                    maxProgress = 0;
                }
            }
        } else {
            if (progress > 0) progress--;
            else maxProgress = 0;
        }

        chargeBattery();

        if (state.getValue(AdvancedCraftingTableBlock.ACTIVE) != active) {
            level.setBlock(blockPos, state.setValue(AdvancedCraftingTableBlock.ACTIVE, active), 3);
        }
    }
}
