package dev.d4nilpzz.d2tech.item.custom;

import dev.d4nilpzz.d2tech.item.TooltipItem;
import dev.d4nilpzz.d2tech.screen.ScreenUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Configurator extends TooltipItem {
    private static final String ENERGY_TAG = "energy";
    public static final int CAPACITY = 1000;
    public static final int MAX_TRANSFER = 200;
    private static final int ENERGY_PER_USE = 20;

    public Configurator(Properties properties, String translationKey) {
        super(properties, translationKey);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
        int energy = getEnergy(stack);
        double ratio = (double) energy / CAPACITY;

        int filled = (int) Math.ceil(10 * ratio);
        StringBuilder bar = new StringBuilder("§7[");
        for (int i = 0; i < 10; i++) {
            bar.append(i < filled ? "§a|" : "§8·");
        }
        bar.append("§7]");
        bar.append("§7 ").append(ScreenUtils.formatFE(energy)).append("§8/§7").append(ScreenUtils.formatFE(CAPACITY)).append(" FE §6⚡");
        tooltip.add(Component.literal(bar.toString()));

        super.appendHoverText(stack, context, tooltip, type);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (getEnergy(stack) < ENERGY_PER_USE) {
            return InteractionResult.FAIL;
        }
        setEnergy(stack, getEnergy(stack) - ENERGY_PER_USE);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getEnergy(stack) < ENERGY_PER_USE) {
            return InteractionResultHolder.fail(stack);
        }
        setEnergy(stack, getEnergy(stack) - ENERGY_PER_USE);
        return InteractionResultHolder.success(stack);
    }

    public static int getEnergy(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();
        return tag.contains(ENERGY_TAG) ? tag.getInt(ENERGY_TAG) : 0;
    }

    public static void setEnergy(ItemStack stack, int energy) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(ENERGY_TAG, Math.min(energy, CAPACITY));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static IEnergyStorage getEnergyStorage(ItemStack stack) {
        return new EnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER) {
            @Override
            public int getEnergyStored() {
                return getEnergy(stack);
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int energy = getEnergy(stack);
                int received = Math.min(CAPACITY - energy, Math.min(MAX_TRANSFER, maxReceive));
                if (!simulate) {
                    setEnergy(stack, energy + received);
                }
                return received;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int energy = getEnergy(stack);
                int extracted = Math.min(energy, Math.min(MAX_TRANSFER, maxExtract));
                if (!simulate) {
                    setEnergy(stack, energy - extracted);
                }
                return extracted;
            }
        };
    }
}
