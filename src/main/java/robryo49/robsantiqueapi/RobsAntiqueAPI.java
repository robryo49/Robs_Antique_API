package robryo49.robsantiqueapi;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robryo49.robsantiqueapi.entity.ModEntities;
import robryo49.robsantiqueapi.item.ModItemGroups;
import robryo49.robsantiqueapi.item.ModItems;

public class RobsAntiqueAPI implements ModInitializer {
	public static final String MOD_ID = "robsantiqueapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	// onInitialize method

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModEntities.registerModEntities();
	}
}