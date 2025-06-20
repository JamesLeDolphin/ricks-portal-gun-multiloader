package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractUpgradeItem extends Item {

    public AbstractUpgradeItem(Properties properties) {
        super(properties);
    }

    public abstract InteractionResult applyUpgrade(Player player, ItemStack stack, PortalGunItem item);
}
