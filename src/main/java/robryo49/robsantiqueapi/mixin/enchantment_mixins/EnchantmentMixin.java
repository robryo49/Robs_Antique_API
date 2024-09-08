package robryo49.robsantiqueapi.mixin.enchantment_mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.robsantiqueapi.enchantment.AntiqueEnchantmentTarget;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Shadow @Final public EnchantmentTarget target;
    @Shadow public abstract Text getName(int level);

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (this.target == EnchantmentTarget.WEAPON) {
            cir.setReturnValue(AntiqueEnchantmentTarget.WEAPON.isAcceptableItem(stack.getItem()));
        }
        if (this.target == EnchantmentTarget.DIGGER) {
            cir.setReturnValue(AntiqueEnchantmentTarget.DIGGER.isAcceptableItem(stack.getItem()));
        }
        if ((Object)this == Enchantments.LOYALTY) {
            cir.setReturnValue(AntiqueEnchantmentTarget.THROWABLE.isAcceptableItem(stack.getItem()));
        }
    }
}
