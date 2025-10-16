package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGNbtKeys;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.upgrade.AbstractUpgradeItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PortalGunItem extends Item implements IWaypointStorage, ItemColor {
    private final int tints;
    public PortalGunItem(Properties properties, int tints) {
        super(properties);
        this.tints = tints;
    }

    public int getTints() {
        return tints;
    }

    public static int getMaxFuel(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_MAX_FUEL) ? tag.getInt(PGNbtKeys.TAG_MAX_FUEL) : 64;
    }

    public static int getFuel(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_FUEL) ? tag.getInt(PGNbtKeys.TAG_FUEL) : getMaxFuel(stack);
    }

    public static void lowerFuel(ItemStack stack, int amount) {
        int i = getFuel(stack);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_FUEL, Math.max(0, i - amount));
    }

    public static void setPrimaryDye(ItemStack stack, int color) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.PRIMARY_COLOR, color);
    }

    public static void setSecondaryDye(ItemStack stack, int color) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.SECONDARY_COLOR, color);
    }

    public static int getPrimaryDye(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.PRIMARY_COLOR) ? tag.getInt(PGNbtKeys.PRIMARY_COLOR) : 15989755;
    }

    public static int getSecondaryDye(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.SECONDARY_COLOR) ? tag.getInt(PGNbtKeys.SECONDARY_COLOR) : 15989755;
    }


    public static void setCode(ItemStack stack, String code) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(PGNbtKeys.BARRIER_CODE, code);
    }

    public static String getCode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.BARRIER_CODE) ? tag.getString(PGNbtKeys.BARRIER_CODE) : "";
    }

    public static void refillFuel(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_FUEL, getMaxFuel(stack));
    }

    public static void migrateDamage(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        int fuel = getMaxFuel(stack) - stack.getDamageValue();
        tag.putInt(PGNbtKeys.TAG_FUEL, fuel);
        tag.remove("Damage");
    }

    public static boolean refuel(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        InteractionHand pgHand = PGHelper.getPortalGunHand(player);
        InteractionHand fluidHand = PGHelper.getOppositeHand(pgHand);
        ItemStack gunStack = player.getItemInHand(pgHand);
        ItemStack fluidStack = player.getItemInHand(fluidHand);
        if (getFuel(stack) < getMaxFuel(stack) && gunStack.is(PGTags.Items.PORTAL_GUNS)) {
            if (fluidStack.is(PGItems.PORTAL_FLUID)) {
                refillFuel(stack);
                fluidStack.shrink(1);
                tag.putBoolean(PGNbtKeys.TAG_BOOTLEG, false);
                return true;
            }
            if (fluidStack.is(PGItems.BOOTLEG_PORTAL_FLUID)) {
                refillFuel(stack);
                tag.putBoolean(PGNbtKeys.TAG_BOOTLEG, false);
                fluidStack.shrink(1);
                return true;
            }
        }
        return false;
    }

    private Vec3 getLocation(Level level, BlockPos bPos, Direction dir, Vec3 loc) {
        if (isAir(level, bPos.below()) && (dir == Direction.DOWN)) {
            return loc.add(0, -0.2, 0);
        }
        if (dir.equals(Direction.UP)) {
            return loc;
        }
        if (!isAir(level, bPos.relative(dir))) {
            switch (dir) {
                case NORTH -> {
                    Vec3 vec = bPos.north().getCenter();
                    loc = vec.add(0, 0, 0.4);

                }
                case SOUTH -> {
                    Vec3 vec = bPos.south().getCenter();
                    loc = vec.add(0, 0, -0.4);
                }
                case WEST -> {
                    Vec3 vec = bPos.west().getCenter();
                    loc = vec.add(0.4, 0, 0);
                }
                case EAST -> {
                    Vec3 vec = bPos.east().getCenter();
                    loc = vec.add(-0.4, 0, 0);
                }
            }
        } else {
            float x = dir.getAxis().equals(Direction.Axis.X) ? dir.getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 0.1f : -0.1f : 0;
            float z = dir.getAxis().equals(Direction.Axis.Z) ? dir.getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 0.1f : -0.1f : 0;
            loc = new Vec3(loc.x() + x, bPos.getY(), loc.z() + z);
        }

        return loc;
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack oppositeStack = player.getItemInHand(PGHelper.getOppositeHand(hand));
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        CompoundTag tag = stack.getOrCreateTag();
        if (!level.isClientSide() && player instanceof ServerPlayer) {
            migrateDamage(stack);
            if (PGHelper.canPlayerAccessGun(player, stack)) {
                if (oppositeStack.getItem() instanceof AbstractUpgradeItem upgrade) {
                    InteractionResult result = upgrade.applyUpgrade(player, stack, this);
                    if (!player.isCreative()) oppositeStack.shrink(1);
                    return InteractionResultHolder.success(stack);
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

                    float size = tag.contains(PGNbtKeys.TAG_SIZE) ? tag.getFloat(PGNbtKeys.TAG_SIZE) : 1.0f;
                    int age = tag.contains(PGNbtKeys.TAG_AGE) ? tag.getInt(PGNbtKeys.TAG_AGE) : PGHelper.seconds(10);

                    PortalEntity portal = new PortalEntity(level, newLoc, dir, facing, size);
                    PortalEntity exPortal = new PortalEntity(level, getHopCoords(stack).above().getCenter(), dir, facing, size);

                    ResourceLocation dim = tag.contains(PGNbtKeys.TAG_DIMENSION) ? new ResourceLocation(tag.getString(PGNbtKeys.TAG_DIMENSION)) : Level.OVERWORLD.location();
                    ResourceKey<Level> key = LevelHelper.getWorldKey(dim);
                    ServerLevel serverlevel = LevelHelper.getServerWorld(level, key);
                    serverlevel.getChunkSource().updateChunkForced(new ChunkPos(getHopCoords(stack)), true);
                    doForBoth(entity -> entity.setLifetime(age), portal, exPortal);

                    if (stack.hasCustomHoverName()) {
                        Component component = stack.getHoverName();
                        String s = component.getString();
                        doForBoth(entity -> entity.setCustomName(Component.literal(s)), portal, exPortal);
                    }
                    portal.setHopLocation(getHopDimension(stack), getHopCoords(stack));
                    exPortal.setHopLocation(level.dimension().location(), portal.blockPosition());

                    int color = getColor(stack);
                    doForBoth(entity -> entity.setColor(color), portal, exPortal);

                    boolean bootleg = tag.contains(PGNbtKeys.TAG_BOOTLEG) && tag.getBoolean(PGNbtKeys.TAG_BOOTLEG);
                    doForBoth(entity -> entity.setBootleg(bootleg), portal, exPortal);

                    if (LevelHelper.isBlenderDestination(getHopDimension(stack).toString())) {
                        if (!portal.isFlat()) {
                            portal.setYRot(player.getYRot());
                        }
                        level.addFreshEntity(portal);

                        return InteractionResultHolder.success(stack);
                    }
                    if (LevelHelper.canPortalTo(serverlevel, getHopCoords(stack), stack)) {
                        if (canBypassDragon(stack) || !(LevelHelper.endHasDragons((ServerLevel) level) || LevelHelper.endHasDragons(serverlevel))) {
                            if (!portal.isFlat()) {
                                doForBoth(entity -> entity.setYRot(player.getYRot()), portal, exPortal);
                            }

                            serverlevel.getServer().executeIfPossible(() -> serverlevel.addFreshEntity(exPortal));
                            level.addFreshEntity(portal);
                        } else {
                            PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.unreachable");
                            return InteractionResultHolder.fail(stack);
                        }
                    } else PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.dragon");
                    player.awardStat(Stats.ITEM_USED.get(this));
                    player.getCooldowns().addCooldown(this, 20 * 3);
                    if (!player.isCreative()) {
                        lowerFuel(stack, 1);
                    }
                }
            }
            return InteractionResultHolder.success(stack);
        } else return InteractionResultHolder.fail(stack);
    }

    private void doForBoth(Consumer<PortalEntity> consumer, PortalEntity a, PortalEntity b) {
        consumer.accept(a);
        consumer.accept(b);
    }

    public static boolean canBypassDragon(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.EXTRA_DIM_2) && tag.getBoolean(PGNbtKeys.EXTRA_DIM_2);
    }

    private boolean isAir(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level pLevel, List<Component> tooltips, TooltipFlag pIsAdvanced) {
        CompoundTag tag = stack.getOrCreateTag();
        List<String> list = IWaypointStorage.getWaypoints(stack);
        if (!Screen.hasShiftDown()) {
        tooltips.add(Component.translatable("ricksportalgun.destination",
                getHopCoords(stack).getX(), getHopCoords(stack).getY(), getHopCoords(stack).getZ()).withStyle(ChatFormatting.GRAY));
        tooltips.add(Component.translatable("ricksportalgun.dimension", getHopDimension(stack).toString())
                .withStyle(ChatFormatting.GRAY));
        tooltips.add(Component.translatable("tooltip.ricksportalgun.fuel", getFuel(stack), getMaxFuel(stack)).withStyle(ChatFormatting.GRAY));

        } else {
            tooltips.add(Component.translatable("tooltip.ricksportalgun.waypoints", list.size()).withStyle(ChatFormatting.GRAY));
            if (tag.contains(PGNbtKeys.PRIMARY_COLOR) || tag.contains(PGNbtKeys.SECONDARY_COLOR)) {
                tooltips.add(Component.translatable("item.dyed", list.size()).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            }
        }
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

    public static int getColor(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_COLOR) ? tag.getInt(PGNbtKeys.TAG_COLOR) : getDefaultColor(stack);
    }

    public static void setDefaultColor(ItemStack stack, int color) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_DEFAULT_COLOR, color);
    }

    public static int getDefaultColor(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_DEFAULT_COLOR) ? tag.getInt(PGNbtKeys.TAG_DEFAULT_COLOR) : Color.GREEN.getRGB();
    }

    public static void setStyle(ItemStack stack, PortalGunStyle style) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(PGNbtKeys.TAG_GUN_STYLE, style.toNBT());
    }

    public static void setColor(ItemStack stack, int color) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PGNbtKeys.TAG_COLOR, color);
    }

    public static void setHopLocation(ItemStack stack, ResourceLocation dimension, BlockPos pos) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(PGNbtKeys.TAG_DIMENSION, dimension.toString());
        tag.put(PGNbtKeys.TAG_BPOS, NbtUtils.writeBlockPos(pos));
    }

    public static ResourceLocation getHopDimension(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_DIMENSION) ? new ResourceLocation(tag.getString(PGNbtKeys.TAG_DIMENSION)) : Level.OVERWORLD.location();
    }

    public static BlockPos getHopCoords(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(PGNbtKeys.TAG_BPOS)) {
            CompoundTag bpTag = tag.getCompound(PGNbtKeys.TAG_BPOS);
            return NbtUtils.readBlockPos(bpTag);
        } else return BlockPos.ZERO;
    }

    @Override
    public int getColor(ItemStack itemStack, int i) {
        return getColor(itemStack);
    }
}