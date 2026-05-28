package dev.d4nilpzz.d2tech;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = D2tech.MODID)
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> VERSION =
            BUILDER.comment("DONT CHANGE THIS!")
                    .define("version", "1.0.0");

    public static final ModConfigSpec.IntValue PROCESS_TIME_SECONDS =
            BUILDER.comment("Time in seconds that a process should take")
                    .defineInRange("processTimeSeconds", 5, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static String version;
    public static int processTimeSeconds;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        version = VERSION.get();
        processTimeSeconds = PROCESS_TIME_SECONDS.get();
    }
}
