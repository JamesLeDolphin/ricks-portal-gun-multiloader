package com.jdolphin.ricksportalgun.client.render;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class PlayerForcefieldLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerModel<AbstractClientPlayer> model;
    private int tickMark = 0;

    public PlayerForcefieldLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
        model = new PlayerModel<>(modelSet.bakeLayer(ModelLayers.PLAYER), false);
        model.young = false;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.hurtTime == 10) tickMark = player.tickCount;
        if (player.tickCount < tickMark + 20) {
            stack.pushPose();
            stack.scale(1.2f, 1.2f, 1.2f);
            float f = (float)player.tickCount + partialTick;

            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.energySwirl(PGHelper.vanilla("textures/entity/creeper/creeper_armor.png"),
                    f * 0.01f % 1.0F, f * 0.01F % 1.0F));

            this.model.renderToBuffer(stack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, player.tickCount % 20f);
            stack.popPose();
        }
    }
}
