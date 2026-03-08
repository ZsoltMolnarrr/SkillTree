package net.skill_tree_rpgs.skills;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
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

    // public static final Skills.Entry archer_tier_1_spell_1_modifier_1 = add(archer_tier_1_spell_1_modifier_1());
    private static Skills.Entry archer_tier_1_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_1_spell_1_modifier_1");
        var title = "Improved Hunter's Mark";
        var description = "Power Shot applies {stash_amplifier_add} additional Hunter's Mark stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:power_shot";
        modifier.stash_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

//    public static final Skills.Entry archer_tier_1_spell_1_modifier_2 = add(archer_tier_1_spell_1_modifier_2());
//    private static Skills.Entry archer_tier_1_spell_1_modifier_2() {
//        var id = Identifier.of(NAMESPACE, "archer_tier_1_spell_1_modifier_2");
//        var title = "Charged Shot";
//        var description = "Power Shot deals {damage} damage around the target hit.";
//        var spell = SpellBuilder.createSpellModifier();
//        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
//
//        var radius = 3F;
//
//        var modifier = new Spell.Modifier();
//        modifier.spell_pattern = "archers:power_shot";
//
//        var impact = SpellBuilder.Impacts.damage(0.5F, 0);
//        impact.action.allow_on_center_target = false;
//
//        var area_impact = new Spell.AreaImpact();
//        area_impact.execute_action_type = Spell.Impact.Action.Type.DAMAGE;
//        area_impact.radius = radius;
//        area_impact.area = new Spell.Target.Area();
//        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
//        area_impact.particles = new ParticleBatch[]{
//                new ParticleBatch(
//                        SkillsCommon.SPARK_DECELERATE.toString(),
//                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
//                        20, 0.35F, 0.35F
//                ).color(Color.RED.toRGBA()),
//                new ParticleBatch(
//                        "firework",
//                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
//                        20, 0.15F, 0.15F
//                )
//        };
//
//        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
//        modifier.impacts = List.of(impact);
//        modifier.replacing_area_impact = area_impact;
//
//        spell.modifiers = List.of(modifier);
//
//        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
//    }

//    public static final Skills.Entry archer_tier_2_spell_1_modifier_1 = add(archer_tier_2_spell_1_modifier_1());
//    private static Skills.Entry archer_tier_2_spell_1_modifier_1() {
//        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_1_modifier_1");
//        var title = "Nettle Sprouts";
//        var description = "Entangling Roots has {impact_chance} chance to apply stacking poison, lasting {effect_duration} sec.";
//        var spell = SpellBuilder.createSpellModifier();
//        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
//
//        var modifier = new Spell.Modifier();
//        modifier.spell_pattern = "archers:entangling_roots";
//
//        var impact = SpellBuilder.Impacts.effectAdd(StatusEffects.POISON.getIdAsString(), 5, 1, 1);
//        impact.chance = 0.5F;
//        impact.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
//        impact.particles = SkillsCommon.poisonImpactParticles();
//
//        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
//        modifier.impacts = List.of(impact);
//
//        spell.modifiers = List.of(modifier);
//
//        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
//    }

    public static final Skills.Entry archer_tier_2_spell_1_modifier_1 = add(archer_tier_2_spell_1_modifier_1());
    private static Skills.Entry archer_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_1_modifier_1");
        var title = "Improved Hunter's Mark";
        var description = "Power Shot applies {stash_amplifier_add} additional Hunter's Mark stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:power_shot";
        modifier.stash_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_2_spell_1_modifier_2 = add(archer_tier_2_spell_1_modifier_2());
    private static Skills.Entry archer_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_spell_1_modifier_2");
        var title = "Nature's Grasp";
        var description = "Entangling Roots has {impact_chance} chance to immobilize the target for {effect_duration} sec.";
        var effect = SkillEffects.NATURES_GRASP;
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:entangling_roots";
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        impact.chance = 0.3F;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_spell_1_modifier_1 = add(archer_tier_3_spell_1_modifier_1());
    private static Skills.Entry archer_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_spell_1_modifier_1");
        var title = "Extensive Barrage";
        var description = "Barrage fires {extra_launch} extra arrow.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:barrage";
        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1; // TODO: Check if works for arrows
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_spell_1_modifier_2 = add(archer_tier_3_spell_1_modifier_2());
    private static Skills.Entry archer_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_spell_1_modifier_2");
        var title = "Blood Barrage";
        var description = "Barrage arrow hits heal you by {heal}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:barrage";
        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.sound = Sound.withVolume(SpellEngineSounds.LEECHING_IMPACT.id(), 0.75F);
        impact.action.apply_to_caster = true;
        impact.particles = SkillsCommon.leechImpactParticles();
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_4_spell_1_modifier_1 = add(archer_tier_4_spell_1_modifier_1());
    private static Skills.Entry archer_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_4_spell_1_modifier_1");
        var title = "Conjured Arrow";
        var description = "Magic Arrow has {trigger_chance} chance to reset its own cooldown.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var trigger = SpellBuilder.Triggers.specificSpellCast("archers:magic_arrow");
        trigger.chance = 0.4F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive("archers:magic_arrow");
        impact.action.apply_to_caster = true;
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_4_spell_1_modifier_2 = add(archer_tier_4_spell_1_modifier_2());
    private static Skills.Entry archer_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_4_spell_1_modifier_2");
        var title = "Magic Punch";
        var description = "Magic Arrow deals extra {knockback_multiply_base} knockback.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:magic_arrow";
        modifier.knockback_multiply_base = 1.5F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    private static final float RHYTHM_DURATION = 6F;
    private static Spell.Impact rhythmImpact() {
        var impact = SpellBuilder.Impacts.effectAdd(SkillEffects.RHYTHM.id.toString(), RHYTHM_DURATION, 1, 4);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.4F)
                        .color(Color.NATURE.toRGBA())
        };
        impact.sound = new Sound(SkillSounds.archer_rhythm_activate.id());
        return impact;
    }

    private static SpellEntityPredicates.Entry HAS_HUNTERS_MARK = SpellEntityPredicates.hasEffectOptimized(Identifier.of("archers", "hunters_mark"));
    public static final Skills.Entry archer_tier_1_passive_1 = add(archer_tier_1_passive_1());
    private static Skills.Entry archer_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_1_passive_1");
        var title = "Rhythm";
        var description = "Hitting Marked target increasing ranged attack speed by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var effect = SkillEffects.RHYTHM;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

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

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_1_passive_2 = add(archer_tier_1_passive_2());
    private static Skills.Entry archer_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_1_passive_2");
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
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "crit",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.25F, 0.3F)
                        .color(Color.RED.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
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

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Color ROLL_COLOR = Color.from(0x3399ff);

    public static final Skills.Entry archer_tier_2_passive_2 = add(archer_tier_2_passive_2()); // Tactical Maneuver (effect on roll)
    private static Skills.Entry archer_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_2_passive_2");
        var title = "Tactical Maneuver";
        var description = "Rolling has {trigger_chance} chance to increase your roll recharge speed by {bonus}, for {effect_duration} sec.";
        var effect = SkillEffects.TACTICAL_MANEUVER;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_roll.id(), ROLL_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F)
                        .color(ROLL_COLOR.toRGBA())
        };

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.4F)
                        .color(Color.NATURE.toRGBA())
        };
        impact.sound = new Sound(SkillSounds.archer_maneuver_activate.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Color SUPERCHARGE_COLOR = Color.NATURE.blend(Color.WHITE, 0.5F);

    public static final Skills.Entry archer_tier_3_passive_1 = add(archer_tier_3_passive_1()); // Supercharge on arrow hit
    private static Skills.Entry archer_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_passive_1");
        var title = "Supercharge";
        var effect = SkillEffects.SUPERCHARGE;
        var damageMultiplier = 2F;
        var description = "Arrow hits have {trigger_chance_1} chance to Supercharge your next shot within {stash_duration} sec, taking longer to pull but dealing {bonus} damage with strong knockback.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(damageMultiplier);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_arrow.id(), SUPERCHARGE_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F).color(SUPERCHARGE_COLOR.toRGBA())
        };
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
        spell.arrow_perks.launch_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 50,0.18F,0.2F, 0)
                        .color(SUPERCHARGE_COLOR.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 25,0.28F,0.3F, 0)
                        .color(SUPERCHARGE_COLOR.toRGBA())
        };
        spell.arrow_perks.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        5, 0.1F, 0.2F)
                        .color(SUPERCHARGE_COLOR.toRGBA())
        };
        spell.arrow_perks.launch_sound = new Sound(SkillSounds.archer_supercharge_release.id());

        var impact = SpellBuilder.Impacts.damage(0F, 1.5F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.4F, 0.5F)
                        .color(SUPERCHARGE_COLOR.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.7F, 0.8F)
                        .color(SUPERCHARGE_COLOR.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCHER));
    }

    public static final Skills.Entry archer_tier_3_passive_2 = add(archer_tier_3_passive_2()); // Deflection (protective effect on low HP)
    private static Skills.Entry archer_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "archer_tier_3_passive_2");
        var title = "Deflection";
        final var healthThreshold = 0.5F;
        var description = "Upon taking damage below {threshold} health you gain Deflection effect, parrying the next {effect_amplifier} incoming melee attack, lasting {effect_duration} sec.";
        var effect = SkillEffects.DEFLECTION;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var threshold = SpellTooltip.percent(healthThreshold);
            return args.description()
                    .replace("{threshold}", threshold);
        };

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

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCHER));
    }
}
