package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.PGDataComponents;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.upgrade.AbstractUpgradeItem;
import com.jdolphin.ricksportalgun.common.util.PortalGunStyle;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PortalGunItem extends Item implements IWaypointStorage {
    private final int tints;
    public PortalGunItem(Properties properties, int tints) {
        super(properties);
        this.tints = tints;
    }

    public int getTints() {
        return tints;
    }

    public static int getMaxFuel(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.MAX_FUEL, 64);
    }

    public static int getFuel(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.FUEL, getMaxFuel(stack));
    }

    public void lowerFuel(ItemStack stack, int amount) {
        int i = getFuel(stack);
        int fuel = Math.max(0, i - amount);
        stack.set(PGDataComponents.FUEL, fuel);
    }

    public static void setPrimaryDye(ItemStack stack, int color) {
        stack.set(PGDataComponents.PRIMARY_DYE, color);
    }

    public static void setSecondaryDye(ItemStack stack, int color) {
        stack.set(PGDataComponents.SECONDARY_DYE, color);
    }

    public static int getPrimaryDye(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.PRIMARY_DYE, 15989755);
    }

    public static int getSecondaryDye(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.SECONDARY_DYE, 15989755);
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
        if (stack.has(DataComponents.DAMAGE)) {
            int fuel = getMaxFuel(stack) - stack.getOrDefault(DataComponents.DAMAGE, 0);
            stack.set(PGDataComponents.FUEL, fuel);
            stack.remove(DataComponents.DAMAGE);
        }
    }

    public static boolean refuel(ItemStack stack, Player player) {
        InteractionHand pgHand = PGHelper.getPortalGunHand(player);
        InteractionHand fluidHand = PGHelper.getOppositeHand(pgHand);
        ItemStack gunStack = player.getItemInHand(pgHand);
        ItemStack fluidStack = player.getItemInHand(fluidHand);
        if (getFuel(stack) < getMaxFuel(stack) && gunStack.is(PGTags.Items.PORTAL_GUNS)) {
            if (fluidStack.is(PGItems.PORTAL_FLUID)) {
                refillFuel(stack);
                fluidStack.shrink(1);
                stack.set(PGDataComponents.BOOTLEG, false);
                return true;
            }
            if (fluidStack.is(PGItems.BOOTLEG_PORTAL_FLUID)) {
                refillFuel(stack);
                stack.set(PGDataComponents.BOOTLEG, true);
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
                    Vec3 vec = bPos.north().getBottomCenter();
                    loc = vec.add(0, 0, 0.4);

                }
                case SOUTH -> {
                    Vec3 vec = bPos.south().getBottomCenter();
                    loc = vec.add(0, 0, -0.4);
                }
                case WEST -> {
                    Vec3 vec = bPos.west().getBottomCenter();
                    loc = vec.add(0.4, 0, 0);
                }
                case EAST -> {
                    Vec3 vec = bPos.east().getBottomCenter();
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

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer) {
            migrateDamage(stack);
            if (PGHelper.canPlayerAccessGun(player, stack)) {
                ItemStack oppositeStack = player.getItemInHand(PGHelper.getOppositeHand(hand));
                BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

                if (!refuel(stack, player) && getFuel(stack) > 0) {
                    if (oppositeStack.getItem() instanceof AbstractUpgradeItem upgrade) {
                        InteractionResult result = upgrade.applyUpgrade(player, stack, this);
                        if (!result.equals(InteractionResult.FAIL)) {
                            if (!player.isCreative()) oppositeStack.shrink(1);
                        }
                        return new InteractionResultHolder<>(result, stack);
                    } else {
                        ResourceLocation dim = stack.getOrDefault(PGDataComponents.PORTAL_DIM, Level.OVERWORLD.location());
                        ResourceKey<Level> key = LevelHelper.getWorldKey(dim);
                        ServerLevel serverlevel = LevelHelper.getServerWorld(level, key);
                        BlockPos destination = getHopCoords(stack);

                        Vec3 loc = hitResult.getLocation();
                        if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
                            Direction dir = hitResult.getDirection();
                            BlockPos bPos = hitResult.getBlockPos();

                            loc = getLocation(level, bPos, dir, loc);
                        }
                        Direction hitDir = hitResult.getDirection();
                        Direction playerDir = player.getDirection();

                        float size = stack.getOrDefault(PGDataComponents.PORTAL_SIZE, 1.0f);
                        int age = stack.getOrDefault(PGDataComponents.PORTAL_LIFETIME, 10);
                        if (serverlevel != null) {
                            if (LevelHelper.canPortalTo(serverlevel, destination, stack) && LevelHelper.canPortalTo(((ServerLevel) level), hitResult.getBlockPos(), stack)) {
                                if (canBypassDragon(stack) || !(LevelHelper.endHasDragons((ServerLevel) level) || LevelHelper.endHasDragons(serverlevel))) {
                                    //No errors: actually make the portal
                                    PortalEntity portal = new PortalEntity(level, loc, hitDir, playerDir, size);
                                    serverlevel.getChunkSource().updateChunkForced(new ChunkPos(destination), true);
                                    PortalEntity exPortal = new PortalEntity(serverlevel, destination.above().getCenter(), hitDir, playerDir, size);

                                    boolean bootleg = stack.getOrDefault(PGDataComponents.BOOTLEG, false);
                                    doForBoth(entity -> {
                                        entity.setLifetime(PGHelper.seconds(Math.max(age, 5)));
                                        entity.setColor(getColor(stack));
                                        entity.setBootleg(bootleg);
                                    }, portal, exPortal);

                                    if (stack.has(DataComponents.CUSTOM_NAME)) {
                                        Component component = stack.getHoverName();
                                        String s = component.getString();
                                        doForBoth(entity -> entity.setCustomName(Component.literal(s)), portal, exPortal);
                                    }
                                    portal.setHopLocation(dim, destination);
                                    exPortal.setHopLocation(level.dimension().location(), portal.blockPosition());

                                    if (!portal.isFlat()) doForBoth(entity -> entity.setYRot(player.getYRot()), portal, exPortal);

                                    serverlevel.getServer().executeIfPossible(() -> serverlevel.addFreshEntity(exPortal));
                                    level.addFreshEntity(portal);

                                    player.awardStat(Stats.ITEM_USED.get(this));
                                    player.getCooldowns().addCooldown(this, PGHelper.seconds(3));
                                    if (!player.isCreative()) lowerFuel(stack, 1);
                                } else {
                                    //Target or Origin is end & dragon is alive
                                    PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.dragon");
                                    return InteractionResultHolder.fail(stack);
                                }
                            } else {
                                //Destination cant be portaled to
                                PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.unreachable");
                                return InteractionResultHolder.fail(stack);
                            }
                        } else if (LevelHelper.isBlenderDestination(dim.toString())) {
                            PortalEntity portal = new PortalEntity(level, loc, hitDir, playerDir, size);
                            portal.setLifetime(PGHelper.seconds(age));

                            if (stack.has(DataComponents.CUSTOM_NAME)) {
                                Component component = stack.getHoverName();
                                String s = component.getString();
                                portal.setCustomName(Component.literal(s));
                            }
                            portal.setHopLocation(dim, destination);
                            portal.setColor(getColor(stack));

                            boolean bootleg = stack.getOrDefault(PGDataComponents.BOOTLEG, false);
                            portal.setBootleg(bootleg);

                            if (!portal.isFlat()) portal.setYRot(player.getYRot());
                            if (!player.isCreative()) lowerFuel(stack, 1);

                            level.addFreshEntity(portal);

                            player.awardStat(Stats.ITEM_USED.get(this));
                            player.getCooldowns().addCooldown(this, PGHelper.seconds(3));
                            return InteractionResultHolder.success(stack);
                        }
                    }
                }
            } else {
                //Player isnt allowed to open gun
                PGHelper.sendFailMsg(player, "error.ricksportalgun.security");
                return InteractionResultHolder.fail(stack);
            }
        } return InteractionResultHolder.pass(stack);
    }

    private void doForBoth(Consumer<PortalEntity> consumer, PortalEntity a, PortalEntity b) {
        consumer.accept(a);
        consumer.accept(b);
    }

    public static boolean canBypassDragon(ItemStack stack) {
        return stack.getOrDefault(PGDataComponents.EXTRA_DIMENSIONS_2, false);
    }

    private boolean isAir(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> toolTips, TooltipFlag pTooltipFlag) {
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

    public static void migrateNBT(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            String dim = "PortalDimension";
            String fuel = "Fuel";
            String pos = "PortalPos";
            String bootleg = "Bootleg";
            String color = "Color";
            String waypoints = "Waypoints";
            String lock = "Lock";
            String owner = "Owner";
            String defaultColor = "DefaultColor";

            CompoundTag tag = data.copyTag();
            if (tag.contains(dim)) {
                String dimension = tag.getString(dim);
                ResourceLocation rl = ResourceLocation.parse(dimension);
                stack.set(PGDataComponents.PORTAL_DIM, rl);
                tag.remove(dim);
            }
            if (tag.contains(fuel)) {
                int f = tag.getInt(fuel);
                stack.set(PGDataComponents.FUEL, f);
                tag.remove(fuel);
            }
            if (tag.contains(pos)) {
                @SuppressWarnings("OptionalGetWithoutIsPresent") BlockPos blockPos = NbtUtils.readBlockPos(tag, pos).get();
                stack.set(PGDataComponents.PORTAL_POS, blockPos);
                tag.remove(pos);
            }
            if (tag.contains(bootleg)) {
                boolean acid = tag.getBoolean(bootleg);
                stack.set(PGDataComponents.BOOTLEG, acid);
                tag.remove(bootleg);
            }
            if (tag.contains(color)) {
                int colour = tag.getInt(color);
                stack.set(PGDataComponents.PORTAL_COLOUR, colour);
                tag.remove(color);
            }
            if (tag.contains(waypoints)) {
                ListTag listTag = tag.getList(waypoints, Tag.TAG_STRING);
                List<Waypoint> waypointList = listTag.stream().map(Tag::getAsString).map(Waypoint::getWaypoint).toList();
                stack.set(PGDataComponents.WAYPOINTS, waypointList);
                tag.remove(waypoints);
            }
            if (tag.contains(lock)) {
                boolean locked = tag.getBoolean(lock);
                stack.set(PGDataComponents.LOCK, locked);
                tag.remove(lock);
            }
            if (tag.contains(owner)) {
                String own = tag.getString(owner);
                stack.set(PGDataComponents.OWNER, own);
                tag.remove(owner);
            }
            if (tag.contains(defaultColor)) {
                int defColour = tag.getInt(defaultColor);
                stack.set(PGDataComponents.DEFAULT_PORTAL_COLOUR, defColour);
                tag.remove(defaultColor);
            }
        }
    }
}