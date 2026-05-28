package dev.d4nilpzz.d2tech.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TooltipItem extends Item {

    private final String translationKey;

    public TooltipItem(Properties properties, String translationKey) {
        super(properties);
        this.translationKey = translationKey;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);

        var showExtra = Screen.hasControlDown();

        if (showExtra) {
            tooltip.add(Component.translatable(translationKey).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.d2tech.item_extra_info").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
