package dev.d4nilpzz.d2tech.dataGenerators.providers;

import dev.d4nilpzz.d2tech.registry._Blocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Ores
        blockWithItem(_Blocks.STEEL_ORE);
        blockWithItem(_Blocks.STEEL_DEEPSLATE_ORE);

        blockWithItem(_Blocks.ALUMINUM_ORE);
        blockWithItem(_Blocks.ALUMINUM_DEEPSLATE_ORE);

        blockWithItem(_Blocks.STRUCTURE_BLOCK);
        blockWithItem(_Blocks.ANTENNA_BLOCK);
        blockWithItem(_Blocks.ANTENNA_CONTROLLER);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}