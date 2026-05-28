package dev.d4nilpzz.d2tech.screen.custom.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.d4nilpzz.d2tech.screen.ScreenUtils;
import dev.d4nilpzz.d2tech.screen.custom.HydraulicPressMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class HydraulicPressScreen extends AbstractContainerScreen<HydraulicPressMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/hydraulic_press_menu.png");

    public HydraulicPressScreen(HydraulicPressMenu menu, Inventory playerInventory, Component title) {
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
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        if (ScreenUtils.isHovering(
                ScreenUtils.getEnergyBounds(this.leftPos + 155, this.topPos + 25),
                pMouseX,
                pMouseY
        )) {
            ScreenUtils.drawEnergyTooltip(
                    pGuiGraphics,
                    menu.blockEntity.getEnergyStorage(null).getEnergyStored(),
                    menu.blockEntity.getEnergyStorage(null).getMaxEnergyStored(),
                    pMouseX,
                    pMouseY
            );
        }
    }
}
