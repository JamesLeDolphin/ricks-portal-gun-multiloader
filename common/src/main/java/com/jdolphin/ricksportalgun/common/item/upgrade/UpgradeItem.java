package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);

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
