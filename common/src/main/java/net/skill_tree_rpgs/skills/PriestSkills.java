package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PriestSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Skills.Entry priest_tier_2_spell_1_modifier_1 = add(priest_tier_2_spell_1_modifier_1());
    private static Skills.Entry priest_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_spell_1_modifier_1");
        var title = "Graceful Channeling";
        var description = "Reduces the cooldown of Holy Light by {cooldown_duration_deduct} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_beam";
        modifier.cooldown_duration_deduct = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_2_spell_1_modifier_2 = add(priest_tier_2_spell_1_modifier_2());
    private static Skills.Entry priest_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_spell_1_modifier_2");
        var title = "Searing Light";
        var description = "Holy Light deals {power_multiplier} more damage, and lights enemies on fire.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_beam";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.1F;

        var impact = SpellBuilder.Impacts.fire(2F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        1, 0.1F, 0.2F),
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        1, 0.1F, 0.2F)
        };
        impact.sound = Sound.withVolume(SpellEngineSounds.GENERIC_FIRE_IGNITE.id(), 0.6F);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_spell_1_modifier_1 = add(priest_tier_3_spell_1_modifier_1());
    private static Skills.Entry priest_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_spell_1_modifier_1");
        var title = "Mass Dispel";
        var description = "Circle of Healing removes {effect_amplifier} negative effect from allies.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:circle_of_healing";

        var impact = SpellBuilder.Impacts.effectCleanse();
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.6F, 0.6F)
                        .color(Color.WHITE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.4F)
                        .color(Color.WHITE.toRGBA())
        };
        impact.sound = new Sound(SpellEngineSounds.GENERIC_DISPEL_1.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_spell_1_modifier_2 = add(priest_tier_3_spell_1_modifier_2());
    private static Skills.Entry priest_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_spell_1_modifier_2");
        var title = "Consecration";
        var description = "Circle of Healing leaves a consecrated area behind, dealing {damage} damage to enemies, for {cloud_duration} sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast("paladins:circle_of_healing");
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var radius = 6.0F;
        consecration(spell, 0.2F, radius, 4);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    private static void consecration(Spell spell, float coefficient, float radius, float particleMultiplier) {
        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = radius;
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 5;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
                        3 * particleMultiplier, 0.05F, 0.1F)
                        .color(SkillsCommon.HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
                        3 * particleMultiplier, 0.05F, 0.15F)
                        .color(SkillsCommon.HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        3 * particleMultiplier, 0.05F, 0.15F)
                        .color(SkillsCommon.HOLY_COLOR).extent(radius),
        };
        spell.deliver.clouds = List.of(cloud);

        var impact = SpellBuilder.Impacts.damage(coefficient, 0.1F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        10, 0.4F, 0.4F)
                        .color(SkillsCommon.HOLY_COLOR),
        };
        impact.sound = new Sound(SkillSounds.priest_consecration_impact.id());
        spell.impacts = List.of(impact);
    }

    public static final Skills.Entry priest_tier_4_spell_1_modifier_1 = add(priest_tier_4_spell_1_modifier_1());
    private static Skills.Entry priest_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_4_spell_1_modifier_1");
        var title = "Barrier Recovery";
        var description = "Reduces the cooldown of Barrier by {cooldown_duration_deduct} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:barrier";
        modifier.cooldown_duration_deduct = 10;

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_4_spell_1_modifier_2 = add(priest_tier_4_spell_1_modifier_2());
    private static Skills.Entry priest_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_4_spell_1_modifier_2");
        var title = "Barrier Duration";
        var description = "Increases the duration of Barrier by {spawn_duration_add} sec.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:barrier";
        modifier.spawn_duration_add = 4;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_1_passive_1 = add(priest_tier_1_passive_1());
    private static Skills.Entry priest_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_1_passive_1");
        var effect = SkillEffects.HEALING_FOCUS;
        var title = "Healing Focus";
        var description = "Healing spells apply Healing Focus effect. Increasing healing received by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHeal(1F);
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 1, 4);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.area_circle_1.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                        1, 0.15F, 0.16F)
                        .followEntity(true)
                        .scale(0.8F)
                        .maxAge(0.8F)
                        .color(Color.HOLY.toRGBA()),
        };
        impact.sound = new Sound(SkillSounds.priest_healing_focus.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_1_passive_2 = add(priest_tier_1_passive_2());
    private static Skills.Entry priest_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_1_passive_2");
        var effect = SkillEffects.INCANTER_CADENCE;
        var title = "Incanters' Cadence";
        var description = "Spell hits have {trigger_chance} chance to increase spell haste by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, null);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        10, 0.15F, 0.3F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SkillSounds.priest_incanter_cadence.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_2_passive_1 = add(priest_tier_2_passive_1()); // Fade
    private static Skills.Entry priest_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_passive_1");
        var title = "Fade";
        var description = "Upon rolling, nearby mobs stop attacking you, allowing them to target your allies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 15;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();

        var impact = SpellBuilder.Impacts.disengage(true);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.2F)
                        .color(Color.HOLY.toRGBA())
        };
        impact.sound = new Sound(SkillSounds.priest_fade.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_2_passive_2 = add(priest_tier_2_passive_2()); // Divine Favor
    private static Skills.Entry priest_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_passive_2");
        var effect = SkillEffects.DIVINE_FAVOR;
        var title = effect.title;
        var description = "Upon rolling, you have {trigger_chance_1} chance to guarantee critical strike for your next spell cast.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_wand.id(), Color.HOLY),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        25, 0.2F, 0.2F)
                        .color(Color.HOLY.toRGBA())
        };
        spell.release.sound = new Sound(SpellEngineSounds.SIGNAL_SPELL_CRIT.id());

        var cooldownDuration = 15F;
        var stashTriggers = List.of(
                SpellBuilder.Triggers.activeSpellCast(),
                SpellBuilder.Triggers.activeSpellHit(1, null)
        );
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), cooldownDuration, stashTriggers);
        spell.deliver.stash_effect.consumed_next_tick = true;

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_passive_1 = add(priest_tier_3_passive_1()); // Pain Suppression
    private static Skills.Entry priest_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_passive_1");
        var effect = SkillEffects.PAIN_SUPPRESSION;
        var title = effect.title;
        var healthThreshold = 0.3F;
        var description = "Healing targets under {threshold} health, grants them " + effect.title + ", reducing damage taken by {bonus}, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(effect.config().firstModifier().value));
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{threshold}", SpellTooltip.percent(healthThreshold));
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHeal(1F);
        trigger.stage = Spell.Trigger.Stage.PRE;
        trigger.target_conditions = List.of(SpellBuilder.TargetConditions.lowHP(healthThreshold));
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.2F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SkillSounds.priest_pain_suppression.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_passive_2 = add(priest_tier_3_passive_2()); // Celestial Orbs
    private static Skills.Entry priest_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_passive_2");
        var effect = SkillEffects.CELESTIAL_ORB;
        var title = "Celestial Orbs";
        var description = "Spell critical strikes and heals grant you {stash_amplifier} Celestial Orbs. Orbs damage enemies attacking you, dealing {damage} spell damage.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = new Sound(SkillSounds.priest_orbs_activate.id());

        var trigger = SpellBuilder.Triggers.activeSpellCrit();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 15, SpellBuilder.Triggers.damageTaken());
        spell.deliver.stash_effect.amplifier = 2;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0.1F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.6F, 0.8F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SkillSounds.priest_holy_blast.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }
}
