package robryo49.robsantiqueapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import robryo49.robsantiqueapi.client.render.entity.AntiqueItemProjectileRenderer;
import robryo49.robsantiqueapi.entity.ModEntities;
import robryo49.robsantiqueapi.item.ModItems;
import robryo49.robsantiqueapi.item.custom.AntiqueToolItem;

public class RobsAntiqueAPIClient implements ClientModInitializer {

    public void registerPredicates() {
        ModelPredicateProviderRegistry.register(
                ModItems.TEST_TOOL,
                new Identifier("parrying"),
                (stack, world, entity, seed) ->
                        (stack.getItem() instanceof AntiqueToolItem antiqueToolItem &&
                        entity != null && entity.isUsingItem() && entity.getActiveItem() == stack
                        && antiqueToolItem.canBlock) ? 1.0F: 0.0F);

        ModelPredicateProviderRegistry.register(
                ModItems.TEST_TOOL,
                new Identifier("throwing"),
                (stack, world, entity, seed) ->
                        (stack.getItem() instanceof AntiqueToolItem antiqueToolItem &&
                        entity != null && entity.isUsingItem() && entity.getActiveItem() == stack
                        && antiqueToolItem.throwableLevel > 0) ? 1.0F: 0.0F);
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.ANTIQUE_ITEM_PROJECTILE, AntiqueItemProjectileRenderer::new);
    }

    @Override
    public void onInitializeClient() {
        registerPredicates();
        registerEntityRenderers();
    }
}
