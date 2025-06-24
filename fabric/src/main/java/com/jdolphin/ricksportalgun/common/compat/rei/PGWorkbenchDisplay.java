package com.jdolphin.ricksportalgun.common.compat.rei;

import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PGWorkbenchDisplay extends BasicDisplay {
    public static final DisplaySerializer<PGWorkbenchDisplay> SERIALIZER;

    public PGWorkbenchDisplay(PortalGunWorkbenchRecipe recipe) {
        super(List.of(EntryIngredients.ofItemStacks(recipe.getInputs())), List.of(EntryIngredients.of(recipe.getResult())));
    }

    public PGWorkbenchDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public PGWorkbenchDisplay(WorkbenchRecipeDisplay workbenchRecipeDisplay, Optional<RecipeDisplayId> recipeDisplayId) {
        this(EntryIngredients.ofSlotDisplays(workbenchRecipeDisplay.ingredients()), List.of(EntryIngredients.ofSlotDisplay(workbenchRecipeDisplay.result())));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CategoryIdentifier.of(PGHelper.createLocation("plugins/workbench"));
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    static {
        SERIALIZER = DisplaySerializer.of(RecordCodecBuilder.mapCodec((instance) ->
                instance.group(EntryIngredient.codec().listOf().fieldOf("ingredients").forGetter(BasicDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("result").forGetter(BasicDisplay::getOutputEntries)).apply(instance, PGWorkbenchDisplay::new)),
                StreamCodec.composite(EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), BasicDisplay::getInputEntries, EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        BasicDisplay::getOutputEntries, PGWorkbenchDisplay::new));

    }
}
