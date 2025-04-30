package com.jdolphin.ricksportalgun.common.init;

import com.jdolphin.ricksportalgun.common.menu.PortalDispenserMenu;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PGMenuTypes {
    private static final Map<ResourceLocation, MenuType<?>> ALL = new HashMap<>();

    public static MenuType<PortalDispenserMenu> PORTAL_DISPENSER = register("portal_dispenser",
            PortalDispenserMenu::new);

    private static <M extends AbstractContainerMenu> MenuType<M> register(String name, BiFunction<Integer, Inventory, M> constructor) {
        MenuType<M> menu = Services.PLATFORM.createMenuType(constructor);
        ALL.put(PGHelper.createLocation(name), menu);
        return menu;
    }

    public static void init(BiConsumer<MenuType<?>, ResourceLocation> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
}
