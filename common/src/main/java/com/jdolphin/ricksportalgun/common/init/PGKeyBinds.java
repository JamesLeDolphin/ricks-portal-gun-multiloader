package com.jdolphin.ricksportalgun.common.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class PGKeyBinds {
    public static final String PORTAL_MENU_CATEGORY = "key.category.ricksportalgun.portal_menu";
    public static final String PORTAL_MENU_KEY = "key.ricksportalgun.portal_menu";

    public static final KeyMapping KEY_PORTAL_MENU = new KeyMapping(PORTAL_MENU_KEY,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, PORTAL_MENU_CATEGORY);
}