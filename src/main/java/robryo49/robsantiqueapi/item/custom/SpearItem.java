package robryo49.robsantiqueapi.item.custom;

import net.minecraft.item.ToolMaterial;

public class SpearItem extends AntiqueToolItem{
    public SpearItem(ToolMaterial material) {
        super(5, -2, material,
                new AntiqueItemSettings()
                .throwable(3)
                .reach(5)
                .twoHanded(1)
                .isWeapon()
        );
    }
}
