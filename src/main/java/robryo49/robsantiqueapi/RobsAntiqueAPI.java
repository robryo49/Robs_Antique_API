package robryo49.robsantiqueapi;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robryo49.robsantiqueapi.entity.AntiqueEntities;

public class RobsAntiqueAPI implements ModInitializer {
	public static final String MOD_ID = "robsantiqueapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		AntiqueEntities.registerModEntities();
	}
}