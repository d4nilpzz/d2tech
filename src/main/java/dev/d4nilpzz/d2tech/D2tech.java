package dev.d4nilpzz.d2tech;

import com.mojang.logging.LogUtils;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.registry._Blocks;
import dev.d4nilpzz.d2tech.registry._Items;
import dev.d4nilpzz.d2tech.screen._MenuTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(D2tech.MODID)
public class D2tech {
    public static final String MODID = "d2tech";
    private static final Logger LOGGER = LogUtils.getLogger();

    public D2tech(IEventBus modEventBus, ModContainer modContainer) {
        _Items.register(modEventBus);
        _Blocks.register(modEventBus);
        _BlockEntities.register(modEventBus);
        _MenuTypes.register(modEventBus);

        D2techTab.register(modEventBus);
        
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    
}
