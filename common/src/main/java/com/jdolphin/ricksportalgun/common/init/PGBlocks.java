package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.block.*;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

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

    public static final Block PORTAL_FLUID_TANK = register("portal_fluid_tank", PortalFluidStorageBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.STONE).noOcclusion().strength(1.5F, 6.0F));

    public static final Block PORTAL_FRAME = register("portal_frame", PortalFrameBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.STONE).noOcclusion().strength(1.5F, 6.0F));

    public static final Block PORTAL_CONTROLLER = register("portal_controller", PortalControllerBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.STONE).noOcclusion().strength(1.5F, 6.0F));

    public static final Block PORTAL_DIALER = register("portal_dialer", PortalDialerBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.STONE).noOcclusion().strength(1.5F, 6.0F));

    public static final Block PORTAL = register("portal", PortalBlock::new, BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL));

    public static final Block PORTAL_FLUID = register("portal_fluid", properties -> new PGLiquidBlock(PGServices.PLATFORM.getStillFluid("portal_fluid"), properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).replaceable().strength(100.0F).pushReaction(PushReaction.DESTROY)
                    .noLootTable().lightLevel(value -> 8).liquid().sound(SoundType.EMPTY));

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
}
