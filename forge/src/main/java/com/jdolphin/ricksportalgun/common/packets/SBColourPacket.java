package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.common.item.PortalGunItem;
import com.jdolphin.ricksportalgun.common.util.helpers.Helper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SBColourPacket {
	int colour;

	public SBColourPacket(int colour) {
		this.colour = colour;
	}

	public SBColourPacket(FriendlyByteBuf buf) {
		this.colour = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.colour);
	}

	public boolean handle(CustomPayloadEvent.Context context) {
		ServerPlayer player = context.getSender();

		assert player != null;
		ItemStack stack = player.getMainHandItem();
		PortalGunItem item = Helper.getPortalGun(stack);
		item.setColor(stack, this.colour);
		return true;
	}
}