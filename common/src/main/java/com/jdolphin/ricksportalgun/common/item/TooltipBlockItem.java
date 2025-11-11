package com.jdolphin.ricksportalgun.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipBlockItem extends BlockItem {
    private final Component component;
    public TooltipBlockItem(Block block, Properties properties, Component component) {
        super(block, properties);
        this.component = component;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltips, flag);
        if (Screen.hasShiftDown()) {
            String tooltip = component.getString();
            String[] split = tooltip.split("\n");
            for (String s : split) {
                tooltips.add(Component.literal(s).withStyle(ChatFormatting.GRAY));
            }
        }
        else tooltips.add(Component.translatable("tooltip.ricksportalgun.hold_shift").withStyle(ChatFormatting.GRAY));
    }
}
