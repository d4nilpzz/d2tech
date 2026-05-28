package dev.d4nilpzz.d2tech.dataGenerators.providers;

import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.registry._Tags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(_Blocks.STRUCTURE_BLOCK.get())

                // Ores
                .add(_Blocks.STEEL_ORE.get())
                .add(_Blocks.STEEL_DEEPSLATE_ORE.get())
                .add(_Blocks.ALUMINUM_ORE.get())
                .add(_Blocks.ALUMINUM_DEEPSLATE_ORE.get())

                .add(_Blocks.SOLAR_GENERATOR.get())
                .add(_Blocks.HEAT_GENERATOR.get())
                .add(_Blocks.HYDRAULIC_PRESS.get())

                .add(_Blocks.CABLE.get())

                .add(_Blocks.DECODE_COMPUTER.get())
                .add(_Blocks.ANTENNA_BLOCK.get())
                .add(_Blocks.ANTENNA_CONTROLLER.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(_Blocks.STRUCTURE_BLOCK.get())

                // Ores
                .add(_Blocks.STEEL_ORE.get())
                .add(_Blocks.STEEL_DEEPSLATE_ORE.get())
                .add(_Blocks.ALUMINUM_ORE.get())
                .add(_Blocks.ALUMINUM_DEEPSLATE_ORE.get())

                .add(_Blocks.SOLAR_GENERATOR.get())
                .add(_Blocks.HEAT_GENERATOR.get())
                .add(_Blocks.HYDRAULIC_PRESS.get())
                .add(_Blocks.COAL_GENERATOR.get())

                .add(_Blocks.CABLE.get())

                .add(_Blocks.DECODE_COMPUTER.get())
                .add(_Blocks.ANTENNA_BLOCK.get())
                .add(_Blocks.ANTENNA_CONTROLLER.get());

        tag(_Tags.Blocks.CONNECTABLE_CABLE)
                .add(_Blocks.CABLE.get())
                .add(_Blocks.SOLAR_GENERATOR.get())
                .add(_Blocks.HYDRAULIC_PRESS.get())
                .add(_Blocks.HEAT_GENERATOR.get())
                .add(_Blocks.COAL_GENERATOR.get());

        tag(_Tags.Blocks.CONFIGURABLE)
                .add(_Blocks.CABLE.get());
    }
}