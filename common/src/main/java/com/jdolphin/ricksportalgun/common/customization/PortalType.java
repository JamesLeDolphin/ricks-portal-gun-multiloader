package com.jdolphin.ricksportalgun.common.customization;

import com.jdolphin.ricksportalgun.common.init.PGSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;

public class PortalType {
    private final String id;

    public PortalType(String id) {
        this.id = id;
    }

    public String getId() {
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

    public boolean canShapeChange() {
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
