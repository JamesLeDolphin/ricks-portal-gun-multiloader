package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.util.Waypoint;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DataCardItem extends Item implements IWaypointStorage {

    public DataCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level pLevel, List<Component> tooltipComponents, TooltipFlag pIsAdvanced) {
        List<Waypoint> list = IWaypointStorage.getWaypoints(stack);
        tooltipComponents.add(Component.translatable("tooltip.ricksportalgun.waypoints", list.size()).withStyle(ChatFormatting.DARK_GRAY));
    }

}
