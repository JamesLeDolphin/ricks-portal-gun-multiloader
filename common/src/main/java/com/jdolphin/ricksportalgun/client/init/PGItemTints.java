package com.jdolphin.ricksportalgun.client.init;

import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.client.tint.PortalColourTint;
import com.jdolphin.ricksportalgun.client.tint.PrimaryDyeTint;
import com.jdolphin.ricksportalgun.client.tint.SecondaryDyeTint;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PGItemTints {
    public static final Map<ResourceLocation, MapCodec<? extends ItemTintSource>> ALL = new HashMap<>();

    static {
        register("primary_dye", PrimaryDyeTint.CODEC);
        register("secondary_dye", SecondaryDyeTint.CODEC);
        register("portal_color", PortalColourTint.CODEC);
    }

    private static void register(String name, MapCodec<? extends ItemTintSource> codec) {
        ALL.put(PGHelper.createLocation(name), codec);
    }
}
