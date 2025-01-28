package com.jdolphin.ricksportalgun.common.init;


import com.jdolphin.ricksportalgun.common.util.helper.Helper;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PGTags {
    public static class Items {
        public static final TagKey<Item> PORTAL_GUNS = makeTag("portal_guns");

        public static TagKey<Item> makeTag(String name) {
            return TagKey.create(Registries.ITEM, Helper.createLocation(name));
        }
    }
    public static class Blocks {
        public static final TagKey<Block> RANDOMIZER_AVOID = makeTag("randomizer_avoid");

        public static TagKey<Block> makeTag(String name) {
            return TagKey.create(Registries.BLOCK, Helper.createLocation(name));
        }
    }
}