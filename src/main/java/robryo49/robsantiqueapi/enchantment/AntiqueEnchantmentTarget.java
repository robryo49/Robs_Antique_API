package robryo49.robsantiqueapi.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import robryo49.robsantiqueapi.item.custom.models.AntiqueToolItem;

public enum AntiqueEnchantmentTarget {
    WEAPON {
        public boolean isAcceptableItem(Item item) {
            return EnchantmentTarget.WEAPON.isAcceptableItem(item);
        }

        public boolean isAcceptableItem(AntiqueToolItem item) {
            return item.isWeapon;
        }
    },

    DIGGER{
        public boolean isAcceptableItem(Item item) {
            return EnchantmentTarget.DIGGER.isAcceptableItem(item);
        }

        public boolean isAcceptableItem(AntiqueToolItem item) {
            return item.isForMining;
        }
    },

    THROWABLE{
        public boolean isAcceptableItem(Item item) {
            return EnchantmentTarget.TRIDENT.isAcceptableItem(item);
        }

        public boolean isAcceptableItem(AntiqueToolItem item) {
            return item.throwableLevel > 0;
        }
    };


    public boolean isAcceptableItem(Item item) {
        return false;
    }
}
