package robryo49.robsantiqueapi.item.custom.models;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;

public class AntiqueItemSettings extends Item.Settings {
    public int size = 1;
    public int throwableLevel = 0; // DONE
    public int twoHandedLevel = 0; // DONE
    public int heavyHittingLevel = 0; // DONE
    public int armorPiercingLevel = 0; // DONE
    public double reach = 3.0D; // DONE
    public boolean isForMining = false; // DONE
    public boolean isWeapon = false; // DONE
    public boolean canBlock = false; // DONE
    public boolean canBreakShields = false; // DONE
    public List<TagKey<Block>> effectiveBlocks = new ArrayList<>(); // DONE

    public AntiqueItemSettings throwable(int level) {
        throwableLevel = level;
        return this;
    }

    public AntiqueItemSettings twoHanded(int level) {
        twoHandedLevel = level;
        return this;
    }

    public AntiqueItemSettings heavyHitting(int level) {
        heavyHittingLevel = level;
        return this;
    }

    public AntiqueItemSettings armorPiercer(int level) {
        armorPiercingLevel = level;
        return this;
    }

    public AntiqueItemSettings reach(double distance) {
        reach = distance;
        return this;
    }

    public AntiqueItemSettings canBlock() {
        canBlock = true;
        return this;
    }

    public AntiqueItemSettings canBreakShields() {
        canBreakShields = true;
        return this;
    }

    public AntiqueItemSettings isForMining() {
        isForMining = true;
        return this;
    }
    public AntiqueItemSettings isWeapon() {
        isWeapon = true;
        return this;
    }

    public AntiqueItemSettings effectiveAgainst(TagKey<Block> tagKey) {
        effectiveBlocks.add(tagKey);
        return this;
    }

    public AntiqueItemSettings setSize(int size) {
        this.size = size;
        return this;
    }
}