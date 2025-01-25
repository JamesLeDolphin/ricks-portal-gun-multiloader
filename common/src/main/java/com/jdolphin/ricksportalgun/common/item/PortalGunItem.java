package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGEntities;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helpers.LevelHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;

public class PortalGunItem extends Item implements IWaypointStorage {
    public static final String TAG_ACIDIC = "Bootleg";
    public static final String TAG_COLOR = "Color";

    public PortalGunItem(Properties properties) {
        super(properties);
    }

    public static int getMaxFuel(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.MAX_FUEL, 16);
    }

    public static int getFuel(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.FUEL, 16);
    }

    public static void lowerFuel(ItemStack stack, int amount) {
        int i = getFuel(stack);
        stack.set(PGDataComponents.FUEL, Math.max(0, i - amount));
    }

    public static void refillFuel(ItemStack stack) {
        stack.set(PGDataComponents.FUEL, getMaxFuel(stack));
    }

    public static void migrateDamage(ItemStack stack) {
        int fuel = getMaxFuel(stack) - stack.getOrDefault(DataComponents.DAMAGE, 0);
        stack.set(PGDataComponents.FUEL, fuel);
        stack.remove(DataComponents.DAMAGE);
    }

    public boolean refuel(ItemStack stack, Player player) {
        ItemStack offhand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();
        if (getFuel(stack) < getMaxFuel(stack) && mainHand.is(PGTags.Items.PORTAL_GUNS)) {
            if (offhand.is(PGItems.PORTAL_FLUID)) {
                refillFuel(stack);
                offhand.shrink(1);
                stack.set(PGDataComponents.BOOTLEG, false);
                return true;
            }
            if (offhand.is(PGItems.BOOTLEG_PORTAL_FLUID)) {
                refillFuel(stack);
                stack.set(PGDataComponents.BOOTLEG, true);
                offhand.shrink(1);
                return true;
            }
        }
        return false;
    }

    public void setPortalGunType(ItemStack stack, PortalGunType type) {
        stack.set(PGDataComponents.PORTAL_GUN_TYPE, type);
        stack.set(DataComponents.ITEM_MODEL, type.model());
        stack.set(DataComponents.ITEM_NAME, type.name());
        setDefaultColor(stack, type.color().getRGB());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (!level.isClientSide() && player instanceof ServerPlayer) {
            migrateDamage(stack);
            if ((stack.getOrDefault(PGDataComponents.LOCK, false) &&
                    stack.getOrDefault(PGDataComponents.OWNER, "").equals(player.getUUID().toString())) ||
                    !stack.getOrDefault(PGDataComponents.LOCK, false)) {

                if (!refuel(stack, player) && getFuel(stack) > 0) {


                    PortalEntity portal = new PortalEntity(PGEntities.PORTAL, level);
                    PortalEntity exPortal = new PortalEntity(PGEntities.PORTAL, level);

                    exPortal.setPos(getHopCoords(stack).getX(), getHopCoords(stack).getY(), getHopCoords(stack).getZ());
                    Vec3 loc = hitResult.getLocation();

                    if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
                        Direction dir = hitResult.getDirection();
                        BlockPos bPos = hitResult.getBlockPos();

                        if (level.getBlockState(bPos.above()).is(Blocks.AIR) && (dir == Direction.UP)) {
                            portal.setPos(loc.x(), loc.y(), loc.z());
                        }
                        if (level.getBlockState(bPos.below()).is(Blocks.AIR) && (dir == Direction.DOWN)) {
                            portal.setPos(loc.x(), loc.y() - 2, loc.z());
                        }

                        //switch (dir) {
                        //    case NORTH -> {
                        //        if (isAir(level, bPos.north())) {
                        //            portal.setPos(bPos.getX() + 0.5, bPos.getY() - 1, bPos.getZ() - 0.5);
                        //        } else portal.setPos(loc.x(), bPos.getY() - 1, loc.z());
                        //    }
                        //    case SOUTH -> {
                        //        if (isAir(level, bPos.south())) {
                        //            portal.setPos(bPos.getX() + 0.5, bPos.getY() - 1, bPos.getZ() + 1.5);
                        //        } else portal.setPos(loc.x(), bPos.getY() - 1, loc.z());
                        //    }
                        //    case WEST -> {
                        //        if (isAir(level, bPos.west())) {
                        //            portal.setPos(bPos.getX() - 0.5, bPos.getY() - 1, bPos.getZ() + 0.5);
                        //        } else portal.setPos(loc.x(), bPos.getY() - 1, loc.z());
                        //    }
                        //    case EAST -> {
                        //        if (isAir(level, bPos.east())) {
                        //            portal.setPos(bPos.getX() + 1.5, bPos.getY() - 1, bPos.getZ() + 0.5);
                        //        } else portal.setPos(loc.x(), bPos.getY() - 1, loc.z());
                        //    }
                        //}
                    } else {
                        portal.setPos(loc.x(), loc.y() - 1, loc.z());
                    }

                    ResourceKey<Level> key = LevelHelper.getWorldKey(stack.getOrDefault(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.location()));
                    ServerLevel serverlevel = LevelHelper.getServerWorld(level, key);

                    portal.setHopLocation(getHopDimension(stack), getHopCoords(stack));
                    exPortal.setHopLocation(level.dimension().location(), new BlockPos((int) portal.getX(), (int) portal.getY(), (int) portal.getZ()));

                    portal.getOnPos();
                    exPortal.getOnPos();

                    portal.setColor(this.getColor(stack));
                    exPortal.setColor(this.getColor(stack));

                    if (stack.getOrDefault(PGDataComponents.BOOTLEG, false)) {
                        portal.setAcid(true);
                        exPortal.setAcid(true);
                    }

                    if (!stack.getOrDefault(PGDataComponents.BOOTLEG, false)) {
                        portal.setAcid(false);
                        exPortal.setAcid(false);
                    }

                    if (serverlevel != null) {
                        portal.setYRot(player.getYRot());
                        exPortal.setYRot(player.getYRot());
                        portal.setXRot(0);
                        exPortal.setXRot(0);
                        serverlevel.addFreshEntity(exPortal);
                        level.addFreshEntity(portal);

                        player.awardStat(Stats.ITEM_USED.get(this));
                        player.getCooldowns().addCooldown(stack, 20 * 3);
                        if (!player.getAbilities().instabuild) {
                            lowerFuel(stack, 1);
                        }
                    } else
                        ((ServerPlayer) player).sendSystemMessage(Component.translatable("notice.ricksportalgun.destination_not_found").withStyle(ChatFormatting.RED));


                }
            }
            return InteractionResult.SUCCESS;
        } else return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("item.ricksportalgun.portal_gun.tooltip.destination",
                getHopCoords(pStack).getX(), getHopCoords(pStack).getY(), getHopCoords(pStack).getZ()).withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.ricksportalgun.portal_gun.tooltip.dimension", getHopDimension(pStack).toString())
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return getColor(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getFuel(stack) < getMaxFuel(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float)getFuel(stack) * 13.0F / (float)getMaxFuel(stack));
    }

    public int getColor(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_COLOUR, Color.GREEN.getRGB());
    }

    public void setDefaultColor(ItemStack stack, int color) {
        stack.set(PGDataComponents.DEFAULT_COLOUR, color);
    }

    public void setColor(ItemStack stack, int color) {
        stack.set(PGDataComponents.PORTAL_COLOUR, color);
    }

    public  void setHopLocation(ItemStack stack, ResourceLocation dimension, BlockPos pos) {
        stack.set(PGDataComponents.PORTAL_DIM, dimension);
        stack.set(PGDataComponents.PORTAL_POS, pos);
    }

    public static ResourceLocation getHopDimension(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.location());
    }

    public static BlockPos getHopCoords(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_POS, BlockPos.ZERO);
    }

}