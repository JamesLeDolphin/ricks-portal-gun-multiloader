package com.jdolphin.ricksportalgun.common.item.upgrade;

import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CreativeUpgradeItem extends AbstractUpgradeItem {

    public CreativeUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult applyUpgrade(Player player, ItemStack stack, PortalGunItem item) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(PGNbtKeys.UPGRADE_PLAYER_LOC, true);
        tag.putBoolean(PGNbtKeys.UPGRADE_BIOME_LOC, true);
        tag.putBoolean(PGNbtKeys.UPGRADE_STRUCTURE_LOC, true);
        tag.putBoolean(PGNbtKeys.UPGRADE_WAYPOINT, true);
        tag.putBoolean(PGNbtKeys.EXTRA_DIM, true);
        tag.putBoolean(PGNbtKeys.EXTRA_DIM_2, true);
        tag.putBoolean(PGNbtKeys.SETTINGS, true);
        tag.putInt(PGNbtKeys.TAG_MAX_FUEL, 128);
        return InteractionResult.SUCCESS;
    }
}
