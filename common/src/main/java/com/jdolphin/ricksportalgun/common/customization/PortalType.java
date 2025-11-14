package com.jdolphin.ricksportalgun.common.customization;

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
        return Component.translatable("portaltype.ricksportalgun." + id.getPath());
    }

    public ResourceLocation getId() {
        return id;
    }

    public SoundEvent getOpenSound() {
        return PGSounds.PORTAL_SHOOT;
    }

    public PortalShape defaultShape() {
        return PortalShape.SQUARE;
    }

    public SoundEvent getCloseSound() {
        return SoundEvents.EMPTY;
    }

    public int getDefaultColor() {
        return Color.GREEN.getRGB();
    }

    public boolean canChangeShape() {
        return true;
    }

    public enum PortalShape { //Use the strings later to get the premade vertex shape
        SQUARE("square"),
        DIAMOND("diamond"),
        CUSTOM("custom"),
        TRIANGLE("triangle"),
        OCTAGON("octagon")
        ;
        final String name;
        PortalShape(String name) {
            this.name = name;
        }

        public String getShapeName() {
            return name;
        }
    }
}
