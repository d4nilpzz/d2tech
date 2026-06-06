package dev.d4nilpzz.d2tech.event;

import dev.d4nilpzz.d2tech.D2tech;
import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.registry._Fluids;
import dev.d4nilpzz.d2tech.screen._MenuTypes;
import dev.d4nilpzz.d2tech.screen.custom.screen.AdvancedCraftingTableScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.CoalGeneratorScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.DecodeComputerScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.HydraulicPressScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.SolarGeneratorScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = D2tech.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(_MenuTypes.SOLAR_GENERATOR_MENU.get(), SolarGeneratorScreen::new);
        event.register(_MenuTypes.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
        event.register(_MenuTypes.HYDRAULIC_PRESS_MENU.get(), HydraulicPressScreen::new);
        event.register(_MenuTypes.DECODE_COMPUTER_MENU.get(), DecodeComputerScreen::new);
        event.register(_MenuTypes.ADVANCED_CRAFTING_TABLE_MENU.get(), AdvancedCraftingTableScreen::new);
    }

    @SubscribeEvent
    public static void registerFluidTypeExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return _Fluids.CRUDE_OIL_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return _Fluids.CRUDE_OIL_FLOW;
            }

            @Override
            public int getTintColor() {
                return 0xFF1A1A14;
            }
        }, _Fluids.CRUDE_OIL_TYPE.get());

        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return _Fluids.FUEL_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return _Fluids.FUEL_FLOW;
            }

            @Override
            public int getTintColor() {
                return 0x60B8B860;
            }
        }, _Fluids.FUEL_TYPE.get());
    }

    @SuppressWarnings({"deprecation", "CodeBlock2Expr"})
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(_Blocks.CABLE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(_Blocks.DATA_CABLE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(_Blocks.WARNING_LIGHT.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(_Fluids.CRUDE_OIL_BLOCK.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(_Fluids.FUEL_BLOCK.get(), RenderType.translucent());
        });
    }
}
