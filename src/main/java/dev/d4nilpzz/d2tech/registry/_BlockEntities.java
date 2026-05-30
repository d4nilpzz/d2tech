package dev.d4nilpzz.d2tech.registry;

import dev.d4nilpzz.d2tech.blocks.blockentity.AdvancedCraftingTableBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.AntennaControllerBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.CoalGeneratorBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.DecodeComputerBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.HydraulicPressBlockEntity;
import dev.d4nilpzz.d2tech.blocks.blockentity.SolarGeneratorBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class _BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);

    public static final Supplier<BlockEntityType<SolarGeneratorBlockEntity>> SOLAR_GENERATOR_BE =
            BLOCK_ENTITIES.register("solar_generator", () -> BlockEntityType.Builder.of(
                    SolarGeneratorBlockEntity::new, _Blocks.SOLAR_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR_BE =
            BLOCK_ENTITIES.register("coal_generator", () -> BlockEntityType.Builder.of(
                    CoalGeneratorBlockEntity::new, _Blocks.COAL_GENERATOR.get()).build(null));

    public static final Supplier<BlockEntityType<HydraulicPressBlockEntity>> HYDRAULIC_PRESS_BE =
            BLOCK_ENTITIES.register("hydraulic_press", () -> BlockEntityType.Builder.of(
                    HydraulicPressBlockEntity::new, _Blocks.HYDRAULIC_PRESS.get()).build(null));

    public static final Supplier<BlockEntityType<DecodeComputerBlockEntity>> DECODE_COMPUTER_BE =
            BLOCK_ENTITIES.register("decode_computer", () -> BlockEntityType.Builder.of(
                    DecodeComputerBlockEntity::new, _Blocks.DECODE_COMPUTER.get()).build(null));

    public static final Supplier<BlockEntityType<AntennaControllerBlockEntity>> ANTENNA_CONTROLLER_BE =
            BLOCK_ENTITIES.register("antenna_controller", () -> BlockEntityType.Builder.of(
                    AntennaControllerBlockEntity::new, _Blocks.ANTENNA_CONTROLLER.get()).build(null));

    public static final Supplier<BlockEntityType<AdvancedCraftingTableBlockEntity>> ADVANCED_CRAFTING_TABLE_BE =
            BLOCK_ENTITIES.register("advanced_crafting_table", () -> BlockEntityType.Builder.of(
                    AdvancedCraftingTableBlockEntity::new, _Blocks.ADVANCED_CRAFTING_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
