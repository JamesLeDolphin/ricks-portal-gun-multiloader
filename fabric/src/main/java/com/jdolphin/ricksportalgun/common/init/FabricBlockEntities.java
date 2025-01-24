package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricBlockEntities {

    public static void register() {
        PGBlockEntities.GUN_WORKBENCH = register("portal_gun_workbench", GunWorkbenchBlockEntity::new, PGBlocks.GUN_WORKBENCH);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Helper.createLocation(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
