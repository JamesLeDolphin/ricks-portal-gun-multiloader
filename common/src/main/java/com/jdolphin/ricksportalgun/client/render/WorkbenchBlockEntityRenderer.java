package com.jdolphin.ricksportalgun.client.render;

import com.jdolphin.ricksportalgun.common.blockentity.GunWorkbenchBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WorkbenchBlockEntityRenderer implements BlockEntityRenderer<GunWorkbenchBlockEntity> {

    public WorkbenchBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(GunWorkbenchBlockEntity workbench, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        NonNullList<ItemStack> stacks = workbench.getItems();
        ClientLevel level = (ClientLevel) workbench.getLevel();
        if (level != null) {
            BlockEntity be = level.getBlockEntity(workbench.getBlockPos());
            if (be instanceof GunWorkbenchBlockEntity blockEntity) {
                System.out.println(blockEntity.getItems());
            }
        }
    }
}
