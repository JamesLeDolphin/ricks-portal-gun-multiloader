package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.PortalDispenserBlockEntity;
import com.jdolphin.ricksportalgun.common.blockentity.SubetherBarrierBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricBlockEntities {

    public static void register() {
        PGBlockEntities.GUN_WORKBENCH = register("portal_gun_workbench", GunWorkbenchBlockEntity::new, PGBlocks.GUN_WORKBENCH);
        PGBlockEntities.PORTAL_DISPENSER = register("portal_dispenser", PortalDispenserBlockEntity::new, PGBlocks.PORTAL_DISPENSER);
        PGBlockEntities.SUBETHER_BARRIER = register("subether_barrier", SubetherBarrierBlockEntity::new, PGBlocks.SUBETHER_BARRIER);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, PGHelper.createLocation(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
