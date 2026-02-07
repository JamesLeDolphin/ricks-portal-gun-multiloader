package com.jdolphin.ricksportalgun.common.item;

import com.jdolphin.ricksportalgun.common.comp.immersive_portals.ImmersivePortalsHandler;
import com.jdolphin.ricksportalgun.common.comp.infinity.InfinityHandler;
import com.jdolphin.ricksportalgun.common.customization.shape.PortalShape;
import com.jdolphin.ricksportalgun.common.customization.type.PortalType;
import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import com.jdolphin.ricksportalgun.common.init.*;
import com.jdolphin.ricksportalgun.common.item.upgrade.UpgradeItem;
import com.jdolphin.ricksportalgun.common.item.upgrade.types.UpgradeType;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.jdolphin.ricksportalgun.common.init.PGNbtKeys.TAG_UPGRADES;

@SuppressWarnings("unused")
public class PortalGunItem extends Item implements IWaypointStorage, IPortalFluidItem {
    private final int tints;

    public PortalGunItem(Properties properties, int tints) {
        super(properties);
        this.tints = tints;
    }

    public int getTints() {
        return tints;
    }

    @Override
    public @Nullable ItemStack getRemainingStack() {
        return null;
    }

    public int getMaxFluid(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return getUpgrades(stack).contains(PGUpgradeTypes.MAX_FUEL.getUpgradeTag()) ? 128 : 64;
    }

    public static PortalShape getPortalShape(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(PGNbtKeys.PORTAL_SHAPE)) {
            String s = tag.getString(PGNbtKeys.PORTAL_SHAPE);
            ResourceLocation rl = new ResourceLocation(s);
            return PGPortalShapes.SHAPES.get(rl);
        } else return PGPortalShapes.SQUARE;
    }

    public static PortalType getPortalType(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(PGNbtKeys.PORTAL_TYPE)) {
            String s = tag.getString(PGNbtKeys.PORTAL_TYPE);
            ResourceLocation rl = new ResourceLocation(s);
            return PGPortalTypes.TYPES.get(rl);
        } else return PGPortalTypes.DEFAULT;
    }

    public void migrateDamage(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Damage")) {
            int fuel = getMaxFluid(stack) - stack.getDamageValue();
            tag.putInt(PGNbtKeys.TAG_FUEL, fuel);
            tag.remove("Damage");
        }
    }

    public boolean refuel(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        InteractionHand pgHand = PGHelper.getPortalGunHand(player);
        InteractionHand fluidHand = PGHelper.getOppositeHand(pgHand);
        ItemStack gunStack = player.getItemInHand(pgHand);
        ItemStack fluidStack = player.getItemInHand(fluidHand);
        if (getFluid(stack) < getMaxFluid(stack) && gunStack.is(PGTags.Items.PORTAL_GUNS)) {
            if (fluidStack.getItem() instanceof IPortalFluidItem fluidItem) {
                int gunFuel = getFluid(gunStack);
                int itemFuel = fluidItem.getFluid(fluidStack);
                int toGun = getMaxFluid(gunStack) - gunFuel;
                int transferred = Math.min(toGun, itemFuel);

                this.setAmount(gunStack, gunFuel + transferred);

                int remainder = itemFuel - transferred;
                if (remainder > 0) {
                    fluidItem.setAmount(fluidStack, remainder);
                } else {
                    fluidItem.empty(fluidStack, player, fluidHand);
                }
                tag.putBoolean(PGNbtKeys.TAG_BOOTLEG, fluidItem.isBootleg());
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
        if (!isAir(level, bPos)) {
            float height = !isAir(level, bPos.relative(dir.getOpposite()).below()) ? 0.5f : -0.5f;
            float x = dir.getAxis().equals(Direction.Axis.X) ? 0 : -0.5f;
            float z = dir.getAxis().equals(Direction.Axis.Z) ? 0 : -0.5f;
            loc = bPos.relative(dir).getCenter().add(new Vec3(dir.step().mul(-0.4f)).add(0, height, 0));

        } else {
            float x = dir.getAxis().equals(Direction.Axis.X) ? dir.getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 0.1f : -0.1f : 0;
            float z = dir.getAxis().equals(Direction.Axis.Z) ? dir.getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 0.1f : -0.1f : 0;
            loc = new Vec3(loc.x() + x, bPos.getY(), loc.z() + z);
        }

        return loc;
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluidMode, double distance) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vec31 = vec3.add(f6 * distance, f5 * distance, f7 * distance);
        return level.clip(new ClipContext(vec3, vec31, net.minecraft.world.level.ClipContext.Block.OUTLINE, fluidMode, player));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && player instanceof ServerPlayer) {
            migrateDamage(stack);
            migrateOldUpgrades(stack);
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.contains(PGNbtKeys.TAG_OWNER)) {
                tag.putUUID(PGNbtKeys.TAG_OWNER, player.getUUID());
            }
            if (PGHelper.canPlayerAccessGun(player, hand)) {
                ItemStack oppositeStack = player.getItemInHand(PGHelper.getOppositeHand(hand));
                BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

                if (!refuel(stack, player) && getFluid(stack) > 0) {
                    if (oppositeStack.getItem() instanceof UpgradeItem upgrade) {
                        InteractionResult result = upgrade.onApply(player, stack);
                        if (!result.equals(InteractionResult.FAIL) && !player.isCreative())
                            oppositeStack.shrink(1);
                        return new InteractionResultHolder<>(result, stack);

                    } else {
                        String dimension = getHopDimension(stack);
                        ResourceLocation dim = new ResourceLocation(dimension);
                        ResourceKey<Level> key = LevelHelper.getWorldKey(dim);
                        ServerLevel destinationLevel = LevelHelper.getServerWorld(level, key);

                        BlockPos destination = getHopCoords(stack, player.blockPosition());
                        Vec3 exitPortalPos = destination.above().getCenter();

                        if (PGHelper.checkTagBoolean(tag, PGNbtKeys.PROJECTILE_MODE)) {
                            //Manual target mode
                            BlockHitResult destinationRay = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY, 128);
                            destinationLevel = (ServerLevel) level;
                            destination = destinationRay.getBlockPos();
                            exitPortalPos = getLocation(level, destination, destinationRay.getDirection(), destinationRay.getLocation()).add(0, 1, 0);

                        } else if (destinationLevel == null && PGHelper.hasInfiniteDimensions()) {
                            ResourceKey<Level> resourceKey = InfinityHandler.getOrCreateResourceKey(((ServerLevel) level).getServer(), dimension);
                            destinationLevel = LevelHelper.getServerWorld(level, resourceKey);
                        }

                        Vec3 loc = hitResult.getLocation();
                        if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
                            Direction dir = hitResult.getDirection();
                            BlockPos bPos = hitResult.getBlockPos();

                            loc = getLocation(level, bPos, dir, loc);
                        }
                        Direction hitDir = hitResult.getDirection();
                        Direction playerDir = player.getDirection();

                        float size = tag.contains(PGNbtKeys.TAG_SIZE) ? tag.getFloat(PGNbtKeys.TAG_SIZE) : 1.0f;
                        int age = tag.contains(PGNbtKeys.TAG_AGE) ? tag.getInt(PGNbtKeys.TAG_AGE) : 10;

                        if (destinationLevel != null) {
                            if (LevelHelper.canPortalTo(destinationLevel, destination, stack) && LevelHelper.canPortalTo(((ServerLevel) level), hitResult.getBlockPos(), stack)) {
                                if (canBypassDragon(stack) || !(LevelHelper.endHasDragons((ServerLevel) level) || LevelHelper.endHasDragons(destinationLevel))) {
                                    //No errors: actually make the portal

                                    if (PGHelper.hasImmersivePortals() /*Check for portal type later*/) {
                                        return ImmersivePortalsHandler.spawnPortal(stack, level, loc, key, destination.getCenter(), size, playerDir, hitDir);
                                    } else {
                                        PortalEntity portal = new PortalEntity(level, loc, hitDir, playerDir, size);
                                        destinationLevel.getChunkSource().updateChunkForced(new ChunkPos(destination), true);
                                        PortalEntity exPortal = new PortalEntity(destinationLevel, exitPortalPos, hitDir, playerDir, size);

                                        boolean bootleg = tag.contains(PGNbtKeys.TAG_BOOTLEG) && tag.getBoolean(PGNbtKeys.TAG_BOOTLEG);
                                        PGHelper.doForEach(entity -> {
                                            entity.setLifetime(PGHelper.seconds(age));
                                            entity.setColor(getColor(stack));
                                            entity.setBootleg(bootleg);
                                            entity.setPortalType(getPortalType(stack));
                                            entity.setShape(getPortalShape(stack));
                                        }, portal, exPortal);

                                        exPortal.setLevelCallback(new EntityInLevelCallback() {
                                            @Override
                                            public void onMove() {}

                                            @Override
                                            public void onRemove(Entity.RemovalReason reason) {
                                                portal.remove(reason);
                                            }
                                        });
                                        portal.setLevelCallback(new EntityInLevelCallback() {
                                            @Override
                                            public void onMove() {}

                                            @Override
                                            public void onRemove(Entity.RemovalReason reason) {
                                                exPortal.remove(reason);
                                            }
                                        });

                                        if (stack.hasCustomHoverName()) {
                                            Component component = stack.getHoverName();
                                            String s = component.getString();
                                            PGHelper.doForEach(entity -> entity.setCustomName(Component.literal(s)), portal, exPortal);
                                        }
                                        portal.setHopLocation(dim, destination);
                                        exPortal.setHopLocation(level.dimension().location(), portal.blockPosition());

                                        if (!portal.isFlat()) {
                                            PGHelper.doForEach(entity -> entity.setYRot(player.getYRot()), portal, exPortal);
                                        }

                                        ServerLevel finalDestinationLevel = destinationLevel;
                                        destinationLevel.getServer().executeIfPossible(() -> finalDestinationLevel.addFreshEntity(exPortal));
                                        level.addFreshEntity(portal);

                                        player.awardStat(Stats.ITEM_USED.get(this));
                                        player.getCooldowns().addCooldown(this, 20 * 3);
                                        if (!player.isCreative()) lowerFuel(stack, 1);
                                    }
                                } else {
                                    //Target or Origin is End & dragon is alive
                                    PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.dragon");
                                    return InteractionResultHolder.fail(stack);
                                }
                            } else {
                                //Destination cant be portaled to
                                PGHelper.sendFailMsg(player, "error.ricksportalgun.destination.unreachable");
                                return InteractionResultHolder.fail(stack);
                            }
                        } else if (LevelHelper.isBlenderDestination(dimension)) {
                            PortalEntity portal = new PortalEntity(level, loc, hitDir, playerDir, size);
                            portal.setLifetime(PGHelper.seconds(age));

                            if (stack.hasCustomHoverName()) {
                                Component component = stack.getHoverName();
                                String s = component.getString();
                                portal.setCustomName(Component.literal(s));
                            }
                            portal.setHopLocation(dim, destination);
                            portal.setColor(getColor(stack));

                            boolean bootleg = tag.contains(PGNbtKeys.TAG_BOOTLEG) && tag.getBoolean(PGNbtKeys.TAG_BOOTLEG);
                            portal.setBootleg(bootleg);
                            portal.setPortalType(getPortalType(stack));
                            portal.setShape(getPortalShape(stack));
                            if (!portal.isFlat()) portal.setYRot(player.getYRot());
                            if (!player.isCreative()) lowerFuel(stack, 1);

                            level.addFreshEntity(portal);

                            player.awardStat(Stats.ITEM_USED.get(this));
                            player.getCooldowns().addCooldown(this, 20 * 3);
                            return InteractionResultHolder.success(stack);
                        } else {
                            PGHelper.sendFailMsg(player, "error.ricksportalgun.destination_not_found");
                            return InteractionResultHolder.fail(stack);
                        }
                    }
                }
            } else {
                //Player isnt allowed to open gun
                PGHelper.sendFailMsg(player, "error.ricksportalgun.security");
                return InteractionResultHolder.fail(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    public static boolean canBypassDragon(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return getUpgrades(stack).contains(PGUpgradeTypes.DIM_2.getUpgradeTag());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return getColor(stack);
    }

    public boolean isBarVisible(ItemStack stack) {
        return this.getFluid(stack) < this.getMaxFluid(stack);
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round(getFluid(stack) * 13.0F / getMaxFluid(stack));
    }

    private boolean isAir(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level pLevel, List<Component> tooltips, TooltipFlag pIsAdvanced) {
        CompoundTag tag = stack.getOrCreateTag();
        List<Waypoint> list = IWaypointStorage.getWaypoints(stack);
        if (!Screen.hasShiftDown()) {
        tooltips.add(Component.translatable("ricksportalgun.destination",
                getHopCoords(stack, null).getX(), getHopCoords(stack, null).getY(), getHopCoords(stack, null).getZ()).withStyle(ChatFormatting.GRAY));
        tooltips.add(Component.translatable("ricksportalgun.dimension", getHopDimension(stack))
                .withStyle(ChatFormatting.GRAY));
        tooltips.add(Component.translatable("tooltip.ricksportalgun.fluid", getFluid(stack), getMaxFluid(stack)).withStyle(ChatFormatting.GRAY));

        } else {
            tooltips.add(Component.translatable("tooltip.ricksportalgun.waypoints", list.size()).withStyle(ChatFormatting.GRAY));
            if (tag.contains(PGNbtKeys.PRIMARY_COLOR) || tag.contains(PGNbtKeys.SECONDARY_COLOR)) {
                tooltips.add(Component.translatable("item.dyed", list.size()).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            }
        }
    }

    public static int getColor(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(PGNbtKeys.TAG_COLOR)) {
            return getPortalType(stack).getDefaultColor();
        } else return tag.getInt(PGNbtKeys.TAG_COLOR);
    }

    public static void setHopLocation(ItemStack stack, String dimension, BlockPos pos) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(PGNbtKeys.TAG_DIMENSION, dimension);
        tag.put(PGNbtKeys.TAG_BPOS, NbtUtils.writeBlockPos(pos));
    }

    public static String getHopDimension(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(PGNbtKeys.TAG_DIMENSION) ? tag.getString(PGNbtKeys.TAG_DIMENSION) : Level.OVERWORLD.location().toString();
    }

    public static BlockPos getHopCoords(ItemStack stack, @Nullable BlockPos fallback) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(PGNbtKeys.TAG_BPOS)) {
            CompoundTag bpTag = tag.getCompound(PGNbtKeys.TAG_BPOS);
            return NbtUtils.readBlockPos(bpTag);
        } else return fallback == null ? BlockPos.ZERO : fallback;
    }

    public static List<String> getUpgrades(ItemStack stack) {
        return stack.getOrCreateTag().getList(TAG_UPGRADES, Tag.TAG_STRING).stream().map(Tag::getAsString).toList();
    }

    public static void addUpgrade(ItemStack stack, UpgradeType type) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listtag = tag.getList(TAG_UPGRADES, Tag.TAG_STRING);
        String upTag = type.getUpgradeTag();
        if (!upTag.isEmpty()) {
            listtag.add(StringTag.valueOf(upTag));
            tag.put(TAG_UPGRADES, listtag);
        }
    }

    public static void removeUpgrade(ItemStack stack, UpgradeType type) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listtag = tag.getList(TAG_UPGRADES, Tag.TAG_STRING);
        String upTag = type.getUpgradeTag();
        if (!upTag.isEmpty()) {
            listtag.remove(StringTag.valueOf(upTag));
            tag.put(TAG_UPGRADES, listtag);
        }
    }

    public static void migrateOldUpgrades(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listTag = tag.getList(TAG_UPGRADES, 8);
        if (tag.contains(PGNbtKeys.UPGRADE_PLAYER_LOC)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.PLAYER_LOC.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.UPGRADE_PLAYER_LOC);
        }
        if (tag.contains(PGNbtKeys.UPGRADE_WAYPOINT)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.WAYPOINTS.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.UPGRADE_WAYPOINT);
        }
        if (tag.contains(PGNbtKeys.UPGRADE_BIOME_LOC)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.BIOME_LOC.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.UPGRADE_BIOME_LOC);
        }
        if (tag.contains(PGNbtKeys.UPGRADE_STRUCTURE_LOC)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.STRUCTURE_LOC.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.UPGRADE_STRUCTURE_LOC);
        }
        if (tag.contains(PGNbtKeys.EXTRA_DIM)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.DIM_1.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.EXTRA_DIM);
        }
        if (tag.contains(PGNbtKeys.EXTRA_DIM_2)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.DIM_2.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.EXTRA_DIM_2);
        }
        if (tag.contains(PGNbtKeys.SETTINGS)) {
            StringTag st = StringTag.valueOf(PGUpgradeTypes.SETTINGS.getUpgradeTag());
            if (listTag.contains(st)) {
                listTag.add(st);
            }
            tag.remove(PGNbtKeys.SETTINGS);
        }
        tag.put(TAG_UPGRADES, listTag);
    }
}