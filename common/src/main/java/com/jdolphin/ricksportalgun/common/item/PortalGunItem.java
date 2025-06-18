package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.upgrade.UpgradeItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.PortalGunType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class PortalGunItem extends Item implements IWaypointStorage {

    public PortalGunItem(Properties properties) {
        super(properties);
    }

    public static int getMaxFuel(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.MAX_FUEL, 64);
    }

    public static int getFuel(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.FUEL, 64);
    }

    public static void lowerFuel(ItemStack stack, int amount) {
        int i = getFuel(stack);
        stack.set(PGDataComponents.FUEL, Math.max(0, i - amount));
    }

    public static void setPrimaryDye(ItemStack stack, int color) {
        stack.set(PGDataComponents.PRIMARY_DYE, color);
    }

    public static void setSecondaryDye(ItemStack stack, int color) {
        stack.set(PGDataComponents.SECONDARY_DYE, color);
    }

    public static int getPrimaryDye(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PRIMARY_DYE, 0);
    }

    public static int getSecondaryDye(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.SECONDARY_DYE, 0);
    }


    public static void setCode(ItemStack stack, String code) {
        stack.set(PGDataComponents.CODE, code);
    }

    public static String getCode(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.CODE, "");
    }

    public static void refillFuel(ItemStack stack) {
        stack.set(PGDataComponents.FUEL, getMaxFuel(stack));
    }

    public static void migrateDamage(ItemStack stack) {
        int fuel = getMaxFuel(stack) - stack.getOrDefault(DataComponents.DAMAGE, 0);
        stack.set(PGDataComponents.FUEL, fuel);
        stack.remove(DataComponents.DAMAGE);
    }

    public static boolean refuel(ItemStack stack, Player player) {
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

    public static void setPortalGunType(ItemStack stack, PortalGunType type) {
        stack.set(PGDataComponents.PORTAL_GUN_TYPE, type);
        stack.set(DataComponents.ITEM_MODEL, type.model());
        if (stack.getCustomName() == null) stack.set(DataComponents.ITEM_NAME, type.name());
        setDefaultColor(stack, type.color());
    }

    public static PortalGunType getPortalGunType(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_GUN_TYPE, PortalGunType.DEFAULT);
    }

    private Vec3 getLocation(Level level, BlockPos bPos, Direction dir, Vec3 loc) {
        Vec3 newLoc = loc;
        if (isAir(level, bPos.above()) && (dir == Direction.UP)) {
            newLoc = new Vec3(loc.x(), loc.y(), loc.z());
        }
        if (isAir(level, bPos.below()) && (dir == Direction.DOWN)) {
            newLoc = new Vec3(loc.x(), loc.y() - 0.2, loc.z());
        }

        switch (dir) {
            case NORTH -> {
                if (isAir(level, bPos.north())) {
                    newLoc = new Vec3(bPos.getX() + 0.5, bPos.getY(), bPos.getZ() - 0.5);
                } else newLoc = new Vec3(loc.x(), bPos.getY() - 1, loc.z());
            }
            case SOUTH -> {
                if (isAir(level, bPos.south())) {
                    newLoc = new Vec3(bPos.getX() + 0.5, bPos.getY(), bPos.getZ() + 1.5);
                } else newLoc = new Vec3(loc.x(), bPos.getY() - 1, loc.z());
            }
            case WEST -> {
                if (isAir(level, bPos.west())) {
                    newLoc = new Vec3(bPos.getX() - 0.5, bPos.getY(), bPos.getZ() + 0.5);
                } else newLoc = new Vec3(loc.x(), bPos.getY() - 1, loc.z());
            }
            case EAST -> {
                if (isAir(level, bPos.east())) {
                    newLoc = new Vec3(bPos.getX() + 1.5, bPos.getY(), bPos.getZ() + 0.5);
                } else newLoc = new Vec3(loc.x(), bPos.getY() - 1, loc.z());
            }
        }
        return newLoc;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();
        ItemStack offhandStack = player.getOffhandItem();
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            migrateDamage(stack);
            if ((stack.getOrDefault(PGDataComponents.LOCK, false) &&
                    stack.getOrDefault(PGDataComponents.OWNER, "").equals(player.getUUID().toString())) ||
                    !stack.getOrDefault(PGDataComponents.LOCK, false)) {

                if (offhandStack.getItem() instanceof UpgradeItem upgrade) {
                    InteractionResult result = upgrade.applyUpgrade(player, stack, this);
                    if (!player.isCreative()) offhandStack.shrink(1);
                    return result;
                }

                if (!refuel(stack, player) && getFuel(stack) > 0) {

                    Vec3 loc = hitResult.getLocation();
                    Vec3 newLoc = loc;
                    if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
                        Direction dir = hitResult.getDirection();
                        BlockPos bPos = hitResult.getBlockPos();

                        newLoc = getLocation(level, bPos, dir, loc);
                    }

                    Direction dir = hitResult.getDirection();
                    Direction facing = player.getDirection();
                    float size = stack.getOrDefault(PGDataComponents.PORTAL_SIZE, 1.0f);
                    int age = stack.getOrDefault(PGDataComponents.PORTAL_LIFETIME, 10);

                    PortalEntity portal = new PortalEntity(level, newLoc, dir, facing, size);
                    PortalEntity exPortal = new PortalEntity(level, new Vec3(getHopCoords(stack)), dir, facing, size);

                    ResourceKey<Level> key = LevelHelper.getWorldKey(stack.getOrDefault(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.location()));
                    ServerLevel serverlevel = LevelHelper.getServerWorld(level, key);

                    portal.setLifetime(age);
                    exPortal.setLifetime(age);

                    Component customName = stack.getCustomName();
                    if (customName != null) {
                        portal.setCustomName(customName);
                        exPortal.setCustomName(customName);
                    }

                    portal.setHopLocation(getHopDimension(stack), getHopCoords(stack));
                    exPortal.setHopLocation(level.dimension().location(), portal.blockPosition());

                    int color = getColor(stack);
                    portal.setColor(color);
                    exPortal.setColor(color);

                    boolean bootleg = stack.getOrDefault(PGDataComponents.BOOTLEG, false);
                    portal.setBootleg(bootleg);
                    exPortal.setBootleg(bootleg);

                    if (canBypassDragon(stack) || !(LevelHelper.endHasDragons((ServerLevel) level) || LevelHelper.endHasDragons(serverlevel))) {

                        if (LevelHelper.isBlenderDestination(getHopDimension(stack).toString())) {
                            level.addFreshEntity(portal);
                            if (!player.isCreative()) {
                                lowerFuel(stack, 1);
                                player.awardStat(Stats.ITEM_USED.get(this));
                                player.getCooldowns().addCooldown(stack, 20 * 3);
                            }
                        } else {
                            if (LevelHelper.canPortalTo(serverlevel, getHopCoords(stack), stack)) {
                                if (!portal.isFlat()) {
                                    portal.setYRot(player.getYRot());
                                    exPortal.setYRot(player.getYRot());
                                }
                                serverlevel.addFreshEntity(exPortal);
                                level.addFreshEntity(portal);

                                player.awardStat(Stats.ITEM_USED.get(this));
                                player.getCooldowns().addCooldown(stack, 20 * 3);
                                if (!player.isCreative()) {
                                    lowerFuel(stack, 1);
                                }
                            } else {
                                PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.unreachable");
                                return InteractionResult.FAIL;
                            }
                        }
                    } else PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.dragon");
                }
            }
            return InteractionResult.SUCCESS;
        } else return InteractionResult.FAIL;
    }

    public static boolean canBypassDragon(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.EXTRA_DIMENSIONS_2, false);
    }

    private boolean isAir(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext pContext, List<Component> toolTips, @NotNull TooltipFlag pTooltipFlag) {
        List<Waypoint> list = stack.getOrDefault(PGDataComponents.WAYPOINTS, List.of());
        if (!Screen.hasShiftDown()) {
        toolTips.add(Component.translatable("ricksportalgun.destination",
                getHopCoords(stack).getX(), getHopCoords(stack).getY(), getHopCoords(stack).getZ()).withStyle(ChatFormatting.GRAY));
        toolTips.add(Component.translatable("ricksportalgun.dimension", getHopDimension(stack).toString())
                .withStyle(ChatFormatting.GRAY));
        toolTips.add(Component.translatable("tooltip.ricksportalgun.fuel", getFuel(stack), getMaxFuel(stack)).withStyle(ChatFormatting.GRAY));

        } else {
            toolTips.add(Component.translatable("tooltip.ricksportalgun.waypoints", list.size()).withStyle(ChatFormatting.GRAY));
            if (stack.has(PGDataComponents.PRIMARY_DYE) || stack.has(PGDataComponents.SECONDARY_DYE)) {
                toolTips.add(Component.translatable("item.dyed", list.size()).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            }
        }
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return getColor(stack);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getFuel(stack) < getMaxFuel(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round((float)getFuel(stack) * 13.0F / (float)getMaxFuel(stack));
    }

    public static int getColor(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PORTAL_COLOUR, Color.GREEN.getRGB());
    }

    public static void setDefaultColor(ItemStack stack, int color) {
        stack.set(PGDataComponents.DEFAULT_PORTAL_COLOUR, color);
    }

    public static void setStyle(ItemStack stack, PortalGunStyle style) {
        stack.set(PGDataComponents.PORTAL_GUN_STYLE, style);
    }

    public static void setColor(ItemStack stack, int color) {
        stack.set(PGDataComponents.PORTAL_COLOUR, color);
    }

    public static void setHopLocation(ItemStack stack, ResourceLocation dimension, BlockPos pos) {
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