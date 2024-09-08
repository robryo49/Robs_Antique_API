package robryo49.robsantiqueapi.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import robryo49.robsantiqueapi.item.custom.AntiqueToolItem;

public enum AntiqueEnchantmentTarget {
    WEAPON {
        public boolean isAcceptableItem(Item item) {
            if (item instanceof AntiqueToolItem antiqueToolItem) {
                return antiqueToolItem.isWeapon;
            } else return false;
        }
    },

    DIGGER{
        public boolean isAcceptableItem(Item item) {
            if (item instanceof AntiqueToolItem antiqueToolItem) {
                return antiqueToolItem.isForMining;
            } else return false;
        }
    },

    THROWABLE{
        public boolean isAcceptableItem(Item item) {
            if (item instanceof AntiqueToolItem antiqueToolItem) {
                return antiqueToolItem.throwableLevel > 0;
            } else return EnchantmentTarget.TRIDENT.isAcceptableItem(item);
        }
    };


    public boolean isAcceptableItem(Item item) {
        return false;
    }
}
