package dev.d4nilpzz.d2tech.dataGenerators.providers;

import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.registry._Items;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        dropSelf(_Blocks.STRUCTURE_BLOCK.get());
        dropSelf(_Blocks.SOLAR_GENERATOR.get());
        dropSelf(_Blocks.HEAT_GENERATOR.get());
        dropSelf(_Blocks.HYDRAULIC_PRESS.get());
        dropSelf(_Blocks.COAL_GENERATOR.get());
        dropSelf(_Blocks.CABLE.get());
        dropSelf(_Blocks.DATA_CABLE.get());

        // Learning Recipe System
        dropSelf(_Blocks.DECODE_COMPUTER.get());
        dropSelf(_Blocks.ANTENNA_BLOCK.get());
        dropSelf(_Blocks.ANTENNA_CONTROLLER.get());

        dropSelf(_Blocks.ADVANCED_CRAFTING_TABLE.get());

        add(_Blocks.STEEL_ORE.get(),
                block -> createOreDrop(_Blocks.STEEL_ORE.get(), _Items.RAW_STEEL.get()));

        add(_Blocks.STEEL_DEEPSLATE_ORE.get(),
                block -> createMultipleOreDrops(_Blocks.STEEL_DEEPSLATE_ORE.get(), _Items.RAW_STEEL.get(), 2, 5));

        add(_Blocks.ALUMINUM_ORE.get(),
                block -> createOreDrop(_Blocks.ALUMINUM_ORE.get(), _Items.RAW_ALUMINUM.get()));

        add(_Blocks.ALUMINUM_DEEPSLATE_ORE.get(),
                block -> createMultipleOreDrops(_Blocks.ALUMINUM_DEEPSLATE_ORE.get(), _Items.RAW_ALUMINUM.get(), 2, 5));

    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return _Blocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
