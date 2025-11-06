package com.jdolphin.ricksportalgun.common.item.upgrade.types;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class UpgradeType {
    private final String id;
    private final Component description;
    private final String tag;

    public UpgradeType(String id, Component desc, String tag) {
        this.id = id;
        this.description = desc;
        this.tag = tag;
    }
    public Component getName() {
        return Component.translatable("upgrade.ricksportalgun." + id);
    }

    public Component getDescription() {
        return description;
    }

    public String getUpgradeTag() {
        return this.tag;
    }

    public String getId() {
        return id;
    }

    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        CompoundTag tag = gunStack.getOrCreateTag();
        ListTag listTag = tag.getList(PGNbtKeys.TAG_UPGRADES, 8);
        if (listTag.add(StringTag.valueOf(this.tag))) {
            PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.upgrade"));
            tag.put(PGNbtKeys.TAG_UPGRADES, listTag);
            return true;
        }
        PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.upgrade.already_applied"));
        return false;
    };
}
