package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PGTintHandler {
    public static Item[] TINTABLES = PGItems.PORTAL_GUNS.toArray(new PortalGunItem[0]);


    public static int tint(ItemStack stack, int index) {
        if (!PGConfigHelper.disablePortalColourTint()) {
            switch (index) {
                case 0 -> {
                    return FastColor.ARGB32.opaque(PortalGunItem.getPrimaryDye(stack));
                }
                case 1 -> {
                    return FastColor.ARGB32.opaque(PortalGunItem.getColor(stack));
                }
                case 2 -> {
                    return FastColor.ARGB32.opaque(PortalGunItem.getSecondaryDye(stack));
                }
            }
        }
        return 0;
    }
}
