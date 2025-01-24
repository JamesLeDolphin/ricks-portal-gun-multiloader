package com.jdolphin.ricksportalgun.common.util.helpers;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Helper {
    public static ResourceLocation createLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID, string);
    }

    public static PortalGunItem getPortalGun(@NotNull ItemStack stack) {
        if (stack.is(PGTags.Items.PORTAL_GUNS)) {
            return (PortalGunItem) stack.getItem();
        }
        return null;
    }
}
