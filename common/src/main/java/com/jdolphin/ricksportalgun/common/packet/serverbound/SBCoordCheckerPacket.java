package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helper.LevelHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGConfigHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

public record SBCoordCheckerPacket(BlockPos pos, String dim) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        server.executeIfPossible(() -> {
            player.displayClientMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.start").withStyle(ChatFormatting.YELLOW), false);
            player.serverLevel().getChunkSource().updateChunkForced(new ChunkPos(pos), true);
            BlockPos bPos = LevelHelper.getSafePos(LevelHelper.getSafeRandomCoords(pos, player.serverLevel(), 100, PGConfigHelper.getRandomizerMax()), player.serverLevel());

            if (bPos != null) {
                ResourceLocation dim = new ResourceLocation(this.dim);
                ServerLevel level;
                level = server.getLevel(ResourceKey.create(Registries.DIMENSION, dim));
                if (level == null) level = player.serverLevel();

                ItemStack stack = player.getItemInHand(PGHelper.getPortalGunHand(player));

                PortalGunItem.setHopLocation(stack, level.dimension().location().toString(), bPos);
                player.sendSystemMessage(Component.translatable("notice.ricksportalgun.randomizer_find_y.success").withStyle(ChatFormatting.GREEN));
            }
        });
    }

    public static SBCoordCheckerPacket decode(FriendlyByteBuf buf) {
        return new SBCoordCheckerPacket(buf.readBlockPos(), buf.readUtf());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(dim);
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("coord_check");
    }
}
