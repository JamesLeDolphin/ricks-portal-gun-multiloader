package com.jdolphin.ricksportalgun.common.meeseeks.argument;

import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class ItemArgument extends AbstractMeeseeksArgument<Item> {

    public ItemArgument(String name) {
        super(name, Item.class);
    }

    @Override
    public Item fromString(String string) {
        if (isValid(string)) {
            ResourceLocation rl = ResourceLocation.tryParse(string);
            return BuiltInRegistries.ITEM.get(rl);
        }
        return null;
    }

    @Override
    public boolean isValid(String string) {
        ResourceLocation rl = ResourceLocation.tryParse(string);
        if (rl != null) {
            Item item = BuiltInRegistries.ITEM.get(rl);
            return !item.equals(Items.AIR);
        }
        return false;
    }

    @Override
    public List<String> values() {
        return BuiltInRegistries.ITEM.stream().map(item -> item.getDefaultInstance().getDisplayName().getString()).toList();
    }
}
