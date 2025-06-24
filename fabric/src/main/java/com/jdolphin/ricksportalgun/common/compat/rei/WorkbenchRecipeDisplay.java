package com.jdolphin.ricksportalgun.common.compat.rei;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public record WorkbenchRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<WorkbenchRecipeDisplay> MAP_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipeDisplay> STREAM_CODEC;
    public static final RecipeDisplay.Type<WorkbenchRecipeDisplay> TYPE;

    @Override
    public SlotDisplay result() {
        return result;
    }

    @Override
    public SlotDisplay craftingStation() {
        return craftingStation;
    }

    @Override
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }

    static {
        MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(WorkbenchRecipeDisplay::ingredients),
                        SlotDisplay.CODEC.fieldOf("result").forGetter(WorkbenchRecipeDisplay::result),
                        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(WorkbenchRecipeDisplay::result))
                .apply(instance, WorkbenchRecipeDisplay::new));

        STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), WorkbenchRecipeDisplay::ingredients,
                SlotDisplay.STREAM_CODEC, WorkbenchRecipeDisplay::result,
                SlotDisplay.STREAM_CODEC, WorkbenchRecipeDisplay::craftingStation,
                WorkbenchRecipeDisplay::new);


        TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
    }
}
