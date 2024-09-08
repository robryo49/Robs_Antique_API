package robryo49.robsantiqueapi;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import robryo49.robsantiqueapi.datagen.ModModelProvider;
import robryo49.robsantiqueapi.datagen.ModRecipeProvider;
import robryo49.robsantiqueapi.datagen.ModTagProvider;

public class RobsAntiqueAPIDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModTagProvider::new);
	}
}
