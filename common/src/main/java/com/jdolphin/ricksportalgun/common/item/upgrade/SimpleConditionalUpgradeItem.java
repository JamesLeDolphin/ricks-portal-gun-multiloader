package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class SimpleConditionalUpgradeItem extends SimpleUpgradeItem {
    protected BiFunction<ItemStack, PortalGunItem, Boolean> condition;
    protected Component errorMsg = null;

    public SimpleConditionalUpgradeItem(Properties properties, BiConsumer<ItemStack, PortalGunItem> onApply, BiFunction<ItemStack, PortalGunItem, Boolean> condition) {
        super(properties, onApply);
        this.condition = condition;
    }

    public SimpleConditionalUpgradeItem(Properties properties, BiConsumer<ItemStack, PortalGunItem> onApply, BiFunction<ItemStack, PortalGunItem, Boolean> condition, Component errorMsg) {
        this(properties, onApply, condition);
        this.errorMsg = errorMsg;
    }

    public InteractionResult applyUpgrade(Player player, ItemStack stack, PortalGunItem item) {
        if (this.condition.apply(stack, item)) {
            super.applyUpgrade(player, stack, item);
            return InteractionResult.SUCCESS;
        } else if (errorMsg != null) {
            PGHelper.sendFailMsg(player, this.errorMsg.copy());
        }
        return InteractionResult.FAIL;
    }
}
