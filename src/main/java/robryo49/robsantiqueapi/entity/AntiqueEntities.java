package robryo49.robsantiqueapi.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robryo49.robsantiqueapi.RobsAntiqueAPI;
import robryo49.robsantiqueapi.entity.custom.AntiqueItemProjectile;

public class AntiqueEntities {

    public static final EntityType<AntiqueItemProjectile> ANTIQUE_ITEM_PROJECTILE =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(RobsAntiqueAPI.MOD_ID, "antique_item_projectile"),
                FabricEntityTypeBuilder.<AntiqueItemProjectile>create(SpawnGroup.MISC, AntiqueItemProjectile::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                        .trackRangeChunks(10)
                        .trackedUpdateRate(20)
                        .build());

    public static void registerModEntities() {
        RobsAntiqueAPI.LOGGER.info("Registering Mod Entities for " + RobsAntiqueAPI.MOD_ID);
    }
}
