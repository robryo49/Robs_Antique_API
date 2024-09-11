package robryo49.robsantiqueapi.item.custom.models;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robryo49.robsantiqueapi.entity.custom.AntiqueItemProjectile;

import java.util.List;
import java.util.UUID;

public class AntiqueSwordItem extends SwordItem implements AntiqueToolItem {
    public int size;
    public int throwableLevel;
    public int twoHandedLevel;
    public int heavyHittingLevel;
    public int armorPiercingLevel;
    public double reach;
    public boolean canBlock;
    public boolean canBreakShields;
    public boolean isForMining;
    public boolean isWeapon;
    public final float miningSpeed;
    public final float baseAttackDamage;
    public final float attackDamage;
    public List<TagKey<Block>> effectiveBlocks;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public AntiqueSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, AntiqueItemSettings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);

        this.size = settings.size;
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
        this.miningSpeed = toolMaterial.getMiningSpeedMultiplier();
        this.baseAttackDamage = attackDamage;
        this.attackDamage = attackDamage + toolMaterial.getAttackDamage();

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(UUID.randomUUID(), "Tool modifier", reach - 3 + 0.5, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(UUID.randomUUID(), "Tool modifier", reach - 3, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (this.throwableLevel > 0) {
            return UseAction.SPEAR;
        }
        else if (this.canBlock) {
            return UseAction.BLOCK;
        }
        else {
            return super.getUseAction(stack);
        }
    }

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(this.isForMining ? 1: 2, miner, (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (this.throwableLevel > 0 || this.canBlock)
            return 72000;
        else return 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        ItemStack itemStack = user.getStackInHand(hand);

        if (this.canBlock || this.throwableLevel> 0) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.pass(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
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
        return this.isEffectiveOn(state) ? this.miningSpeed : super.getMiningSpeedMultiplier(stack, state);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);
        stack.damage(this.isWeapon ? 1: 2, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
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

    public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
        if (i < MiningLevels.DIAMOND && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < MiningLevels.IRON && state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= MiningLevels.STONE || !state.isIn(BlockTags.NEEDS_STONE_TOOL))
                    && (isEffectiveOn(state) || super.isSuitableFor(state));
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
