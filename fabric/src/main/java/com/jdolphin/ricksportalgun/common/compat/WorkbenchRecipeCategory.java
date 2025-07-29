package com.jdolphin.ricksportalgun.common.compat;

import com.jdolphin.ricksportalgun.common.init.PGItems;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchRecipeCategory implements DisplayCategory<WorkbenchDisplay> {

    @Override
    public CategoryIdentifier<? extends WorkbenchDisplay> getCategoryIdentifier() {
        return WorkbenchDisplay.CATEGORY_IDENTIFIER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.ricksportalgun.portal_gun_workbench");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(PGItems.PORTAL_GUN_WORKBENCH);
    }

    public int getDisplayHeight() {
        return 92;
    }

    @Override
    public List<Widget> setupDisplay(WorkbenchDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createCategoryBase(new Rectangle(bounds.x, bounds.y, 155, 92)));

        widgets.add(Widgets.createTexturedWidget(PGHelper.id("textures/gui/workbench/arrow.png"),
                bounds.x + 57, bounds.y + 16, 0, 0, 34, 46, 34, 46));
        bounds.setSize(128, 92);

        List<EntryIngredient> ingredients = display.getInputEntries();
        EntryIngredient input = ingredients.getFirst();

        int size = input.size();
        if (!ingredients.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.x + 40, bounds.y + 10)).entries(input.getFirst().isEmpty() ? List.of() : List.of(input.getFirst())).markInput());

            if (size > 1) {
                widgets.add(Widgets.createSlot(new Point(bounds.x + 92, bounds.y + 10)).entries(input.get(1).isEmpty() ? List.of() : List.of(input.get(1))).markInput());
            } else widgets.add(Widgets.createSlotBackground(new Point(bounds.x + 92, bounds.y + 10)));

            if (size > 2) {
                widgets.add(Widgets.createSlot(new Point(bounds.x + 40, bounds.y + 33)).entries(input.get(2).isEmpty() ? List.of() : List.of(input.get(2))).markInput());
            } else widgets.add(Widgets.createSlotBackground(new Point(bounds.x + 40, bounds.y + 33)));

            if (size > 3) {
                widgets.add(Widgets.createSlot(new Point(bounds.x + 92, bounds.y + 33)).entries(input.get(3).isEmpty() ? List.of() : List.of(input.get(3))).markInput());
            } else widgets.add(Widgets.createSlotBackground(new Point(bounds.x + 92, bounds.y + 33)));

            for (int i = 0; i < display.getOutputEntries().size(); i ++) {
                if (display.getOutputEntries().get(i) != null) {
                    widgets.add(Widgets.createSlot(new Point(bounds.x + 66, bounds.y + 64)).entries(display.getOutputEntries().get(i)).markOutput());
                }
            }

        }
        return widgets;
    }
}