package com.jdolphin.ricksportalgun.common.meeseeks.argument;

import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class BlockArgument extends AbstractMeeseeksArgument<Block> {

    public BlockArgument(String name) {
        super(name, Block.class);
    }

    @Override
    public Block fromString(String string) {
        if (isValid(string)) {
            ResourceLocation rl = ResourceLocation.tryParse(string);
            return BuiltInRegistries.BLOCK.get(rl);
        }
        return null;
    }

    @Override
    public boolean isValid(String string) {
        ResourceLocation rl = ResourceLocation.tryParse(string);
        if (rl != null) {
            Block block = BuiltInRegistries.BLOCK.get(rl);
            return !block.equals(Blocks.AIR);
        }
        return false;
    }

    @Override
    public List<String> values() {
        return BuiltInRegistries.BLOCK.stream().map(block -> block.getName().getString()).toList();
    }
}
