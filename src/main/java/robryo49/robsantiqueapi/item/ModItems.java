package robryo49.robsantiqueapi.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import robryo49.robsantiqueapi.RobsAntiqueAPI;
import robryo49.robsantiqueapi.item.custom.AntiqueItemSettings;
import robryo49.robsantiqueapi.item.custom.AntiqueToolItem;
import robryo49.robsantiqueapi.item.custom.SpearItem;

import java.util.ArrayList;
import java.util.Arrays;

public class ModItems {

    public static final Item TEST_TOOL = registerItem("test_tool", new AntiqueToolItem(
            5, -3, ToolMaterials.DIAMOND,
            new AntiqueItemSettings()
                    .effectiveAgainst(BlockTags.AXE_MINEABLE)
                    .effectiveAgainst(BlockTags.PICKAXE_MINEABLE)
                    .twoHanded(3)
                    .heavyHitting(1)
                    .canBreakShields()
                    .throwable(5)
                    .armorPiercer(5)
                    .isForMining()
                    .isWeapon()
                    .reach(10)
    ));

    public static ArrayList<ToolMaterial> materials = new ArrayList<>(Arrays.asList(ToolMaterials.values()));
    public static ArrayList<Item> antiqueToolItems = new ArrayList<>();

    public static void registerSpearItems() {
        for (ToolMaterial material: materials) {
            antiqueToolItems.add(registerItem(
                    "spear_" + material.toString().toLowerCase(), new SpearItem(material)
            ));
        }
    }

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(RobsAntiqueAPI.MOD_ID, name), item);
    }

    public static void registerModItems() {
        RobsAntiqueAPI.LOGGER.info("Registering Mod Items for " + RobsAntiqueAPI.MOD_ID);
        registerSpearItems();
    }
}
