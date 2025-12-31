package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class PGTintHandler {
    public static Item[] TINTABLES = PGItems.PORTAL_GUNS.toArray(new PortalGunItem[0]);


    public static int tint(ItemStack stack, int index) {
        if (!PGConfigHelper.disablePortalColourTint()) {
            switch (index) {
                case 0 -> {
                    if (!stack.is(PGItems.GOLDEN_PORTAL_GUN)) {
                        CompoundTag tag = stack.getOrCreateTag();
                        int i = tag.contains(PGNbtKeys.PRIMARY_COLOR) ? tag.getInt(PGNbtKeys.PRIMARY_COLOR) : 15989755;
                        return GuiHelper.opaqueColor(i);
                    }
                    return Color.YELLOW.getRGB();
                }
                case 1 -> {
                    return GuiHelper.opaqueColor(PortalGunItem.getColor(stack));
                }
                case 2 -> {
                    CompoundTag tag = stack.getOrCreateTag();
                    int i = tag.contains(PGNbtKeys.SECONDARY_COLOR) ? tag.getInt(PGNbtKeys.SECONDARY_COLOR) : 15989755;
                    return GuiHelper.opaqueColor(i);
                }
            }
        }
        return 0;
    }
}
