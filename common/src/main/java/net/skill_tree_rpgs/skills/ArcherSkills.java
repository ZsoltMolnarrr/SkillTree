package net.skill_tree_rpgs.skills;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.summon.AttributeScaling;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ArcherSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final String POWER_SHOT = "archers:power_shot";
    public static final String ENTANGLING_ROOTS = "archers:entangling_roots";
    public static final String BARRAGE = "archers:barrage";
    public static final String MAGIC_ARROW = "archers:magic_arrow";
    public static final String SPIRIT_WOLF = "archers:spirit_wolf";
    public static final String RAIN_OF_ARROWS = "archers:rain_of_arrows";

    public static final Skills.Entry archer_tier_2_spell_1_modifier_1 = add(archer_tier_2_spell_1_modifier_1());
    private static Skills.Entry archer_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_1_modifier_1");
        var title = "Improved Hunter's Mark";
        var description = "Power Shot applies {stash_amplifier_add} additional Hunter's Mark stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = POWER_SHOT;
        modifier.stash_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_2_spell_1_modifier_2 = add(archer_tier_2_spell_1_modifier_2());
    private static Skills.Entry archer_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_1_modifier_2");
        var title = "Charged Shot";
        var description = "Power Shot deals {damage} damage around the target hit.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var radius = 3F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = POWER_SHOT;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0);
        impact.action.allow_on_center_target = false;

        var area_impact = new Spell.AreaImpact();
        area_impact.execute_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.radius = radius;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.RED.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(20).speed(0.35F, 0.35F)),
                ParticleGroupBuilder.of("firework")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(20).speed(0.15F, 0.15F))
        );

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        modifier.replacing_area_impact = area_impact;

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_2_spell_2_modifier_1 = add(archer_tier_2_spell_2_modifier_1());
    private static Skills.Entry archer_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_2_modifier_1");
        var title = "Nettle Sprouts";
        var description = "Entangling Roots has {impact_chance} chance to apply stacking poison, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ENTANGLING_ROOTS;

        var impact = SpellBuilder.Impacts.effectAdd(StatusEffects.POISON.getIdAsString(), 5, 1, 1);
        impact.chance = 0.5F;
        impact.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
        impact.visuals = SkillsCommon.poisonImpactParticles();

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }


    public static final Skills.Entry archer_tier_2_spell_2_modifier_2 = add(archer_tier_2_spell_2_modifier_2());
    private static Skills.Entry archer_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_2_modifier_2");
        var title = "Nature's Grasp";
        var description = "Entangling Roots has {impact_chance} chance to immobilize the target for {effect_duration} sec.";
        var effect = SkillEffects.NATURES_GRASP;
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ENTANGLING_ROOTS;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        impact.chance = 0.3F;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_spell_1_modifier_1 = add(archer_tier_3_spell_1_modifier_1());
    private static Skills.Entry archer_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_spell_1_modifier_1");
        var title = "Extensive Barrage";
        var description = "Barrage fires {extra_launch} extra arrow.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BARRAGE;
        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1; // TODO: Check if works for arrows
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_spell_1_modifier_2 = add(archer_tier_3_spell_1_modifier_2());
    private static Skills.Entry archer_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_spell_1_modifier_2");
        var title = "Blood Barrage";
        var description = "Barrage arrow hits heal you by {heal}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BARRAGE;
        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.sound = Sound.withVolume(SpellEngineSounds.LEECHING_IMPACT.id(), 0.75F);
        impact.action.apply_to_caster = true;
        impact.visuals = SkillsCommon.leechImpactParticles();
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_4_spell_1_modifier_1 = add(archer_tier_4_spell_1_modifier_1());
    private static Skills.Entry archer_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_4_spell_1_modifier_1");
        var title = "Torrential Arrows";
        var description = "Rain of Arrows rains {extra_launch} additional arrows, scattered over a {meteor_launch_radius_add} blocks wider area.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = RAIN_OF_ARROWS;
        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 10;
        modifier.meteor_launch_radius_add = 1.5F;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_4_spell_1_modifier_2 = add(archer_tier_4_spell_1_modifier_2());
    private static Skills.Entry archer_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_4_spell_1_modifier_2");
        var title = "Endless Volley";
        var description = "Rain of Arrows hits reduce its remaining cooldown by 1 sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit(RAIN_OF_ARROWS);
        // At most one cooldown trim per tick, so the multi-tick, multi-target volley trims 1 sec per
        // tick it lands on someone — not once per arrow per enemy. No internal self-cooldown: the
        // trigger must keep firing across ticks for the deduction to accumulate.
        trigger.cap_per_tick = 1;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        // Deterministic per-tick trim of the remaining cooldown (multiplier stays 1, so it's not a
        // reset). The engine applies duration_add in TICKS against the remaining tick count
        // (SpellImpacts.modifyCooldowns, no seconds->ticks conversion), so -20 ticks == -1 second.
        var shave = new Spell.Impact();
        shave.action = new Spell.Impact.Action();
        shave.action.type = Spell.Impact.Action.Type.COOLDOWN;
        shave.action.apply_to_caster = true;
        shave.action.cooldown = new Spell.Impact.Action.Cooldown();
        shave.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        shave.action.cooldown.actives.id = RAIN_OF_ARROWS;
        shave.action.cooldown.actives.duration_add = -20F; // 20 ticks = 1 second
        spell.impacts = List.of(shave);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry archer_tier_2_spell_1_root = add(SkillsCommon.lingerRoot(
            Skills.Category.ARCHER, ExternalSpellSchools.PHYSICAL_RANGED,
            "archer_tier_2_spell_1_root", POWER_SHOT, "Power Shot", 2F));
    public static final Skills.Entry archer_tier_2_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.ARCHER, ExternalSpellSchools.PHYSICAL_RANGED,
            "archer_tier_2_spell_2_root", ENTANGLING_ROOTS, "Entangling Roots", 3F));
    public static final Skills.Entry archer_tier_3_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.ARCHER, ExternalSpellSchools.PHYSICAL_RANGED,
            "archer_tier_3_spell_1_root", BARRAGE, "Barrage", 1F));
    // +10% max health on the wolves. Archers gives each wolf `20 + 1.0 x owner ranged power` HP
    // (ArcherEntities.spiritWolfDefaults + ArcherSummons.rangedCombatScaling), so 10% of that is
    // `2.0 + 0.1 x P` — reconstructed here as a flat base plus an owner coefficient because the
    // engine applies summon scaling as ADD_VALUE only and ignores OwnerModifier.operation.
    // Keep in sync if Archers retunes the wolf's base health or its health coefficient.
    public static final Skills.Entry archer_tier_3_spell_2_root = add(SkillsCommon.spellRoot(
            Skills.Category.ARCHER, ExternalSpellSchools.PHYSICAL_RANGED,
            "archer_tier_3_spell_2_root", SPIRIT_WOLF, "Spirit Wolf",
            "Spirit Wolves gain 10%% increased maximum health.",
            modifier -> {
                var health = new AttributeScaling.Entry();
                health.attribute_id = EntityAttributes.MAX_HEALTH.getIdAsString();
                health.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                        ExternalSpellSchools.PHYSICAL_RANGED.attributeEntry.getIdAsString(),
                        EntityAttributeModifier.Operation.ADD_VALUE, 2.0, 0.1));
                modifier.summon_attribute_scaling = new AttributeScaling();
                modifier.summon_attribute_scaling.entries = List.of(health);
            }));
    public static final Skills.Entry archer_tier_4_spell_1_root = add(SkillsCommon.critRoot(
            Skills.Category.ARCHER, ExternalSpellSchools.PHYSICAL_RANGED,
            "archer_tier_4_spell_1_root", RAIN_OF_ARROWS, "Rain of Arrows", 0.05F));
    public static final Skills.Entry archer_tier_4_spell_2_root = add(SkillsCommon.powerRoot(
            Skills.Category.ARCHER, ExternalSpellSchools.PHYSICAL_RANGED,
            "archer_tier_4_spell_2_root", MAGIC_ARROW, "Magic Arrow", 0.1F));

    // ===================================================================================
    // Powerful mutex modifiers for the second spell of tiers 3 and 4 (spell_2).
    // Tier 2 spell_2 (Entangling Roots) already has its two powerful nodes above
    // (Nettle Sprouts / Nature's Grasp) — reused from an earlier merge.
    // spirit_wolf (T3, summon), magic_arrow (T4).
    // ===================================================================================

    public static final Skills.Entry archer_tier_3_spell_2_modifier_1 = add(archer_tier_3_spell_2_modifier_1());
    private static Skills.Entry archer_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_spell_2_modifier_1");
        var title = "Leader of the Pack";
        var description = "Summons an additional Spirit Wolf.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SPIRIT_WOLF;
        modifier.summon_spawn_count_add = 1;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_spell_2_modifier_2 = add(archer_tier_3_spell_2_modifier_2());
    private static Skills.Entry archer_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_spell_2_modifier_2");
        var title = "Enduring Bond";
        var seconds = 10;
        var description = "Spirit Wolf lasts " + seconds + " sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SPIRIT_WOLF;
        modifier.summon_behaviour.lifespan.active_seconds_add = seconds;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_4_spell_2_modifier_1 = add(archer_tier_4_spell_2_modifier_1());
    private static Skills.Entry archer_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_4_spell_2_modifier_1");
        var title = "Conjured Arrow";
        var description = "Magic Arrow has {trigger_chance} chance to reset its own cooldown.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var trigger = SpellBuilder.Triggers.specificSpellCast(MAGIC_ARROW);
        trigger.chance = 0.4F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive(MAGIC_ARROW);
        impact.action.apply_to_caster = true;
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_4_spell_2_modifier_2 = add(archer_tier_4_spell_2_modifier_2());
    private static Skills.Entry archer_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_4_spell_2_modifier_2");
        var title = "Magic Punch";
        var description = "Magic Arrow deals extra {knockback_multiply_base} knockback.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = MAGIC_ARROW;
        modifier.knockback_multiply_base = 1.5F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    private static final float RHYTHM_DURATION = 6F;
    private static Spell.Impact rhythmImpact() {
        var impact = SpellBuilder.Impacts.effectAdd(SkillEffects.RHYTHM.id.toString(), RHYTHM_DURATION, 1, 4);
        impact.action.apply_to_caster = true;
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.NATURE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.4F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound(SkillSounds.archer_rhythm_activate.id());
        return impact;
    }

    private static SpellEntityPredicates.Entry HAS_HUNTERS_MARK = SpellEntityPredicates.hasEffectOptimized(Identifier.of("archers", "hunters_mark"));
    public static final Skills.Entry archer_tier_1_passive_1 = add(archer_tier_1_passive_1());
    private static Skills.Entry archer_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_1_passive_1");

        var title = "Concussive Shot";
        var description = "Arrows have {trigger_chance} chance, to stun the target for {effect_duration} sec.";
        var effect = SpellEngineEffects.STUN;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.chance = 0.2F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2F, 0);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("crit")
                        .color(Color.RED.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.25F, 0.3F))
        );
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_1_passive_2 = add(archer_tier_1_passive_2());
    private static Skills.Entry archer_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_1_passive_2");

        var title = "Rhythm";
        var description = "Hitting Marked target increasing ranged attack speed by " + TooltipTokens.effect(SkillEffects.RHYTHM.id) + ", stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var effect = SkillEffects.RHYTHM;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        var condition = new Spell.TargetCondition();
        condition.entity_predicate_id = HAS_HUNTERS_MARK.id().toString();
        trigger.target_conditions = List.of(condition);
        spell.passive.triggers = List.of(trigger);

        spell.impacts = List.of(rhythmImpact());

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_2_passive_1 = add(archer_tier_2_passive_1()); // Momentum (additional stack of Rhythm on roll)
    private static Skills.Entry archer_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_passive_1");
        var title = "Momentum";
        var effect = SkillEffects.RHYTHM;
        var description = "Rolling grants you an additional stack of " + effect.title + ".";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        spell.impacts = List.of(rhythmImpact());

        SpellBuilder.Cost.cooldown(spell, RHYTHM_DURATION);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Color ROLL_COLOR = Color.from(0x3399ff);

    public static final Skills.Entry archer_tier_2_passive_2 = add(archer_tier_2_passive_2()); // Tactical Maneuver (effect on roll)
    private static Skills.Entry archer_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_passive_2");
        var title = "Tactical Maneuver";
        var description = "Rolling has {trigger_chance} chance to increase your roll recharge speed by " + TooltipTokens.effect(SkillEffects.TACTICAL_MANEUVER.id) + ", for {effect_duration} sec.";
        var effect = SkillEffects.TACTICAL_MANEUVER;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_roll.id(), ROLL_COLOR),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.ASCEND)
                        .color(ROLL_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET))
        );

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.NATURE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.4F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound(SkillSounds.archer_maneuver_activate.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Color SUPERCHARGE_COLOR = Color.NATURE.blend(Color.WHITE, 0.5F);

    public static final Skills.Entry archer_tier_3_passive_1 = add(archer_tier_3_passive_1()); // Supercharge on arrow hit
    private static Skills.Entry archer_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_passive_1");
        var title = "Supercharge";
        var effect = SkillEffects.SUPERCHARGE;
        var damageMultiplier = 2F;
        var description = "Arrow hits have {trigger_chance_1} chance to Supercharge your next shot within {stash_duration} sec, taking longer to pull but dealing "
                + Skills.bakedPercent(damageMultiplier)
                + " damage with strong knockback.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_arrow.id(), SUPERCHARGE_COLOR),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(SUPERCHARGE_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET))
        );
        spell.release.sound = new Sound(SkillSounds.archer_supercharge_activate.id());

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.chance = 0.2F;

        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.arrowShot(false);
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 5F, stashTrigger);
        spell.deliver.stash_effect.impact_mode = Spell.Delivery.StashEffect.ImpactMode.TRANSFER;

        spell.arrow_perks = new Spell.ArrowPerks();
        spell.arrow_perks.damage_multiplier = damageMultiplier;
        spell.arrow_perks.launch_visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SUPERCHARGE_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(50).speed(0.18F, 0.2F).anchor(ParticleGroup.Anchor.LAUNCH_POINT).alignment(ParticleGroup.Alignment.LOOK)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SUPERCHARGE_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(25).speed(0.28F, 0.3F).anchor(ParticleGroup.Anchor.LAUNCH_POINT).alignment(ParticleGroup.Alignment.LOOK))
        );
        spell.arrow_perks.travel_particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST)
                        .color(SUPERCHARGE_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(5).speed(0.1F, 0.2F).verticalOrigin(Batches.FEET))
        );
        spell.arrow_perks.launch_sound = new Sound(SkillSounds.archer_supercharge_release.id());

        var impact = SpellBuilder.Impacts.damage(0F, 1.5F);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SUPERCHARGE_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.4F, 0.5F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SUPERCHARGE_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.7F, 0.8F))
        );
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_passive_2 = add(archer_tier_3_passive_2()); // Deflection (protective effect on low HP)
    private static Skills.Entry archer_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_passive_2");
        var title = "Deflection";
        final var healthThreshold = 0.5F;
        var description = "Upon taking damage below " + Skills.bakedPercent(healthThreshold)
                + " health you gain Deflection effect, parrying the next {effect_amplifier} incoming melee attack, lasting {effect_duration} sec.";
        var effect = SkillEffects.DEFLECTION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.becomingLowHP(healthThreshold);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 2);
        buff.sound = new Sound(SkillSounds.archer_deflection_activate.id());
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 45F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ARCHER));
    }
}
