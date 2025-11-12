package com.jdolphin.ricksportalgun.common.item.types;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UpgradeType {

    private final String id;
    private final Component description;

    public UpgradeType(String id, Component desc) {
        this.id = id;
        this.description = desc;
    }
    public Component getName() {
        return Component.translatable("upgrade.ricksportalgun." + id);
    }

    public Component getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        List<String> types = new ArrayList<>(gunStack.getOrDefault(PGDataComponents.UPGRADE_LIST, List.of()));
        if (!types.contains(this.id)) {
            types.add(this.id);
            PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.upgrade"));
            gunStack.set(PGDataComponents.UPGRADE_LIST, types);
            return true;
        }
        PGHelper.sendFailMsg(player, Component.translatable("error.ricksportalgun.upgrade.already_applied"));
        return false;
    }
}