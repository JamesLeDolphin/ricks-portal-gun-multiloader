package com.jdolphin.ricksportalgun.common.customization.type;

import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
import com.jdolphin.ricksportalgun.common.init.PGPortalShapes;
import com.jdolphin.ricksportalgun.common.init.PGSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;

public class PortalType {
    private final ResourceLocation id;

    public PortalType(ResourceLocation id) {
        this.id = id;
    }

    public Component getName() {
        return Component.translatable(String.format("portaltype.%s.%s", id.getNamespace(), id.getPath()));
    }

    public ResourceLocation getId() {
        return id;
    }

    public SoundEvent getOpenSound() {
        return PGSounds.PORTAL_SHOOT;
    }

    public PortalShape defaultShape() {
        return PGPortalShapes.SQUARE;
    }

    public SoundEvent getCloseSound() {
        return SoundEvents.EMPTY;
    }

    public int getDefaultColor() {
        return Color.GREEN.getRGB();
    }

    public boolean supportsShape() {
        return true;
    }
}
