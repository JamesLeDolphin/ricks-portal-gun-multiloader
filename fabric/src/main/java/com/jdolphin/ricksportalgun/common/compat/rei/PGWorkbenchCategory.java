package com.jdolphin.ricksportalgun.common.compat.rei;

import com.jdolphin.ricksportalgun.client.screen.workbench.WorkbenchCraftingScreen;
import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.LinkedList;
import java.util.List;

public class PGWorkbenchCategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<PGWorkbenchDisplay> WORKBENCH =
            CategoryIdentifier.of(PGHelper.createLocation("workbench"));

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return WORKBENCH;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("menu.ricksportalgun.workbench.craft");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(PGItems.PORTAL_GUN_WORKBENCH);
    }

    public int getDisplayHeight() {
        return 90;
    }

    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(WorkbenchCraftingScreen.CRAFT_BG, new Rectangle(startPoint.x, startPoint.y, 175, 82)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 54, startPoint.y + 34))
                .entries(display.getInputEntries().getFirst()).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 104, startPoint.y + 34))
                .entries(display.getOutputEntries().getFirst()).markOutput());

        return widgets;
    }
}
