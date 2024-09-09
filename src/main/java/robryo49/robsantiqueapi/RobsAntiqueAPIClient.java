package robryo49.robsantiqueapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import robryo49.robsantiqueapi.client.render.entity.AntiqueItemProjectileRenderer;
import robryo49.robsantiqueapi.entity.AntiqueEntities;

public class RobsAntiqueAPIClient implements ClientModInitializer {

    public void registerPredicates() {
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(AntiqueEntities.ANTIQUE_ITEM_PROJECTILE, AntiqueItemProjectileRenderer::new);
    }

    @Override
    public void onInitializeClient() {
        registerPredicates();
        registerEntityRenderers();
    }
}
