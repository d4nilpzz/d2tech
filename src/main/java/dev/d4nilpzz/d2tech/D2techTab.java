package dev.d4nilpzz.d2tech;

import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.registry._Items;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class D2techTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> REBUILD_TECH_CREATIVE_MODE_TAB =
            CREATIVE_MODE_TAB.register("rebuild_tech_creative_mode_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(_Blocks.STRUCTURE_BLOCK.get()))
                    .title(Component.translatable("itemGroup.d2tech"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(_Blocks.STRUCTURE_BLOCK.get());
                        output.accept(_Blocks.ANTENNA_BLOCK.get());
                        output.accept(_Blocks.ANTENNA_CONTROLLER.get());
                        output.accept(_Blocks.HYDRAULIC_PRESS.get());
                        output.accept(_Blocks.COAL_GENERATOR.get());
                        output.accept(_Blocks.DECODE_COMPUTER.get());
                        output.accept(_Blocks.SOLAR_GENERATOR.get());
                        output.accept(_Blocks.CABLE.get());
                        output.accept(_Blocks.DATA_CABLE.get());
                        output.accept(_Items.CONFIGURATOR.get());
                        output.accept(_Items.PLASTIC_PELLET.get());
                        output.accept(_Items.PLASTIC_SHEET.get());
                        output.accept(_Items.CHIP.get());
                        output.accept(_Items.ADVANCED_SPACE_SHIP.get());
                        output.accept(_Items.BATTERY.get());
                        output.accept(_Items.RECIPE_MEMORY.get());
                        output.accept(_Items.SATELLITE_ENGINE_MEMORY.get());
                        output.accept(_Items.SATELLITE_BODY_RECIPE_MEMORY.get());
                        output.accept(_Items.SATELLITE_ADVANCED_SPACE_CHIP_MEMORY.get());
                        output.accept(_Items.SATELLITE_SOLAR_PANEL_RECIPE_MEMORY.get());
                    })).build());

    public static final Supplier<CreativeModeTab> REBUILD_TECH_INTERCEPTOR_CREATIVE_MODE_TAB =
            CREATIVE_MODE_TAB.register("rebuild_tech_interceptor_creative_mode_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(_Items.STEEL.get()))
                    .title(Component.translatable("itemGroup.d2tech_ores"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(_Blocks.STEEL_DEEPSLATE_ORE.get());
                        output.accept(_Blocks.STEEL_ORE.get());
                        output.accept(_Items.RAW_STEEL.get());
                        output.accept(_Items.STEEL.get());
                        output.accept(_Blocks.ALUMINUM_DEEPSLATE_ORE.get());
                        output.accept(_Blocks.ALUMINUM_ORE.get());
                        output.accept(_Items.RAW_ALUMINUM.get());
                        output.accept(_Items.ALUMINUM.get());
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
