package com.jdolphin.ricksportalgun.common.item.types;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ComponentUpgrade<T> extends UpgradeType {
    private final DataComponentType<T> component;
    private final T value;

    public  ComponentUpgrade(String id, Component desc, DataComponentType<T> component, T value) {
        super(id, desc);
        this.component = component;
        this.value = value;
    }

    @Override
    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        boolean bool = super.applyUpgrade(player, gunStack);
        if (bool) {
            gunStack.set(component, value);
        }
        return bool;
    }
}
