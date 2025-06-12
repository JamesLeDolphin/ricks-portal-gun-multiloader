package com.jdolphin.ricksportalgun.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class UpgradeItem extends Item {
    private final BiConsumer<ItemStack, PortalGunItem> consumer;

    public UpgradeItem(Properties properties, BiConsumer<ItemStack, PortalGunItem> consumer) {
        super(properties);
        this.consumer = consumer;
    }

    public void applyUpgrade(ItemStack stack, PortalGunItem item) {
        this.consumer.accept(stack, item);
    }
}
