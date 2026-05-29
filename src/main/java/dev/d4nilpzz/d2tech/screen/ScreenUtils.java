package dev.d4nilpzz.d2tech.screen;


import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Collections;
import java.util.Optional;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class ScreenUtils {
    public static final int ENERGY_WIDTH = 10;
    public static final int ENERGY_HEIGHT = 46;

    public static final int[] ENERGY_SLOT = {125, 58};

    public static final ResourceLocation ENERGY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/overlay/energy.png");
    public static final ResourceLocation COAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/overlay/coal.png");
    public static final ResourceLocation ASCII_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/overlay/ascii_sga.png");
    public static final ResourceLocation PROCESS_V_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/overlay/process_v.png");

    public static boolean isHovering(Rectangle bounds, double x, double y) {
        double left = bounds.getX();
        double right = left + bounds.getWidth();
        double top = bounds.getY();
        double bottom = top + bounds.getHeight();
        return left <= x && x < right && top <= y && y < bottom;
    }

    public static void setTooltip(Rectangle bounds, double x, double y, GuiGraphics graphics, String text) {
        if (isHovering(bounds, x, y)) {
            graphics.renderTooltip(
                    Minecraft.getInstance().font,
                    Collections.singletonList(Component.literal(text).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))),
                    Optional.empty(),
                    (int)x,
                    (int)y
            );
        }
    }

    public static void drawAscii(GuiGraphics graphics, int x, int y) {
        Minecraft mc = Minecraft.getInstance();

        int tileSize = 8;

        int tilesX = 256 / tileSize;
        int tilesY = 256 / tileSize;

        int tx = mc.level.random.nextInt(tilesX);
        int ty = mc.level.random.nextInt(tilesY);

        int u = tx * tileSize;
        int v = ty * tileSize;

        graphics.blit(
                ASCII_TEXTURE,
                x,
                y,
                u,
                v,
                tileSize,
                tileSize,
                256,
                256
        );
    }

    public static void drawEnergy(GuiGraphics graphics, int x, int y, long energy, long maxEnergy) {
        double ratio = maxEnergy > 0L ? (double)((float)energy / (float)maxEnergy) : (double)0.0F;
        drawVertical(graphics, x, y, 10, 46, ENERGY_TEXTURE, ratio);
    }

    public static void drawCoal(GuiGraphics graphics, int x, int y, long current, long finish) {
        double ratio = finish > 0L ? (double)((float)current / (float)finish) : (double)0.0F;
        drawVertical(graphics, x, y, 4, 16, COAL_TEXTURE, ratio);
    }

    public static void drawVertical(GuiGraphics graphics, int x, int y, int width, int height, ResourceLocation resource, double ratio) {
        int ratioHeight = (int)Math.ceil((double)height * ratio);
        int remainHeight = height - ratioHeight;
        graphics.blit(resource, x, y + remainHeight, 0.0F, (float)remainHeight, width, ratioHeight, width, height);
    }

    private static String formatFE(long value) {
        if (value >= 1_000_000_000_000L)
            return String.format("%.0fT", value / 1_000_000_000_000.0);
        if (value >= 1_000_000_000L)
            return String.format("%.0fG", value / 1_000_000_000.0);
        if (value >= 1_000_000L)
            return String.format("%.0fM", value / 1_000_000.0);
        if (value >= 1_000L)
            return String.format("%.0fK", value / 1_000.0);
        return String.valueOf(value);
    }

    public static void drawEnergyTooltip(
            GuiGraphics graphics,
            long energy,
            long energyCapacity,
            int mouseX,
            int mouseY
    ) {
        Minecraft mc = Minecraft.getInstance();

        Component text = Component.literal(
                "§7" + formatFE(energy) + "§8/§7" + formatFE(energyCapacity) + " FE §6⚡"
        );

        graphics.renderTooltip(
                mc.font,
                Collections.singletonList(text),
                Optional.empty(),
                mouseX,
                mouseY
        );
    }



    public static void drawFluidTooltip(
            GuiGraphics graphics,
            long fluid,
            long fluidCapacity,
            int mouseX,
            int mouseY
    ) {
        Minecraft mc = Minecraft.getInstance();

        Component text = Component.literal(
                "§7" + (fluid) + "§8/§7" + (fluidCapacity) + " B §6🪣"
        );

        graphics.renderTooltip(
                mc.font,
                Collections.singletonList(text),
                Optional.empty(),
                mouseX,
                mouseY
        );
    }


    public static Rectangle getEnergyBounds(int x, int y) {
        return new Rectangle(x, y, 13, 46);
    }

    public static Rectangle getButtonBounds(int x, int y) {
        return new Rectangle(x, y, 10, 16);
    }

}
