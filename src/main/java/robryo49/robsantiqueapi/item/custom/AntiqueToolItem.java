package robryo49.robsantiqueapi.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import robryo49.robsantiqueapi.entity.custom.AntiqueItemProjectile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.join;
import static java.util.Collections.nCopies;

public class AntiqueToolItem extends ToolItem {
    public int throwableLevel = 0;
    public int twoHandedLevel = 0;
    public int heavyHittingLevel = 0;
    public int armorPiercingLevel = 0;
    public double reach = 4.0D;
    public boolean canBlock = false;
    public boolean canBreakShields = false;
    public boolean isForMining = false;
    public boolean isWeapon = false;
    public List<TagKey<Block>> effectiveBlocks = new ArrayList<>();
    public final float miningSpeed;
    public final float baseAttackDamage;
    public final float attackDamage;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public AntiqueToolItem(float attackDamage, float attackSpeed, ToolMaterial material, AntiqueItemSettings settings) {
        super(material, settings);

        this.throwableLevel = settings.throwableLevel;
        this.twoHandedLevel = settings.twoHandedLevel;
        this.heavyHittingLevel = settings.heavyHittingLevel;
        this.armorPiercingLevel = settings.armorPiercingLevel;
        this.reach = settings.reach;
        this.canBlock = settings.canBlock;
        this.canBreakShields = settings.canBreakShields;
        this.effectiveBlocks = settings.effectiveBlocks;
        this.isForMining = settings.isForMining;
        this.isWeapon = settings.isWeapon;
        this.miningSpeed = material.getMiningSpeedMultiplier();
        this.baseAttackDamage = attackDamage;
        this.attackDamage = attackDamage + material.getAttackDamage();

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
                ReachEntityAttributes.REACH,
                new EntityAttributeModifier(UUID.randomUUID(), "Tool modifier", reach, EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
                ReachEntityAttributes.ATTACK_RANGE,
                new EntityAttributeModifier(UUID.randomUUID(), "Tool modifier", reach, EntityAttributeModifier.Operation.ADDITION)
        );
        this.attributeModifiers = builder.build();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (this.throwableLevel > 0) return UseAction.SPEAR;
        else if (this.canBlock) return UseAction.BLOCK;
        else return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity && this.throwableLevel > 0) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!world.isClient) {
                    stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
                    AntiqueItemProjectile throwingAntiqueToolEntity =
                            new AntiqueItemProjectile(world, playerEntity, stack, playerEntity.getActiveHand());
                    throwingAntiqueToolEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                            0.0F, (float) this.throwableLevel / 3 + 1.5F, 1.0F);
                    if (playerEntity.getAbilities().creativeMode) {
                        throwingAntiqueToolEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    }

                    world.spawnEntity(throwingAntiqueToolEntity);
                    world.playSoundFromEntity(null, throwingAntiqueToolEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (!playerEntity.getAbilities().creativeMode) {
                        playerEntity.getInventory().removeOne(stack);
                    }
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return this.isEffectiveOn(state) ? this.miningSpeed : 1.0F;
    }

    public boolean isCriticalHit(LivingEntity attacker) {
        boolean result = attacker.fallDistance > 0 && !attacker.isOnGround() && !attacker.isSleeping();
        if (attacker instanceof PlayerEntity player) result = result && !player.isFallFlying();
        return result;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(this.isForMining ? 2: 1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (this.heavyHittingLevel > 0 && !target.isBlocking() && isCriticalHit(attacker)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * this.heavyHittingLevel,
                    this.heavyHittingLevel, true, false));
        }
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }


    public boolean isEffectiveOn(BlockState state) {
        boolean effective = false;
        for (TagKey<Block> blockTagKey: effectiveBlocks) {
            if (state.isIn(blockTagKey)) {effective = true;}
        }
        return effective;
    }

    public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
        if (i < MiningLevels.DIAMOND && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < MiningLevels.IRON && state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= MiningLevels.STONE || !state.isIn(BlockTags.NEEDS_STONE_TOOL))
                    && isEffectiveOn(state);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (twoHandedLevel > 0 && selected && entity instanceof PlayerEntity player) {
            ItemStack offHandItemStack = player.getOffHandStack();
            if (!offHandItemStack.isEmpty() && (player.getMainHandStack().getItem() == this)) player.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1,
                            twoHandedLevel, true, false)
            );
        }
    }

    public String getRomanNumber(int number) {
        return join("", nCopies(number, "I"))
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX");
    }

    public void appendTraitLevelTooltip(List<Text> tooltip, String trait, int level) {
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

    public void appendTraitTooltip(List<Text> tooltip, String trait, boolean traitIsPresent) {
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

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        appendTraitLevelTooltip(tooltip, "heavy_hitting", heavyHittingLevel);
        appendTraitLevelTooltip(tooltip, "two_handed", twoHandedLevel);
        appendTraitLevelTooltip(tooltip, "throwable", throwableLevel);
        appendTraitLevelTooltip(tooltip, "armor_piercer", armorPiercingLevel);
        appendTraitTooltip(tooltip, "extended_reach", reach > 4);
        appendTraitTooltip(tooltip, "shield_breaker", canBreakShields);
        appendTraitTooltip(tooltip, "blocker", canBlock);
    }
}
