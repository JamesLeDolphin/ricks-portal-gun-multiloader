package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.block.GunWorkbenchBlock;
import com.jdolphin.ricksportalgun.common.block.PortalDispenserBlock;
import com.jdolphin.ricksportalgun.common.block.SubetherBarrierBlock;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PGBlocks {
    private static final Map<ResourceLocation, Block> ALL = new HashMap<>();

    public static final Block GUN_WORKBENCH = register("portal_gun_workbench", GunWorkbenchBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).sound(SoundType.COPPER).strength(1.5F, 6.0F));

    public static final Block PORTAL_DISPENSER = register("portal_dispenser", PortalDispenserBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY).sound(SoundType.STONE).strength(1.5F, 6.0F));

    public static final Block SUBETHER_BARRIER = register("subether_barrier", SubetherBarrierBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.STONE).strength(1.5F, 6.0F));

    private static Block register(String id, Function<BlockBehaviour.Properties, Block> func, BlockBehaviour.Properties properties) {
        Block block = func.apply(properties);
        ALL.put(PGHelper.id(id), block);
        return block;
    }

    public static void init(BiConsumer<Block, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static ResourceKey<Block> keyOf(String id) {
        return ResourceKey.create(Registries.BLOCK, PGHelper.id(id));
    }
}
