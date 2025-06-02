package com.jdolphin.ricksportalgun.common.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;

public class UpgradeType {
   public final Consumer<ItemStack> consumer;
   public final MutableComponent translationName;

   public UpgradeType(MutableComponent translationName, Consumer<ItemStack> onUse) {
       this.translationName = translationName;
       this.consumer = onUse;
   }



}
