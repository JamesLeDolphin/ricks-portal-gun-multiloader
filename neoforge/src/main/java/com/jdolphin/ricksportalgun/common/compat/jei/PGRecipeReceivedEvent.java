package com.jdolphin.ricksportalgun.common.compat.jei;

import com.jdolphin.ricksportalgun.PGConstants;
import com.jdolphin.ricksportalgun.common.init.PGRecipeTypes;
import com.jdolphin.ricksportalgun.common.recipe.PortalGunWorkbenchRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = PGConstants.MODID, value = Dist.CLIENT)
public class PGRecipeReceivedEvent {
    public static List<RecipeHolder<PortalGunWorkbenchRecipe>> recipeHolders = new ArrayList<>();

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        if (recipeHolders == null) {
            recipeHolders = new ArrayList<>();
        }
        recipeHolders = event.getRecipeMap().byType(PGRecipeTypes.WORKBENCH_TYPE).stream().toList();
    }

    @SubscribeEvent
    public static void onClientLeave(ClientPlayerNetworkEvent.LoggingOut loggingOut) {
        recipeHolders = null;
    }
}