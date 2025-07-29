package com.jdolphin.ricksportalgun.common.compat;

import com.jdolphin.ricksportalgun.PGConstants;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorkbenchDisplay extends BasicDisplay {
    public static CategoryIdentifier<WorkbenchDisplay> CATEGORY_IDENTIFIER = CategoryIdentifier.of(PGConstants.MODID, "workbench");

    public WorkbenchDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CATEGORY_IDENTIFIER;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
}