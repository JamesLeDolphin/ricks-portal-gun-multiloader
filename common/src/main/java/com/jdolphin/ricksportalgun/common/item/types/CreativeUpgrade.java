package com.jdolphin.ricksportalgun.common.item.types;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGUpgradeTypes;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CreativeUpgrade extends UpgradeType {

    public CreativeUpgrade(String id, Component desc) {
        super(id, desc);
    }

    @Override
    public boolean applyUpgrade(Player player, ItemStack gunStack) {
        List<String> types = new ArrayList<>(gunStack.getOrDefault(PGDataComponents.UPGRADE_LIST, List.of()));
        PGUpgradeTypes.UPGRADE_TYPES.values().forEach(type -> {
            if (!(type instanceof CreativeUpgrade)) {
                if (type instanceof PreConditionUpgrade conditionUpgrade) {
                    if (!types.contains(conditionUpgrade.getId())) {
                        String condition = conditionUpgrade.getConditionTag();
                        if (!types.contains(condition)) {
                            types.add(condition);
                        }
                        types.add(conditionUpgrade.getId());
                    }
                } else {
                    if (!types.contains(type.getId())) {
                        types.add(type.getId());
                    }
                }
            }
        });
        gunStack.set(PGDataComponents.UPGRADE_LIST, types);
        PGHelper.sendSuccessMsg(player, Component.translatable("notice.ricksportalgun.upgrade"));
        return true;
    }
}