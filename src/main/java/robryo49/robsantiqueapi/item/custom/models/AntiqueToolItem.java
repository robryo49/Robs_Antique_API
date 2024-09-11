package robryo49.robsantiqueapi.item.custom.models;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static java.util.Collections.nCopies;

public interface AntiqueToolItem {
    int size = 1;
    int throwableLevel = 0;
    int twoHandedLevel = 0;
    int heavyHittingLevel = 0;
    int armorPiercingLevel = 0;
    double reach = 4.0D;
    boolean canBlock = false;
    boolean canBreakShields = false;
    boolean isForMining = false;
    boolean isWeapon = false;
    float miningSpeed = 1;
    float attackDamage = 0;
    List<TagKey<Block>> effectiveBlocks = new ArrayList<>();

    default boolean isCriticalHit(LivingEntity attacker) {
        boolean result = attacker.fallDistance > 0 && !attacker.isOnGround() && !attacker.isSleeping();
        if (attacker instanceof PlayerEntity player) result = result && !player.isFallFlying();
        return result;
    }

    default boolean isEffectiveOn(BlockState state) {
        boolean effective = false;
        for (TagKey<Block> blockTagKey: effectiveBlocks) {
            if (state.isIn(blockTagKey)) {effective = true;}
        }
        return effective;
    }

    default String getRomanNumber(int number) {
        return join("", nCopies(number, "I"))
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX");
    }

    default void appendTraitLevelTooltip(List<Text> tooltip, String trait, int level) {
        if (level > 0) {
            tooltip.add(Text.translatable("item.custom." + trait, getRomanNumber(level))
                    .formatted(Formatting.GREEN));

            String description_key = "item.custom." + trait + "_description";
            if (Screen.hasShiftDown()) {
                if (I18n.hasTranslation(description_key)) {
                    tooltip.add(Text.translatable(description_key).formatted(Formatting.GREEN));
                }
                else {
                    for (int line = 1; line <= 10; line ++) {
                        if (I18n.hasTranslation(description_key + "_" + line)) {
                            tooltip.add(Text.translatable(description_key + "_" + line)
                                    .formatted(Formatting.GREEN)
                                    .formatted(Formatting.ITALIC));
                        }
                    }
                }
            }
        }
    }
    default void appendTraitTooltip(List<Text> tooltip, String trait, boolean traitIsPresent) {
        if (traitIsPresent) {
            tooltip.add(Text.translatable("item.custom." + trait)
                    .formatted(Formatting.GREEN));

            String description_key = "item.custom." + trait + "_description";
            if (Screen.hasShiftDown()) {
                if (I18n.hasTranslation(description_key)) {
                    tooltip.add(Text.translatable(description_key)
                            .formatted(Formatting.GREEN)
                            .formatted(Formatting.ITALIC));
                }
                else {
                    for (int line = 1; line <= 10; line ++) {
                        if (I18n.hasTranslation(description_key + "_" + line)) {
                            tooltip.add(Text.translatable(description_key + "_" + line)
                                    .formatted(Formatting.GREEN)
                                    .formatted(Formatting.ITALIC));
                        }
                    }
                }
            }
        }
    }
}
