package com.jdolphin.ricksportalgun.common.packets;

import com.jdolphin.ricksportalgun.client.gui.portalgun.CoordTravelScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraftforge.event.network.CustomPayloadEvent;
import java.util.List;

public class CBOpenGuiPacket {
    private List<String> list;

    public CBOpenGuiPacket(List<String> list) {
        this.list = list;
    }

    public CBOpenGuiPacket(FriendlyByteBuf buf) {
        this.list = buf.readList(ByteBufCodecs.STRING_UTF8);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(this.list, ByteBufCodecs.STRING_UTF8);
    }

    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isClientSide()) {
            return handleClient(this.list);
        }
        return false;
    }

    public boolean handleClient(List<String> dims) {
        Minecraft.getInstance().setScreen(new CoordTravelScreen(dims));
        return true;
    }
}
