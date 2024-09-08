package robryo49.robsantiqueapi.mixin.entity_mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import robryo49.robsantiqueapi.item.custom.AntiqueToolItem;

import java.util.Iterator;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public ItemStack getMainHandStack() {
        return null;
    }

    @Shadow public abstract int getArmor();

    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @ModifyReturnValue(method = "disablesShield", at = @At("RETURN"))
    private boolean disablesShield(boolean original) {
        Item handItem = this.getMainHandStack().getItem();
        if (handItem instanceof AntiqueToolItem antiqueToolItem) {
            return original  || antiqueToolItem.heavyHittingLevel > 0;
        }
        return true;
    }

    @Redirect(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getArmor()I"))
    protected int applyArmorToDamage(LivingEntity entity, @Local(argsOnly = true) DamageSource source) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingEntity) {
            Iterator<ItemStack> handItems = livingEntity.getHandItems().iterator();
            if (handItems.hasNext()) {
                Item item = handItems.next().getItem();
                if (item instanceof AntiqueToolItem antiqueToolItem) {
                    return (int)(entity.getArmor() / Math.sqrt(antiqueToolItem.armorPiercingLevel + 1));
                }
            }
        }
        return entity.getArmor();
    }
}
