package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.block.GunWorkbenchBlock;
import com.jdolphin.ricksportalgun.common.block.PortalDispenserBlock;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class FabricBlocks {

    public static void register() {
        PGBlocks.GUN_WORKBENCH = registerBlock("portal_gun_workbench", GunWorkbenchBlock::new,
                BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).sound(SoundType.COPPER).strength(1.5F, 6.0F));

        PGBlocks.PORTAL_DISPENSER = registerBlock("portal_dispenser", PortalDispenserBlock::new, BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_GRAY).sound(SoundType.STONE).strength(1.5F, 6.0F));
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        return Blocks.register(keyOf(name), factory, properties);
    }

    private static ResourceKey<Block> keyOf(String id) {
        return ResourceKey.create(Registries.BLOCK, Helper.createLocation(id));
    }

}
