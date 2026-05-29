package dev.d4nilpzz.d2tech.screen.custom.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.d4nilpzz.d2tech.screen.ScreenUtils;
import dev.d4nilpzz.d2tech.screen.custom.DecodeComputerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class DecodeComputerScreen extends AbstractContainerScreen<DecodeComputerMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/decode_computer_menu.png");
    private static final ResourceLocation SLIDE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/overlay/slide.png");

    private static final int TRACK_X = 100;
    private static final int TRACK_Y = 11;
    private static final int TRACK_H = 34;
    private static final int TRACK_HIT_W = 14;
    private static final int POSITIONS = 10;
    private static final int WAVE_COLOR = 0xFF00CC44;

    private boolean isDragging = false;
    private int sliderValue = -1;

    public DecodeComputerScreen(DecodeComputerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        ScreenUtils.drawEnergy(
                guiGraphics,
                this.leftPos + 155,
                this.topPos + 25,
                menu.blockEntity.getEnergyStorage(null).getEnergyStored(),
                menu.blockEntity.getEnergyStorage(null).getMaxEnergyStored()
        );

        drawWave(guiGraphics);
        drawSlider(guiGraphics);
    }

    private void drawWave(GuiGraphics guiGraphics) {
        int bx = this.leftPos + 48;
        int by = this.topPos + 15;
        int centerY = by + 4;
        int w = 40;
        long time = minecraft != null ? minecraft.level != null ? minecraft.level.getGameTime() : 0 : 0;
        double offset = (time % 20) / 20.0 * Math.PI * 2;

        int val = sliderValue >= 0 ? sliderValue : Math.max(menu.blockEntity.getReceivedFrequency(), 1);
        if (val < 1) val = 1;
        if (val > POSITIONS) val = POSITIONS;

        double cycles = 1.0 + (POSITIONS - val) * 3.0 / (POSITIONS - 1);
        double amp = 1.0 + (POSITIONS - val) * 4.0 / (POSITIONS - 1);

        for (int px = 0; px < w; px++) {
            double phase = (px / (double) w) * Math.PI * 2 * cycles + offset;
            int py = (int) (Math.sin(phase) * amp);
            int py2 = (int) (Math.sin(phase + 0.8) * amp * 0.6);
            guiGraphics.fill(bx + px, centerY + py, bx + px + 1, centerY + py + 1, WAVE_COLOR);
            guiGraphics.fill(bx + px, centerY + py2, bx + px + 1, centerY + py2 + 1, WAVE_COLOR);
        }
    }

    private void drawSlider(GuiGraphics guiGraphics) {
        int bx = this.leftPos;
        int by = this.topPos;

        int val = sliderValue >= 0 ? sliderValue : Math.max(menu.blockEntity.getReceivedFrequency(), 1);
        if (val < 1) val = 1;
        if (val > POSITIONS) val = POSITIONS;

        int thumbY = by + TRACK_Y + (POSITIONS - val) * (TRACK_H - 5) / (POSITIONS - 1);

        int tx = bx + TRACK_X;

        int thumbX = tx - 1;
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, SLIDE_TEXTURE);
        guiGraphics.blit(SLIDE_TEXTURE, thumbX, thumbY, 0, 0, 5, 5, 5, 5);

        int freqMHz = val * 100;
        guiGraphics.drawString(font, freqMHz + "MHz", bx + TRACK_X + 5, by + TRACK_Y + TRACK_H - 4, 0x404040, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int bx = this.leftPos;
        int by = this.topPos;
        int sx = bx + TRACK_X - 2;
        int sy = by + TRACK_Y;
        int ex = sx + TRACK_HIT_W;

        if (mouseX >= sx && mouseX < ex && mouseY >= sy && mouseY < sy + TRACK_H) {
            int val = getSliderValueFromMouse(mouseY);
            sliderValue = val;
            isDragging = true;
            if (minecraft != null && minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 100 + val);
            }
            return true;
        }

        if (ScreenUtils.isHovering(ScreenUtils.getButtonBounds(this.leftPos + 63, this.topPos + 52), mouseX, mouseY)) {
            if (minecraft != null && minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            int val = getSliderValueFromMouse(mouseY);
            if (val != sliderValue) {
                sliderValue = val;
                if (minecraft != null && minecraft.gameMode != null) {
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 100 + val);
                }
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging) {
            isDragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (ScreenUtils.isHovering(
                ScreenUtils.getEnergyBounds(this.leftPos + 155, this.topPos + 25),
                mouseX, mouseY
        )) {
            ScreenUtils.drawEnergyTooltip(
                    guiGraphics,
                    menu.blockEntity.getEnergyStorage(null).getEnergyStored(),
                    menu.blockEntity.getEnergyStorage(null).getMaxEnergyStored(),
                    mouseX, mouseY
            );
        }

        ScreenUtils.setTooltip(ScreenUtils.getButtonBounds(this.leftPos + 63, this.topPos + 52),
                mouseX, mouseY, guiGraphics, "Create Recipe");

        drawSliderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void drawSliderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int bx = this.leftPos;
        int by = this.topPos;
        int sx = bx + TRACK_X - 2;
        int sy = by + TRACK_Y;

        if (mouseX >= sx && mouseX < sx + TRACK_HIT_W && mouseY >= sy && mouseY < sy + TRACK_H) {
            int val = getSliderValueFromMouse(mouseY);
            int level = menu.blockEntity.getAntennaLevel();
            String lvl = level > 0 ? " (Lv." + level + ")" : "";
            guiGraphics.renderTooltip(font, Component.literal(val * 100 + " MHz" + lvl), mouseX, mouseY);
        }
    }

    private int getSliderValueFromMouse(double mouseY) {
        int by = this.topPos;
        double rel = (by + TRACK_Y + TRACK_H - mouseY) / (double) (TRACK_H - 5);
        rel = Math.max(0, Math.min(1, rel));
        int val = (int) Math.round(rel * (POSITIONS - 1)) + 1;
        return Math.max(1, Math.min(POSITIONS, val));
    }
}
