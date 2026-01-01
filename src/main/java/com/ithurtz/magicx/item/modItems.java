package com.ithurtz.magicx.item;

import com.ithurtz.magicx.magicx;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static Item MAGIC_ESSENCE;
    public static Item WIND_WAND;
    public static Item BLAZE_WAND;
    public static Item ENDER_ROD;

    public static void register() {
        MAGIC_ESSENCE = registerItem("magic_essence",
                new Item(new FabricItemSettings().group(null))); // set creative group later

        WIND_WAND = registerItem("wind_wand",
                new WandItem(new FabricItemSettings().maxCount(1), WandItem.Type.WIND));

        BLAZE_WAND = registerItem("blaze_wand",
                new WandItem(new FabricItemSettings().maxCount(1), WandItem.Type.BLAZE));

        ENDER_ROD = registerItem("ender_rod",
                new WandItem(new FabricItemSettings().maxCount(1), WandItem.Type.ENDER));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(magicx.MOD_ID, name), item);
    }
}
