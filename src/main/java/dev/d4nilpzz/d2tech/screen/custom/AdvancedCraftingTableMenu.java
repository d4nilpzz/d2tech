package dev.d4nilpzz.d2tech.screen.custom;

import dev.d4nilpzz.d2tech.blocks.blockentity.AdvancedCraftingTableBlockEntity;
import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.screen._MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AdvancedCraftingTableMenu extends AbstractContainerMenu {
    public final AdvancedCraftingTableBlockEntity blockEntity;
    private final Level level;

    public AdvancedCraftingTableMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public AdvancedCraftingTableMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(_MenuTypes.ADVANCED_CRAFTING_TABLE_MENU.get(), containerId);
        this.blockEntity = ((AdvancedCraftingTableBlockEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Crafting grid 3x3 - slots 0-8 (standard alignment, like a normal crafting table)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 44, 14));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 62, 14));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 80, 14));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 3, 44, 32));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 4, 62, 32));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 5, 80, 32));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 6, 44, 50));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 7, 62, 50));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 8, 80, 50));

        // Recipe memory slot - slot 9 (bottom left)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 9, 18, 50));

        // Output slot - slot 10 (to the right of the grid)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 10, 106, 32) {
            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                if (AdvancedCraftingTableMenu.this.blockEntity.getCurrentRecipe().isPresent()) {
                    AdvancedCraftingTableMenu.this.blockEntity.consumeGridItems();
                }
                super.onTake(player, stack);
            }
        });

        // Battery slot - slot 11 (bottom right)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 11, 152, 76));
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 12;

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 11, TE_INVENTORY_FIRST_SLOT_INDEX + 12, false)) {
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX + 9, TE_INVENTORY_FIRST_SLOT_INDEX + 10, false)) {
                    moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + 9, false);
                }
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (pIndex == TE_INVENTORY_FIRST_SLOT_INDEX + 10) {
                if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, _Blocks.ADVANCED_CRAFTING_TABLE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
