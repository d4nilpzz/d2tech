package dev.d4nilpzz.d2tech.screen;

import dev.d4nilpzz.d2tech.screen.custom.CoalGeneratorMenu;
import dev.d4nilpzz.d2tech.screen.custom.DecodeComputerMenu;
import dev.d4nilpzz.d2tech.screen.custom.HydraulicPressMenu;
import dev.d4nilpzz.d2tech.screen.custom.SolarGeneratorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.d4nilpzz.d2tech.D2tech.MODID;

public class _MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<SolarGeneratorMenu>> SOLAR_GENERATOR_MENU =
            registerMenuType("solar_generator_menu", SolarGeneratorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<CoalGeneratorMenu>> COAL_GENERATOR_MENU =
            registerMenuType("coal_generator_menu", CoalGeneratorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<HydraulicPressMenu>> HYDRAULIC_PRESS_MENU =
            registerMenuType("hydraulic_press_menu", HydraulicPressMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<DecodeComputerMenu>> DECODE_COMPUTER_MENU =
            registerMenuType("decode_computer_menu", DecodeComputerMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                               IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
