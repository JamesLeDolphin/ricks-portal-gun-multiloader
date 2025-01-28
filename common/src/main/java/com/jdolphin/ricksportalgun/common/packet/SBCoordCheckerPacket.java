package com.jdolphin.ricksportalgun.common.packet;

import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;

public record SBCoordCheckerPacket(String dim) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, SBCoordCheckerPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SBCoordCheckerPacket::dim, SBCoordCheckerPacket::new);
    public static final Type<SBCoordCheckerPacket> ID = new Type<>(Helper.createLocation("coord_check"));

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.dim);
    }

    public SBCoordCheckerPacket(String dim) {
        this.dim = dim;
    }

    public SBCoordCheckerPacket(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    private BlockPos getRandoCoord(ServerLevel level) {
        WorldBorder border = level.getWorldBorder();
        int i = 1000; //level.getGameRules().getInt(PGGamerules.RANDOMISER_CAP);
        RandomSource rand = level.getRandom();
        int xCoord = Mth.nextInt(rand, (int) Math.max(border.getCenterX(), -i), (int) Math.min(border.getMinX(), i));
        int yCoord = Mth.nextInt(rand,5, level.getHeight());
        int zCoord = Mth.nextInt(rand,(int) Math.max(border.getMaxZ(), -i), (int) Math.min(border.getMinZ(), i));
        return new BlockPos(xCoord, yCoord + 1, zCoord);
    }

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;

        BlockPos bPos = getRandoCoord(player.serverLevel());
        ResourceLocation dim = ResourceLocation.parse(this.dim);

        player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.start").withStyle(ChatFormatting.YELLOW));

        ServerLevel level;
        level = server.getLevel(ResourceKey.create(Registries.DIMENSION, dim));
        if (level == null) level = player.serverLevel();
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);

            ChunkAccess chunk = level.getChunk(bPos);
            level.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);

            int y = bPos.getY();
            int height = level.getHeight();
            int worldCenter = ((level.getMinY() + 2) + height) / 2;

            int direction = y > worldCenter ? -1 : 1;

            while (y >= level.getMinY() + 2 && y <= level.getHeight()) {
                BlockPos pos1 = new BlockPos(bPos.getX(), y, bPos.getZ());

                BlockState blockState = level.getBlockState(pos1);
                BlockState belowState = level.getBlockState(pos1.below());
                BlockState aboveState = level.getBlockState(pos1.above());

                if (belowState.isAir()
                        || aboveState.is(PGTags.Blocks.RANDOMIZER_AVOID)
                        || blockState.isSuffocating(level, pos1)) {

                    y += direction;

                } else break;
            }

        bPos = new BlockPos(bPos.getX(), y, bPos.getZ());

            BlockState blockState = level.getBlockState(bPos);
            if (blockState.isSuffocating(level, bPos)
                    || blockState.is(PGTags.Blocks.RANDOMIZER_AVOID)
                    || y <= level.getMinY() + 2 || y >= level.getHeight()) {
                player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.fail").withStyle(ChatFormatting.RED));
                return;
            }

            assert item != null;

            item.setHopLocation(stack, level.dimension().location(), bPos);

            player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.success").withStyle(ChatFormatting.GREEN));

        level.setChunkForced(chunk.getPos().x, chunk.getPos().z, false);
        }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
