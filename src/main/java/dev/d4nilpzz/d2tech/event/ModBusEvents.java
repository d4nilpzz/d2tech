package dev.d4nilpzz.d2tech.event;

import dev.d4nilpzz.d2tech.D2tech;
import dev.d4nilpzz.d2tech.blocks.blockentity.CoalGeneratorBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.DecodeComputerBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.HydraulicPressBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.SolarGeneratorBlockEntity;
import dev.d4nilpzz.d2tech.item.custom.BatteryItem;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = D2tech.MODID)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, _BlockEntities.SOLAR_GENERATOR_BE.get(), SolarGeneratorBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, _BlockEntities.SOLAR_GENERATOR_BE.get(), SolarGeneratorBlockEntity::getEnergyStorage);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, _BlockEntities.COAL_GENERATOR_BE.get(), CoalGeneratorBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, _BlockEntities.COAL_GENERATOR_BE.get(), CoalGeneratorBlockEntity::getEnergyStorage);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, _BlockEntities.HYDRAULIC_PRESS_BE.get(), HydraulicPressBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, _BlockEntities.HYDRAULIC_PRESS_BE.get(), HydraulicPressBlockEntity::getEnergyStorage);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, _BlockEntities.DECODE_COMPUTER_BE.get(), DecodeComputerBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, _BlockEntities.DECODE_COMPUTER_BE.get(), DecodeComputerBlockEntity::getEnergyStorage);

        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ctx) -> BatteryItem.getEnergyStorage(stack), _Items.BATTERY.get());
    }
}
