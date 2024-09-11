package robryo49.robsantiqueapi.mixin.enchantment_mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import robryo49.robsantiqueapi.enchantment.AntiqueEnchantmentTarget;
import robryo49.robsantiqueapi.item.custom.models.AntiqueToolItem;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentTarget target, Item item, @Local Enchantment enchantment) {

        if (item instanceof AntiqueToolItem antiqueToolItem) {
            if (target == EnchantmentTarget.WEAPON) {
                return AntiqueEnchantmentTarget.WEAPON.isAcceptableItem(antiqueToolItem);
            }
            if (target == EnchantmentTarget.DIGGER) {
                return AntiqueEnchantmentTarget.DIGGER.isAcceptableItem(antiqueToolItem);
            }
            if (enchantment == Enchantments.LOYALTY) {
                return AntiqueEnchantmentTarget.THROWABLE.isAcceptableItem(antiqueToolItem);
            }
        }
        return target.isAcceptableItem(item);
    }
}
