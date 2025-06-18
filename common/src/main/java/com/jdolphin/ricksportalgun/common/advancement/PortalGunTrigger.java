package com.jdolphin.ricksportalgun.common.advancement;

import com.jdolphin.ricksportalgun.common.init.PGCriteriaTriggers;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;

import java.util.Optional;

public class PortalGunTrigger extends SimpleCriterionTrigger<PortalGunTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return null;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> position, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {

        public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> position, Optional<ItemPredicate> item) {
            this.player = player;
            this.position = position;
            this.item = item;
        }

        public static Criterion<TriggerInstance> applyUpgrade(EntityPredicate.Builder player, LocationPredicate distance, ItemPredicate.Builder item) {
            return PGCriteriaTriggers.PORTAL_GUN_TRIGGER.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.of(distance), Optional.of(item.build())));
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }

        @Override
        public Optional<ItemPredicate> item() {
            return item;
        }

        @Override
        public Optional<LocationPredicate> position() {
            return position;
        }
    }
}
