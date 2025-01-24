package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.Constants;
import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.Waypoint;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import com.jdolphin.ricksportalgun.common.util.helpers.LevelHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SBSetDestinationPacket {
	protected BlockPos pos;
	protected String dim;

	public SBSetDestinationPacket(BlockPos pos, ResourceLocation dim) {
		this.pos = pos;
		this.dim = dim.toString();
	}

	public SBSetDestinationPacket(Waypoint waypoint) {
		this.pos = waypoint.getBlockPos();
		this.dim = waypoint.getDim();
	}

	public SBSetDestinationPacket(FriendlyByteBuf buf) {
		this.dim = buf.readUtf();
		this.pos = buf.readBlockPos();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(this.dim);
		buf.writeBlockPos(this.pos);
	}

	public boolean handle(CustomPayloadEvent.Context context) {

		ServerPlayer player = context.getSender();
		assert player != null;
		try {
			if (/*!PortalGunCommonConfig.getBlacklistedDims().contains(this.dim) ||*/ LevelHelper.getPlayerDimensionLocation(player).toString().equals(dim)) {
				ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
				PortalGunItem item = Helper.getPortalGun(stack);
				item.setHopLocation(stack, ResourceLocation.parse(this.dim), this.pos);
				return true;
			} else player.displayClientMessage(
					Component.translatable("notice.ricksportalgun.dimension_disabled").withStyle(ChatFormatting.RED), false);
			return false;
		} catch (NullPointerException err) {
			Constants.LOGGER.warn(err.getLocalizedMessage());
			return false;
		}
	}
}