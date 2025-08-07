package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourDynamicModel implements BakedModel {
    private final BakedModel base;
    private final ModelManager modelManager;
    private final Map<PortalGunType, BakedModel> cache = new HashMap<>();

    public YourDynamicModel(BakedModel base, ModelManager modelManager) {
        this.base = base;
        this.modelManager = modelManager;
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Override
            public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level,
                                      @Nullable LivingEntity entity, int seed) {
                PortalGunType variant = getVariantFromItem(stack);
                if (variant == null) return base;

                return cache.computeIfAbsent(variant, type -> {
                    ResourceLocation variantModelLoc = type.model();
                    return modelManager.getModel(ModelResourceLocation.inventory(variantModelLoc));
                });
            }
        };
    }

    private PortalGunType getVariantFromItem(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_GUN_TYPE, PortalGunType.DEFAULT);
    }

    @Override public boolean isCustomRenderer() { return base.isCustomRenderer(); }
    @Override public boolean usesBlockLight() { return base.usesBlockLight(); }
    @Override public boolean isGui3d() { return base.isGui3d(); }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return base.getQuads(blockState, direction, randomSource);
    }

    @Override public boolean useAmbientOcclusion() { return base.useAmbientOcclusion(); }
    @Override public ItemTransforms getTransforms() { return base.getTransforms(); }
    @Override public TextureAtlasSprite getParticleIcon() { return base.getParticleIcon(); }
}
