package dev.d4nilpzz.d2tech.registry;

import dev.d4nilpzz.d2tech.item.custom.BatteryItem;
import dev.d4nilpzz.d2tech.item.custom.BatteryItem;
import dev.d4nilpzz.d2tech.item.custom.Configurator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class _Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> STEEL = ITEMS.register("steel",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_STEEL = ITEMS.register("raw_steel",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ALUMINUM = ITEMS.register("aluminum",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_ALUMINUM = ITEMS.register("raw_aluminum",
            () -> new Item(new Item.Properties()));

    // Chip
    public static final DeferredItem<Item> CHIP = ITEMS.register("chip",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ADVANCED_SPACE_SHIP = ITEMS.register("advanced_space_ship",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.register("battery",
            () -> new BatteryItem(new Item.Properties().stacksTo(1)));

    // Plastic
    public static final DeferredItem<Item> PLASTIC_PELLET = ITEMS.register("plastic_pellet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PLASTIC_SHEET = ITEMS.register("plastic_sheet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Configurator> CONFIGURATOR = ITEMS.register("configurator",
            () -> new Configurator(new Item.Properties().rarity(Rarity.RARE), "tooltip.d2tech.configurator"));

    // Recipe Memory Items
    public static final DeferredItem<Item> RECIPE_MEMORY = ITEMS.register("recipe_memory",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SATELLITE_ENGINE_MEMORY = ITEMS.register("satellite_engine_memory",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> SATELLITE_BODY_RECIPE_MEMORY = ITEMS.register("satellite_body_recipe_memory",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> SATELLITE_ADVANCED_SPACE_CHIP_MEMORY = ITEMS.register("satellite_advanced_space_chip_memory",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> SATELLITE_SOLAR_PANEL_RECIPE_MEMORY = ITEMS.register("satellite_solar_panel_recipe_memory",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
