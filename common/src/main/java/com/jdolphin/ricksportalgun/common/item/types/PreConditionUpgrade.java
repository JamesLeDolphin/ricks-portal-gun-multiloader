package com.jdolphin.ricksportalgun.common.item.types;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PreConditionUpgrade extends UpgradeType {
    private final String tagToCheck;

    public PreConditionUpgrade(String id, Component desc, String tagToCheck) {
        super(id, desc);
        this.tagToCheck = tagToCheck;
    }

    public String getConditionTag() {
        return tagToCheck;
    }


    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        List<String> types = new ArrayList<>(gunStack.getOrDefault(PGDataComponents.UPGRADE_LIST, List.of()));
        if (types.contains(tagToCheck)) {
            return super.applyUpgrade(player, gunStack);
        } else {
            PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.upgrade.needs_upgrade"));
            return false;
        }
    }
}