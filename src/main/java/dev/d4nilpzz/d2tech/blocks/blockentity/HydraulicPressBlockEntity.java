package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.blocks.custom.HydraulicPressBlock;
import dev.d4nilpzz.d2tech.energy.ModEnergyStorage;
import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipe;
import dev.d4nilpzz.d2tech.recipe.HydraulicPressRecipeInput;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._RecipeTypes;
import dev.d4nilpzz.d2tech.screen.custom.HydraulicPressMenu;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class HydraulicPressBlockEntity extends BlockEntity implements MenuProvider {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int BATTERY_SLOT = 2;

    public final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT -> getLevel() != null && getLevel().getRecipeManager()
                        .getRecipeFor(_RecipeTypes.HYDRAULIC_PRESS_TYPE.get(), new HydraulicPressRecipeInput(stack), getLevel())
                        .isPresent();
                case OUTPUT_SLOT -> false;
                case BATTERY_SLOT -> true;
                default -> super.isItemValid(slot, stack);
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
        return new ModEnergyStorage(20000, 200, 0) {
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
    private int energyPerTick;

    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int i) {
            return switch (i) {
                case 0 -> progress;
                case 1 -> maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int i, int value) {
            switch (i) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public HydraulicPressBlockEntity(BlockPos pos, BlockState blockState) {
        super(_BlockEntities.HYDRAULIC_PRESS_BE.get(), pos, blockState);
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
        tag.putInt("hydraulic_press.energy", ENERGY_STORAGE.getEnergyStored());
        tag.putInt("hydraulic_press.progress", progress);
        tag.putInt("hydraulic_press.max_progress", maxProgress);
        tag.putInt("hydraulic_press.energy_per_tick", energyPerTick);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(tag.getInt("hydraulic_press.energy"));
        progress = tag.getInt("hydraulic_press.progress");
        maxProgress = tag.getInt("hydraulic_press.max_progress");
        energyPerTick = tag.getInt("hydraulic_press.energy_per_tick");
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
        return Component.translatable("block.d2tech.hydraulic_press");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new HydraulicPressMenu(i, inventory, this, containerData);
    }

    public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
        return this.ENERGY_STORAGE;
    }

    public IItemHandler getItemHandler(Direction direction) {
        return this.inventory;
    }

    private Optional<RecipeHolder<HydraulicPressRecipe>> getCurrentRecipe() {
        if (level == null) return Optional.empty();
        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        return level.getRecipeManager()
                .getRecipeFor(_RecipeTypes.HYDRAULIC_PRESS_TYPE.get(), new HydraulicPressRecipeInput(input), level);
    }

    public void tick(Level level, BlockPos blockPos, BlockState state) {
        if (level.isClientSide) return;

        boolean active = progress > 0;
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);

        Optional<RecipeHolder<HydraulicPressRecipe>> recipeHolder = getCurrentRecipe();

        if (recipeHolder.isPresent()) {
            HydraulicPressRecipe recipe = recipeHolder.get().value();
            ItemStack result = recipe.getResultItem(null);

            if (maxProgress == 0) {
                maxProgress = recipe.time();
                energyPerTick = Math.max(1, recipe.energy() / recipe.time());
            }

            boolean canOutput = output.isEmpty()
                    || (output.is(result.getItem()) && output.getCount() + result.getCount() <= output.getMaxStackSize());

            if (canOutput && ENERGY_STORAGE.getEnergyStored() >= energyPerTick) {
                ENERGY_STORAGE.internalExtract(energyPerTick, false);
                progress++;

                if (progress >= maxProgress) {
                    inventory.getStackInSlot(INPUT_SLOT).shrink(1);
                    if (output.isEmpty()) {
                        inventory.setStackInSlot(OUTPUT_SLOT, result.copy());
                    } else {
                        output.grow(result.getCount());
                    }
                    progress = 0;
                    maxProgress = 0;
                }
            } else if (progress > 0) {
                progress--;
            }
        } else {
            if (progress > 0) progress--;
            else maxProgress = 0;
        }

        if (state.getValue(HydraulicPressBlock.ACTIVE) != active) {
            level.setBlock(blockPos, state.setValue(HydraulicPressBlock.ACTIVE, active), 3);
        }
    }
}
