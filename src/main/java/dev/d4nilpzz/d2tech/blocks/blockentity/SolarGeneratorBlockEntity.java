package dev.d4nilpzz.d2tech.blocks.blockentity;

import dev.d4nilpzz.d2tech.blocks.base.BaseGeneratorBlockEntity;
import dev.d4nilpzz.d2tech.energy.EnergyUtils;
import dev.d4nilpzz.d2tech.registry._BlockEntities;
import dev.d4nilpzz.d2tech.screen.custom.SolarGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SolarGeneratorBlockEntity extends BaseGeneratorBlockEntity implements MenuProvider {

    public SolarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(
                _BlockEntities.SOLAR_GENERATOR_BE.get(),
                pos,
                state,
                20000,
                0,
                200,
                3,
                "solar_generator"
        );
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.d2tech.solar_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new SolarGeneratorMenu(id, inventory, this);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        if (level.canSeeSky(pos) && level.isDay()) {
            int genSpeed = genSpeed(level.getDayTime());
            ENERGY_STORAGE.internalReceive(genSpeed, false);
        }

        EnergyUtils.distributeNearly(this, ENERGY_STORAGE, 100);
    }

    private int genSpeed(long time) {
        int speed;
        if (time >= 24000 || (time >= 0 && time < 3000)) {
            speed = 10;
        } else if (time >= 3000 && time < 9000) {
            speed = 25;
        } else {
            speed = 10;
        }
        
        if (this.level.isRainingAt(this.getBlockPos())) {
            speed -= 5;
        }
        
        return speed;
    }
}