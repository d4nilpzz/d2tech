package dev.d4nilpzz.d2tech.registry;

import dev.d4nilpzz.d2tech.D2tech;
import dev.d4nilpzz.d2tech.fluid.ModFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class _Fluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, D2tech.MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, D2tech.MODID);

    public static final ResourceLocation CRUDE_OIL_STILL = ResourceLocation.fromNamespaceAndPath(D2tech.MODID, "block/crude_oil_still");
    public static final ResourceLocation CRUDE_OIL_FLOW = ResourceLocation.fromNamespaceAndPath(D2tech.MODID, "block/crude_oil_flow");

    public static final Supplier<FluidType> CRUDE_OIL_TYPE = FLUID_TYPES.register("crude_oil",
            () -> new ModFluidType(
                    FluidType.Properties.create()
                            .density(3000)
                            .viscosity(6000)
                            .temperature(1300)
                            .canDrown(true)
                            .canSwim(false)
                            .canExtinguish(false)
            ));

    private static BaseFlowingFluid.Properties crudeOilProps;

    public static final Supplier<Fluid> CRUDE_OIL_SOURCE = FLUIDS.register("crude_oil",
            () -> new BaseFlowingFluid.Source(crudeOilProperties()));
    public static final Supplier<Fluid> CRUDE_OIL_FLUID = FLUIDS.register("crude_oil_flowing",
            () -> new BaseFlowingFluid.Flowing(crudeOilProperties()));

    public static final Supplier<LiquidBlock> CRUDE_OIL_BLOCK = _Blocks.registerFluidBlock("crude_oil",
            () -> new LiquidBlock(
                    (FlowingFluid) CRUDE_OIL_SOURCE.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .replaceable()
                            .noCollission()
                            .strength(100f)
                            .noLootTable()
                            .lightLevel(state -> 0)
                            .sound(SoundType.EMPTY)
                            .noTerrainParticles()
            ));
    public static final Supplier<BucketItem> CRUDE_OIL_BUCKET = _Items.registerBucket("crude_oil_bucket",
            () -> new BucketItem(CRUDE_OIL_SOURCE.get(), new Item.Properties().stacksTo(1)));

    private static BaseFlowingFluid.Properties crudeOilProperties() {
        if (crudeOilProps == null) {
            crudeOilProps = new BaseFlowingFluid.Properties(
                    CRUDE_OIL_TYPE,
                    () -> CRUDE_OIL_SOURCE.get(),
                    () -> CRUDE_OIL_FLUID.get()
            ).block(() -> CRUDE_OIL_BLOCK.get())
                    .bucket(() -> CRUDE_OIL_BUCKET.get())
                    .tickRate(30)
                    .slopeFindDistance(2)
                    .levelDecreasePerBlock(2)
                    .explosionResistance(100f);
        }
        return crudeOilProps;
    }

    public static final ResourceLocation FUEL_STILL = ResourceLocation.fromNamespaceAndPath(D2tech.MODID, "block/fuel_still");
    public static final ResourceLocation FUEL_FLOW = ResourceLocation.fromNamespaceAndPath(D2tech.MODID, "block/fuel_flow");

    public static final Supplier<FluidType> FUEL_TYPE = FLUID_TYPES.register("fuel",
            () -> new ModFluidType(
                    FluidType.Properties.create()
                            .density(800)
                            .viscosity(1200)
                            .temperature(300)
                            .canDrown(true)
                            .canSwim(false)
                            .canExtinguish(false)
            ));

    private static BaseFlowingFluid.Properties fuelProps;

    public static final Supplier<Fluid> FUEL_SOURCE = FLUIDS.register("fuel",
            () -> new BaseFlowingFluid.Source(fuelProperties()));
    public static final Supplier<Fluid> FUEL_FLUID = FLUIDS.register("fuel_flowing",
            () -> new BaseFlowingFluid.Flowing(fuelProperties()));

    public static final Supplier<LiquidBlock> FUEL_BLOCK = _Blocks.registerFluidBlock("fuel",
            () -> new LiquidBlock(
                    (FlowingFluid) FUEL_SOURCE.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_YELLOW)
                            .replaceable()
                            .noCollission()
                            .strength(100f)
                            .noLootTable()
                            .lightLevel(state -> 0)
                            .sound(SoundType.EMPTY)
                            .noTerrainParticles()
            ));
    public static final Supplier<BucketItem> FUEL_BUCKET = _Items.registerBucket("fuel_bucket",
            () -> new BucketItem(FUEL_SOURCE.get(), new Item.Properties().stacksTo(1)));

    private static BaseFlowingFluid.Properties fuelProperties() {
        if (fuelProps == null) {
            fuelProps = new BaseFlowingFluid.Properties(
                    FUEL_TYPE,
                    () -> FUEL_SOURCE.get(),
                    () -> FUEL_FLUID.get()
            ).block(() -> FUEL_BLOCK.get())
                    .bucket(() -> FUEL_BUCKET.get())
                    .tickRate(10)
                    .slopeFindDistance(3)
                    .levelDecreasePerBlock(1)
                    .explosionResistance(100f);
        }
        return fuelProps;
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}
