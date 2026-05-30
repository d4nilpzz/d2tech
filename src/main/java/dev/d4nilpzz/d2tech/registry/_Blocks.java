package dev.d4nilpzz.d2tech.registry;

import dev.d4nilpzz.d2tech.blocks.custom.AdvancedCraftingTableBlock;
import dev.d4nilpzz.d2tech.blocks.custom.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class _Blocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> STRUCTURE_BLOCK = registerBlock("structure_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    // Ores
    public static final DeferredBlock<Block> STEEL_ORE = registerBlock("steel_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> STEEL_DEEPSLATE_ORE = registerBlock("steel_deepslate_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ALUMINUM_ORE = registerBlock("aluminum_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ALUMINUM_DEEPSLATE_ORE = registerBlock("aluminum_deepslate_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    // Machines
    public static final DeferredBlock<SolarGeneratorBlock> SOLAR_GENERATOR = registerBlock("solar_generator",
            () -> new SolarGeneratorBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<CoalGeneratorBlock> COAL_GENERATOR = registerBlock("coal_generator",
            () -> new CoalGeneratorBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<HydraulicPressBlock> HYDRAULIC_PRESS = registerBlock("hydraulic_press",
            () -> new HydraulicPressBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> HEAT_GENERATOR = registerBlock("heat_generator",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    // Learning Recipe System

    public static final DeferredBlock<DecodeComputerBlock> DECODE_COMPUTER = registerBlock("decode_computer",
            () -> new DecodeComputerBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ANTENNA_BLOCK = registerBlock("antenna_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<AntennaControllerBlock> ANTENNA_CONTROLLER = registerBlock("antenna_controller",
            () -> new AntennaControllerBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));


    // Advanced Crafting Table
    public static final DeferredBlock<AdvancedCraftingTableBlock> ADVANCED_CRAFTING_TABLE = registerBlock("advanced_crafting_table",
            () -> new AdvancedCraftingTableBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    // Cable
    public static final DeferredBlock<CableBlock> CABLE = registerBlock(
            "cable",
            () -> new CableBlock(
                    BlockBehaviour.Properties.of()
                            .strength(4f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                            _Tags.Blocks.CONNECTABLE_CABLE
            )
    );

    // Data Cable
    public static final DeferredBlock<DataCableBlock> DATA_CABLE = registerBlock(
            "data_cable",
            () -> new DataCableBlock(
                    BlockBehaviour.Properties.of()
                            .strength(4f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    _Tags.Blocks.CONNECTABLE_DATA_CABLE
            )
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        _Items.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
