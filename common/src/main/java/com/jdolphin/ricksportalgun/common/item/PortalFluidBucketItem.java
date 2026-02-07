package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.init.PGFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.awt.*;

public class PortalFluidBucketItem extends BucketItem implements IPortalFluidItem {

    public PortalFluidBucketItem(Properties properties) {
        super(PGFluids.PORTAL_FLUID.getA(), properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getFluid(stack) == getMaxFluid(stack)) {
            return super.use(level, player, hand);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level pLevel, java.util.List<net.minecraft.network.chat.Component> tooltips, TooltipFlag isAdvanced) {
        tooltips.add(net.minecraft.network.chat.Component.translatable("tooltip.ricksportalgun.fluid", getFluid(stack), getMaxFluid(stack)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.GREEN.getRGB();
    }

    public boolean isBarVisible(ItemStack stack) {
        return this.getFluid(stack) < this.getMaxFluid(stack);
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round(getFluid(stack) * 13.0F / getMaxFluid(stack));
    }

    @Override
    public int getMaxFluid(ItemStack stack) {
        return 64;
    }

    @Override
    public @Nullable ItemStack getRemainingStack() {
        return Items.BUCKET.getDefaultInstance();
    }
}
