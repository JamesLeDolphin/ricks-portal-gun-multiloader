package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.item.types.UpgradeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        components.add(Component.translatable("tooltip.ricksportalgun.upgrade.tutorial").withStyle(ChatFormatting.GRAY));
        if (type.getDescription() != null) {
            if (Screen.hasShiftDown()) {
                components.add(type.getDescription());
            } else components.add(Component.translatable("tooltip.ricksportalgun.hold_shift").withStyle(ChatFormatting.GRAY));
        }
    }

    public InteractionResult onApply(Player player, ItemStack stack) {
        if (this.type.applyUpgrade(player, stack)) {
            return InteractionResult.SUCCESS;
        } else return InteractionResult.FAIL;
    }
}