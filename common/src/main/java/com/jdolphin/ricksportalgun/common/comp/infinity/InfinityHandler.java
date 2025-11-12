package com.jdolphin.ricksportalgun.common.comp.infinity;

import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.teleport.PortalCreator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class InfinityHandler {

    public static ResourceKey<Level> getOrCreateResourceKey(MinecraftServer server, String dim) {
        ResourceLocation id = InfinityMethods.dimTextToId(dim);
        PortalCreator.recordIdTranslation(server, id, dim);
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, id);

        if (server.getLevel(levelKey) == null) {
            server.executeIfPossible(() -> PortalCreator.tryAddInfinityDimension(server, id));
        }
        return levelKey;
    }
}
