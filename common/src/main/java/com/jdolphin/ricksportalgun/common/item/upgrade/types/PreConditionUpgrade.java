package com.jdolphin.ricksportalgun.common.item.upgrade.types;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PreConditionUpgrade extends UpgradeType {
    private final String tagToCheck;

    public PreConditionUpgrade(String id, Component desc, String tagToAdd, String tagToCheck) {
        super(id, desc, tagToAdd);
        this.tagToCheck = tagToCheck;
    }

    public String getConditionTag() {
        return tagToCheck;
    }


    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        CompoundTag tag = gunStack.getOrCreateTag();
        ListTag tags = tag.getList(PGNbtKeys.TAG_UPGRADES, 8);
        if (tags.contains(StringTag.valueOf(tagToCheck))) {
            return super.applyUpgrade(player, gunStack);
        } else {
            PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.upgrade.needs_upgrade"));
            return false;
        }
    }
}
