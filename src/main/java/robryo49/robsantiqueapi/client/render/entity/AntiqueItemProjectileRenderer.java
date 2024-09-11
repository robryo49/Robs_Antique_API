package robryo49.robsantiqueapi.client.render.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import robryo49.robsantiqueapi.entity.custom.AntiqueItemProjectile;

public class AntiqueItemProjectileRenderer extends EntityRenderer<AntiqueItemProjectile> {
    public final ItemRenderer itemRenderer;

    public AntiqueItemProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(AntiqueItemProjectile entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {

        ItemStack itemStack = entity.getItemStack();
        float scale = 1.75F;
        if (itemStack.getItem() instanceof AntiqueToolItem antiqueToolItem) scale *= antiqueToolItem.size;

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw() - 90));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.getPitch() - 45));
        matrixStack.scale(scale, scale, 1.75F);
        matrixStack.translate(0.0F, -0.15F, 0.0F);

        this.itemRenderer
                .renderItem(
                        itemStack, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers, entity.getWorld(), entity.getId()
                );
        matrixStack.pop();

        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(AntiqueItemProjectile entity) {
        return entity.getTexture();
    }
}
