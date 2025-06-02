package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.util.UpgradeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UpgradeItem extends Item {
    private final UpgradeType type;
    public UpgradeItem(Properties properties, UpgradeType type) {
        super(properties);
        this.type = type;
    }

    public UpgradeType getUpgradeType() {
        return this.type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltips, flag);
        tooltips.add(Component.translatable("tooltip.ricksportalgun.upgrade").append(this.type.translationName));

    }
}
