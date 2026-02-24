package com.jdolphin.ricksportalgun.common.comp.infinity;

import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.teleport.PortalCreator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class InfinityHandler {

    public static ResourceLocation getDimensionId(MinecraftServer server, String dim) {
        ResourceLocation id = InfinityMethods.dimTextToId(dim);
        PortalCreator.recordIdTranslation(server, id, dim);
        server.executeIfPossible(() -> PortalCreator.tryAddInfinityDimension(server, id));
        return id;
    }
}
