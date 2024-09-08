package robryo49.robsantiqueapi.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import robryo49.robsantiqueapi.item.custom.AntiqueToolItem;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    public void registerItemModel(ItemModelGenerator itemModelGenerator, Item item) {
        itemModelGenerator.register(item, Models.HANDHELD);

        if (item instanceof AntiqueToolItem antiqueToolItem) {
            if (antiqueToolItem.canBlock) itemModelGenerator.register(item, "_blocking", Models.HANDHELD);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        //  registerItemModel(itemModelGenerator, ModItems.TEST_TOOL);
    }
}
