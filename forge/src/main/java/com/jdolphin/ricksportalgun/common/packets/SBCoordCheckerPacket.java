package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.common.init.PGTags;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import com.jdolphin.ricksportalgun.common.util.helpers.LevelHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Random;

public class SBCoordCheckerPacket {
    private String dimension;

    public SBCoordCheckerPacket(String dim) {
        this.dimension = dim;
    }

    public SBCoordCheckerPacket(FriendlyByteBuf buf) {
        this.dimension = buf.readUtf();
    }

    public BlockPos getRandoCoord(Level level) {
        WorldBorder border = level.getWorldBorder();
        Random rand = new Random();
        int i = 1000; //PortalGunCommonConfig.randomizer_max.get();
        int xCoord = rand.nextInt(-i < border.getMinX() ? (int) border.getMinX() : -i, i > border.getMaxX() ? (int) border.getMaxX() : i);
        int yCoord = rand.nextInt(level.getMinY() + 5, level.getMaxY());
        int zCoord = rand.nextInt(-i < border.getMinZ() ? (int) border.getMinZ() : -i, i > border.getMaxZ() ? (int) border.getMaxZ() : i);
        return new BlockPos(xCoord, yCoord + 1, zCoord);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.dimension);
    }

    public boolean handle(CustomPayloadEvent.Context context) {

        ServerPlayer player = context.getSender();
        assert player != null;

        player.connection.send(
                new ClientboundSetActionBarTextPacket(
                        Component.translatable("notice.ricksportalgun.randomizer_find_y.start")
                                .withStyle(ChatFormatting.YELLOW)));

        ServerLevel level = (ServerLevel) player.level();
        ItemStack stack = player.getMainHandItem();
        PortalGunItem item = Helper.getPortalGun(stack);
        BlockPos pos = getRandoCoord(level);

        try {
            ChunkAccess chunk = level.getChunk(pos);
            ForgeChunkManager.forceChunk(level, Constants.MODID, player, chunk.getPos().getRegionX(), chunk.getPos().getRegionZ(), true, false);

            int y = pos.getY();
            int height = level.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ());
            int worldCenter = ((level.getMinY() + 2) + height) / 2;

            int direction = y > worldCenter ? -1 : 1;

            while (y >= level.getMinY() + 2 && y <= level.getMaxY()) {
                BlockPos pos1 = new BlockPos(pos.getX(), y, pos.getZ());

                BlockState blockState = level.getBlockState(pos1);
                BlockState belowState = level.getBlockState(pos1.below());
                BlockState aboveState = level.getBlockState(pos1.above());

                if (belowState.is(Blocks.AIR)
                        || aboveState.is(PGTags.Blocks.RANDOMIZER_AVOID)
                        || blockState.isSuffocating(level, pos1)){

                    y += direction;

                } else break;
            }

            pos = new BlockPos(pos.getX(), y, pos.getZ());

            BlockState blockState = level.getBlockState(pos);
            if (blockState.isSuffocating(level, pos)
                    || blockState.is(PGTags.Blocks.RANDOMIZER_AVOID)
                    || y <= level.getMinY() + 2 || y >= level.getMaxY()) {
                player.connection.send(
                        new ClientboundSetActionBarTextPacket(
                                Component.translatable("notice.ricksportalgun.randomizer_find_y.fail")
                                        .withStyle(ChatFormatting.RED)));

                return false;
            }

            assert item != null;

            item.setHopLocation(stack, LevelHelper.getPlayerDimensionLocation(player), pos);

            player.connection.send(
                    new ClientboundSetActionBarTextPacket(
                            Component.translatable("notice.ricksportalgun.randomizer_find_y.success")
                                    .withStyle(ChatFormatting.GREEN)));

            ForgeChunkManager.forceChunk(level, Constants.MODID, player, chunk.getPos().getRegionX(), chunk.getPos().getRegionZ(), false, false);
            return true;
        } catch (NullPointerException err) {
            err.printStackTrace();
            return false;
        }
    }
}