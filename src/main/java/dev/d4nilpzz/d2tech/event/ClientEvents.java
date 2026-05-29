package dev.d4nilpzz.d2tech.event;

import dev.d4nilpzz.d2tech.D2tech;
import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.screen._MenuTypes;
import dev.d4nilpzz.d2tech.screen.custom.screen.CoalGeneratorScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.DecodeComputerScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.HydraulicPressScreen;
import dev.d4nilpzz.d2tech.screen.custom.screen.SolarGeneratorScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = D2tech.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(_MenuTypes.SOLAR_GENERATOR_MENU.get(), SolarGeneratorScreen::new);
        event.register(_MenuTypes.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
        event.register(_MenuTypes.HYDRAULIC_PRESS_MENU.get(), HydraulicPressScreen::new);
        event.register(_MenuTypes.DECODE_COMPUTER_MENU.get(), DecodeComputerScreen::new);
    }

    @SuppressWarnings({"deprecation", "CodeBlock2Expr"})
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(_Blocks.CABLE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(_Blocks.DATA_CABLE.get(), RenderType.cutout());
        });
    }
}
