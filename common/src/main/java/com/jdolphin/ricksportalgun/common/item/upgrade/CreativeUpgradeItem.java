package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DamageResistant;

public class CreativeUpgradeItem extends AbstractUpgradeItem {

    public CreativeUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult applyUpgrade(Player player, ItemStack stack, PortalGunItem item) {
        stack.set(PGDataComponents.HAS_WAYPOINTS, true);
        stack.set(PGDataComponents.EXTRA_DIMENSIONS, true);
        stack.set(PGDataComponents.EXTRA_DIMENSIONS_2, true);
        stack.set(PGDataComponents.SETTINGS, true);
        stack.set(PGDataComponents.BIOME_LOC, true);
        stack.set(PGDataComponents.PLAYER_LOC, true);
        stack.set(PGDataComponents.STRUCTURE_LOC, true);
        stack.set(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_FIRE));
        return InteractionResult.SUCCESS;
    }
}
