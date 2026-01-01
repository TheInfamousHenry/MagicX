package com.ithurtz.magicx;

import com.ithurtz.magicx.block.ExtractorBlock;
import com.ithurtz.magicx.block.ExtractorBlockEntity;
import com.ithurtz.magicx.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class magicx implements ModInitializer {
    public static final String MOD_ID = "magicx";
    public static final Logger LOGGER = LoggerFactory.getLogger("magicx");

    // Block and BlockEntity registrations
    public static final Block EXTRACTOR_BLOCK = new ExtractorBlock();
    public static BlockEntityType<ExtractorBlockEntity> EXTRACTOR_BLOCK_ENTITY;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing magicx mod");

        // Register items
        ModItems.register();

        // Register extractor block
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "extractor"), EXTRACTOR_BLOCK);

        // BlockEntity type
        EXTRACTOR_BLOCK_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "extractor_block_entity"),
                FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new, EXTRACTOR_BLOCK).build(null)
        );

        LOGGER.info("magicx initialized");
    }
}
