package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PGTintHandler {
    public static Item[] TINTABLES = new Item[]{PGItems.PORTAL_GUN, PGItems.GOLDEN_PORTAL_GUN, PGItems.PRIME_PORTAL_GUN,
    PGItems.ALT_PORTAL_GUN, PGItems.FLINTLOCK_PORTAL_GUN, PGItems.FUTURISTIC_PORTAL_GUN, PGItems.JUNK_PORTAL_GUN,
    PGItems.PORTAL_GUN_MK_II, PGItems.SIDESTEP_PORTAL_GUN, PGItems.SLOPED_PORTAL_GUN, PGItems.SQUARE_PORTAL_GUN,
    PGItems.SYMMETRICAL_PORTAL_GUN};


    public static int tint(ItemStack stack, int index) {
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
            default -> {
                return  0;
            }
        }
    }
}
