package com.jdolphin.ricksportalgun.common.item.upgrade.types;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CreativeUpgrade extends UpgradeType {

    public CreativeUpgrade(String id, Component desc) {
        super(id, desc, "");
    }

    @Override
    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        CompoundTag tag = gunStack.getOrCreateTag();
        ListTag listTag = tag.getList(PGNbtKeys.TAG_UPGRADES, 8);
        PGUpgradeTypes.UPGRADE_TYPES.values().forEach(type -> {
            if (!(type instanceof CreativeUpgrade)) {
                if (type instanceof PreConditionUpgrade conditionUpgrade) {
                    if (!listTag.contains(StringTag.valueOf(conditionUpgrade.getUpgradeTag()))) {
                        String condition = conditionUpgrade.getConditionTag();
                        if (!listTag.contains(StringTag.valueOf(condition))) {
                            listTag.add(StringTag.valueOf(condition));
                        }
                        listTag.add(StringTag.valueOf(conditionUpgrade.getUpgradeTag()));
                    }
                } else {
                    if (!listTag.contains(StringTag.valueOf(type.getUpgradeTag()))) {
                        listTag.add(StringTag.valueOf(type.getUpgradeTag()));
                    }
                }
            }
        });
        tag.put(PGNbtKeys.TAG_UPGRADES, listTag);
        PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.upgrade"));
        return true;
    }
}
