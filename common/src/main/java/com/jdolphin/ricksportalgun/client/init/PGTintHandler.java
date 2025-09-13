package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class PGTintHandler {
    public static Item[] TINTABLES = PGItems.PORTAL_GUNS.toArray(new PortalGunItem[0]);


    public static int tint(ItemStack stack, int index) {
        if (!PGConfigHelper.disablePortalColourTint()) {
            switch (index) {
                case 0 -> {
                    int i = stack.is(PGItems.GOLDEN_PORTAL_GUN) ? Color.YELLOW.getRGB() : PortalGunItem.getPrimaryDye(stack);
                    return GuiHelper.opaqueColor(i);
                }
                case 1 -> {
                    return GuiHelper.opaqueColor(PortalGunItem.getColor(stack));
                }
                case 2 -> {
                    return GuiHelper.opaqueColor(PortalGunItem.getSecondaryDye(stack));
                }
            }
        }
        return 0;
    }
}
