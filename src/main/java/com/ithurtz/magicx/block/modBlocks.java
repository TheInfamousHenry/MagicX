package com.ithurtz.magicx.block;

import com.ithurtz.magicx.magicx;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class modBlocks {

    // Define your custom block
    public static final Block RUBY_BLOCK = registerBlock("ruby_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block RUBY_ORE = registerBlock("ruby_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_ORE)
                    .requiresTool()));

    // Helper method to register blocks
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK,
                new Identifier(YourMod.MOD_ID, name), block);
    }

    // Register the block's item form
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM,
                new Identifier(YourMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        YourMod.LOGGER.info("Registering blocks for " + YourMod.MOD_ID);
    }
}
