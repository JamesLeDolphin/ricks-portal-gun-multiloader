package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractBaseScreen extends Screen {
    public static ResourceLocation BG_LOCATION = PGHelper.id("textures/gui/pg_background.png");
    public static ResourceLocation BACK_BUTTON_TEXTURE = PGHelper.id("textures/gui/sprites/icon/arrow_back.png");

    protected AbstractBaseScreen(Component title) {
        super(title);
    }

    protected AbstractBaseScreen(String title) {
        this(Component.translatable(title));
    }

    public boolean isPauseScreen() {
        return false;
    }

    protected PortalGunStyle getStyle() {
        ItemStack stack = getItemStack();
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(PGNbtKeys.TAG_GUN_STYLE)) {
            CompoundTag styleTag = tag.getCompound(PGNbtKeys.TAG_GUN_STYLE);
            return PortalGunStyle.fromNBT(styleTag);
        }
        return PortalGunStyle.DEFAULT;
    }

    protected ItemStack getItemStack() {
        assert this.minecraft != null && minecraft.player != null;
        Player player = this.minecraft.player;
        InteractionHand hand = PGHelper.getPortalGunHand(player);
        return player.getItemInHand(hand);
    }
}
