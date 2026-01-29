package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.common.init.PGMeeseeksCommands;
import com.jdolphin.ricksportalgun.common.meeseeks.util.CommandNode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MeeseeksCommandScreen extends AbstractBaseScreen {

    public MeeseeksCommandScreen() {
        super(Component.translatable("menu.ricksportalgun.meeseeks"));
    }

    public void init() {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        List<CommandNode> cmds = PGMeeseeksCommands.COMMANDS;


        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
