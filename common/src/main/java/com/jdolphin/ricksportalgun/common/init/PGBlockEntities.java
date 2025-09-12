package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.PGServices;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PGBlockEntities {
    private static final Map<ResourceLocation, BlockEntityType<?>> ALL = new HashMap<>();
    public static BlockEntityType<GunWorkbenchBlockEntity> GUN_WORKBENCH = register("portal_gun_workbench", GunWorkbenchBlockEntity::new, PGBlocks.GUN_WORKBENCH);
    public static BlockEntityType<PortalDispenserBlockEntity> PORTAL_DISPENSER = register("portal_dispenser", PortalDispenserBlockEntity::new, PGBlocks.PORTAL_DISPENSER);
    public static BlockEntityType<SubetherBarrierBlockEntity> SUBETHER_BARRIER = register("subether_barrier", SubetherBarrierBlockEntity::new, PGBlocks.SUBETHER_BARRIER);

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        var bet = PGServices.PLATFORM.createBlockEntityType(func, blocks);
        ALL.put(PGHelper.id(id), bet);
        return bet;
    }

    public static void init(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
}
