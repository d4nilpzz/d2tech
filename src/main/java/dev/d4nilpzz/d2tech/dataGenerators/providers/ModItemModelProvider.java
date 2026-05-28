package dev.d4nilpzz.d2tech.dataGenerators.providers;

import dev.d4nilpzz.d2tech.registry._Items;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(_Items.STEEL.get());
        basicItem(_Items.RAW_STEEL.get());

        basicItem(_Items.ALUMINUM.get());
        basicItem(_Items.RAW_ALUMINUM.get());

        basicItem(_Items.CHIP.get());
        basicItem(_Items.ADVANCED_CHIP.get());
        basicItem(_Items.BATTERY.get());

        basicItem(_Items.PLASTIC_PELLET.get());
        basicItem(_Items.PLASTIC_SHEET.get());

        basicItem(_Items.CONFIGURATOR.get());
    }
}
