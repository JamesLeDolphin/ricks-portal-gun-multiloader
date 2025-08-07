package com.jdolphin.ricksportalgun.client;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DynamicItemOverride extends ItemOverrides {
    private final Map<PortalGunType, BakedModel> cachedModels = new HashMap<>();

    @Override
    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level,
                              @Nullable LivingEntity entity, int seed) {

        PortalGunType type = stack.get(PGDataComponents.PORTAL_GUN_TYPE);
        if (type == null) return originalModel;

        return cachedModels.computeIfAbsent(type, key -> {
            ResourceLocation modelLoc = PGHelper.createLocation("item/portal_guns/portal_gun.json");
            ModelResourceLocation mrl = ModelResourceLocation.inventory(modelLoc);
            return Minecraft.getInstance().getModelManager().getModel(mrl);
        });
    }
}
