package robryo49.robsantiqueapi.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robryo49.robsantiqueapi.RobsAntiqueAPI;

import java.util.ArrayList;
import java.util.Arrays;

public class ModItems {

    public static ArrayList<ToolMaterial> materials = new ArrayList<>(Arrays.asList(ToolMaterials.values()));
    public static ArrayList<Item> antiqueToolItems = new ArrayList<>();

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(RobsAntiqueAPI.MOD_ID, name), item);
    }

    public static void registerModItems() {
        RobsAntiqueAPI.LOGGER.info("Registering Mod Items for " + RobsAntiqueAPI.MOD_ID);
    }
}
