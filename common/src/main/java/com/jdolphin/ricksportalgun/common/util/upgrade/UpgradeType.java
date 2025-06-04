package com.jdolphin.ricksportalgun.common.util.upgrade;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public abstract class UpgradeType {
   public final MutableComponent translationName;

   public UpgradeType(MutableComponent translationName) {
       this.translationName = translationName;
   }

    public abstract void applyUpgrade(ItemStack portalGunStack, PortalGunItem portalGun);

}
