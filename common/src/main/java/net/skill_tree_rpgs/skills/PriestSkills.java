package net.skill_tree_rpgs.skills;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.summon.AttributeScaling;
import net.spell_engine.api.spell.summon.SummonBehaviour;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.Sound;
import net.minecraft.entity.attribute.EntityAttributes;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
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

    public static final String HOLY_BEAM = "paladins:holy_beam";
    public static final String CIRCLE_OF_HEALING = "paladins:circle_of_healing";
    public static final String BARRIER = "paladins:barrier";
    public static final String LEVITATE = "paladins:levitate";
    public static final String PENANCE = "paladins:penance";
    public static final String LIGHTWELL = "paladins:lightwell";

    /// Helper spell cast by the Lightwell when the Cleansing Light node is taken. Not bound to any
    /// node or book; it exists so `actions_add` below has a spell for the well to cast (player spell
    /// modifiers never apply to summon-cast spells, so the orb spell itself cannot be modified).
    public static final Skills.Entry lightwell_cleanse = add(lightwell_cleanse());
    private static Skills.Entry lightwell_cleanse() {
        var id = Identifier.of(NAMESPACE, "lightwell_cleanse");
        var title = "Cleansing Light";
        var description = "Removes a negative effect.";
        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 12;
        spell.learn = null; // summon-cast helper, never learnable

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.required = true;

        var cleanse = SpellBuilder.Impacts.effectCleanse();
        cleanse.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(Color.WHITE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(10).speed(0.2F, 0.4F))
        );
        cleanse.sound = new Sound(SpellEngineSounds.GENERIC_DISPEL_1.id());
        spell.impacts = List.of(cleanse);

        SpellBuilder.Cost.cooldown(spell, 8);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_2_spell_1_modifier_1 = add(priest_tier_2_spell_1_modifier_1());
    private static Skills.Entry priest_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_spell_1_modifier_1");
        var title = "Graceful Channeling";
        var description = "Channeling Holy Light releases {channel_ticks_add} additional times.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = HOLY_BEAM;
        modifier.channel_ticks_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_2_spell_1_modifier_2 = add(priest_tier_2_spell_1_modifier_2());
    private static Skills.Entry priest_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_spell_1_modifier_2");
        var title = "Searing Light";
        var description = "Holy Light deals {power_multiplier} more damage, and lights enemies on fire.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = HOLY_BEAM;
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.1F;

        var impact = SpellBuilder.Impacts.fire(2F);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_a)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(1).speed(0.1F, 0.2F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(1).speed(0.1F, 0.2F).verticalOrigin(Batches.FEET))
        );
        impact.sound = Sound.withVolume(SpellEngineSounds.GENERIC_FIRE_IGNITE.id(), 0.6F);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_spell_1_modifier_1 = add(priest_tier_3_spell_1_modifier_1());
    private static Skills.Entry priest_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_spell_1_modifier_1");
        var title = "Mass Dispel";
        var description = "Circle of Healing removes {effect_amplifier} negative effect from allies.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = CIRCLE_OF_HEALING;

        var impact = SpellBuilder.Impacts.effectCleanse();
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST)
                        .color(Color.WHITE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.6F, 0.6F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(Color.WHITE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(10).speed(0.2F, 0.4F))
        );
        impact.sound = new Sound(SpellEngineSounds.GENERIC_DISPEL_1.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_spell_1_modifier_2 = add(priest_tier_3_spell_1_modifier_2());
    private static Skills.Entry priest_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_spell_1_modifier_2");
        var title = "Sanctuary";
        var description = "Circle of Healing also applies an absorption shield to affected allies, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = CIRCLE_OF_HEALING;

        // The absorption shield Circle of Healing used to apply as part of its base kit,
        // now opt-in via this node ("paladins:priest_absorption" scales with healing power).
        var shield = SpellBuilder.Impacts.effectSet_ScaledAmplifier(
                "paladins:priest_absorption", 6, 0, 0.25F);
        shield.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(12).speed(0.2F, 0.25F))
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(shield);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_4_spell_1_modifier_1 = add(priest_tier_4_spell_1_modifier_1());
    private static Skills.Entry priest_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_4_spell_1_modifier_1");
        var title = "Cleansing Light";
        var description = "The Lightwell also cleanses negative effects from the allies it tends.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = LIGHTWELL;

        // Give the well a second spell-cast action; its cadence comes from the cleanse
        // spell's own cooldown, and it fires at the same acquired friendly target.
        var cast = new SummonBehaviour.Action.SpellCast(
                lightwell_cleanse.id().toString(), 30);
        cast.aiming.accept_target = true;
        cast.aiming.fallback = SummonBehaviour.Action.SpellCast.Aiming.Fallback.NONE;
        modifier.summon_behaviour.actions_add = List.of(SummonBehaviour.Action.spell(cast));

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_4_spell_1_modifier_2 = add(priest_tier_4_spell_1_modifier_2());
    private static Skills.Entry priest_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_4_spell_1_modifier_2");
        var title = "Empowered Well";
        var description = "Increases the Lightwell's healing power by 50%%.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = LIGHTWELL;

        // The well's base scaling copies 0.5x of the owner's healing power; merging in another
        // 0.25x raises that to 0.75x — i.e. +50% of the well's own healing power.
        var healingPower = new AttributeScaling.Entry();
        healingPower.attribute_id = SpellSchools.HEALING.id.toString();
        healingPower.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                SpellSchools.HEALING.id.toString(),
                EntityAttributeModifier.Operation.ADD_VALUE, 0.0, 0.25));
        modifier.summon_attribute_scaling = new AttributeScaling();
        modifier.summon_attribute_scaling.entries = List.of(healingPower);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry priest_tier_2_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.PRIEST, SpellSchools.HEALING,
            "priest_tier_2_spell_1_root", HOLY_BEAM, "Holy Light", 2F));
    public static final Skills.Entry priest_tier_3_spell_1_root = add(SkillsCommon.powerRoot(
            Skills.Category.PRIEST, SpellSchools.HEALING,
            "priest_tier_3_spell_1_root", CIRCLE_OF_HEALING, "Circle of Healing", 0.1F));
    public static final Skills.Entry priest_tier_4_spell_1_root = add(SkillsCommon.companionRoot(
            Skills.Category.PRIEST, SpellSchools.HEALING,
            "priest_tier_4_spell_1_root", LIGHTWELL, "Lightwell", 4));
    public static final Skills.Entry priest_tier_2_spell_2_root = add(SkillsCommon.lingerRoot(
            Skills.Category.PRIEST, SpellSchools.HEALING,
            "priest_tier_2_spell_2_root", LEVITATE, "Levitate", 2F));
    public static final Skills.Entry priest_tier_3_spell_2_root = add(SkillsCommon.lingerRoot(
            Skills.Category.PRIEST, SpellSchools.HEALING,
            "priest_tier_3_spell_2_root", PENANCE, "Penance", 2F));
    public static final Skills.Entry priest_tier_4_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.PRIEST, SpellSchools.HEALING,
            "priest_tier_4_spell_2_root", BARRIER, "Barrier", 5F));

    // ===================================================================================
    // Powerful mutex nodes for the second spell of each tier (spell_2):
    // Levitate (T2), Penance (T3), Barrier (T4).
    // ===================================================================================

    public static final Skills.Entry priest_tier_2_spell_2_modifier_1 = add(priest_tier_2_spell_2_modifier_1());
    private static Skills.Entry priest_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_spell_2_modifier_1");
        var title = "Uplift";
        var description = "Levitate also lifts allies within {impact_range} blocks of you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = LEVITATE;

        // Splash the caster-targeted impacts (upward kick + Floating) onto nearby allies. The kick
        // uses reset_velocity, so the caster receiving it twice (directly + via the splash's center
        // target) still lands on the same lift.
        var area = new Spell.AreaImpact();
        area.radius = 2F;
        modifier.replacing_area_impact = area;

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_2_spell_2_modifier_2 = add(priest_tier_2_spell_2_modifier_2());
    private static Skills.Entry priest_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_2_spell_2_modifier_2");
        var title = "Serenity";
        var effect = SkillEffects.SERENITY;
        var description = "Channeling Levitate grants Serenity, reducing damage taken by " + TooltipTokens.effect(SkillEffects.SERENITY.id, 0, null, TooltipTokens.Format.ABS) + " per stack, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = LEVITATE;

        // One stack per channel release (Levitate releases 4 times): a full channel reaches
        // 4 x 20% = 80% damage reduction, lingering as long as the Floating effect does.
        var serenity = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 6, 1, 3);
        serenity.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(8).speed(0.15F, 0.2F))
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(serenity);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_spell_2_modifier_1 = add(priest_tier_3_spell_2_modifier_1());
    private static Skills.Entry priest_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_spell_2_modifier_1");
        var title = "Hysteria";
        var effect = SkillEffects.HYSTERIA;
        // HYSTERIA has three equal haste modifiers; name the first (attack speed) explicitly.
        var description = "Penance bolts grant allies Hysteria, increasing attack speed, ranged and spell haste by "
                + TooltipTokens.effect(SkillEffects.HYSTERIA.id, 0,
                        Identifier.of(EntityAttributes.ATTACK_SPEED.getIdAsString()))
                + ", stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = PENANCE;

        // Beneficial, so Penance's Atonement splash carries it to allies near the struck
        // enemy (alongside the absorption shield) — one stack per bolt, 3 bolts per volley.
        var hysteria = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 1, 2);
        hysteria.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(10).speed(0.15F, 0.3F))
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(hysteria);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_spell_2_modifier_2 = add(priest_tier_3_spell_2_modifier_2());
    private static Skills.Entry priest_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_spell_2_modifier_2");
        var title = "Chastise";
        var effect = SkillEffects.CHASTISE;
        var description = "Penance bolts apply Chastise, increasing damage taken by " + TooltipTokens.effect(SkillEffects.CHASTISE.id) + ", stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = PENANCE;

        var chastise = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 1, 2);
        chastise.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.from(0xffcc66).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.2F, 0.25F))
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(chastise);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_4_spell_2_modifier_1 = add(priest_tier_4_spell_2_modifier_1());
    private static Skills.Entry priest_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_4_spell_2_modifier_1");
        var title = "Sacred Refuge";
        var description = "While your Barrier stands, its interior heals allies for {heal} and cleanses a negative effect every 2 sec, for {cloud_duration} sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast(BARRIER);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        // A gentle cloud matching the dome: same radius and the barrier's 10s lifetime.
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 4F;
        cloud.impact_tick_interval = 40;
        cloud.time_to_live_seconds = 10;
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.FLOAT)
                        .color(Color.from(0xccffff).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(2).speed(0.02F, 0.08F).anchor(ParticleGroup.Anchor.GROUND))
        );
        spell.deliver.clouds = List.of(cloud);

        var heal = SpellBuilder.Impacts.heal(0.1F);
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_heal, ParticleGroup.Motion.DECELERATE)
                        .color(Color.from(0xccffff).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(6).speed(0.1F, 0.15F))
        );
        heal.sound = Sound.withVolume(SpellEngineSounds.GENERIC_HEALING_IMPACT_2.id(), 0.4F);

        // Default cleanse carries a dispel chime; muted here so it doesn't ring on every cloud tick.
        var cleanse = SpellBuilder.Impacts.effectCleanse();
        cleanse.sound = Sound.of(SpellEngineSounds.GENERIC_DISPEL_1.id());
        cleanse.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.from(0xccffff).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(25).speed(0.3F, 0.5F))
        );

        spell.impacts = List.of(heal, cleanse);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_4_spell_2_modifier_2 = add(priest_tier_4_spell_2_modifier_2());
    private static Skills.Entry priest_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_4_spell_2_modifier_2");
        var title = "Barrier Duration";
        var description = "Increases the duration of Barrier by {spawn_duration_add} sec.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BARRIER;
        modifier.spawn_duration_add = 4;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_1_passive_1 = add(priest_tier_1_passive_1());
    private static Skills.Entry priest_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_1_passive_1");
        var effect = SkillEffects.HEALING_FOCUS;
        var title = "Healing Focus";
        var description = "Healing spells apply Healing Focus effect. Increasing healing received by " + TooltipTokens.effect(SkillEffects.HEALING_FOCUS.id) + ", stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHeal(1F);
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 1, 4);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_circle_1)
                        .attached()
                        .scale(0.8F)
                        .playbackSpeed(1.25F)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL).count(1).speed(0.15F, 0.16F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound(SkillSounds.priest_healing_focus.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_1_passive_2 = add(priest_tier_1_passive_2());
    private static Skills.Entry priest_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "priest_tier_1_passive_2");
        var effect = SkillEffects.INCANTER_CADENCE;
        var title = "Incanters' Cadence";
        var description = "Spell hits have {trigger_chance} chance to increase spell haste by " + TooltipTokens.effect(SkillEffects.INCANTER_CADENCE.id) + ", stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, null);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(10).speed(0.15F, 0.3F))
        );
        impact.sound = new Sound(SkillSounds.priest_incanter_cadence.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
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
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.2F, 0.2F))
        );
        impact.sound = new Sound(SkillSounds.priest_fade.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
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

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_wand.id(), Color.HOLY),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.DECELERATE)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(25).speed(0.2F, 0.2F))
        );
        spell.release.sound = new Sound(SpellEngineSounds.SIGNAL_SPELL_CRIT.id());

        var cooldownDuration = 15F;
        var stashTriggers = List.of(
                SpellBuilder.Triggers.activeSpellCast(),
                SpellBuilder.Triggers.activeSpellHit(1, null)
        );
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), cooldownDuration, stashTriggers);
        spell.deliver.stash_effect.consumed_next_tick = true;

        SpellBuilder.Cost.cooldown(spell, 15F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry priest_tier_3_passive_1 = add(priest_tier_3_passive_1()); // Pain Suppression
    private static Skills.Entry priest_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_tier_3_passive_1");
        var effect = SkillEffects.PAIN_SUPPRESSION;
        var title = effect.title;
        var healthThreshold = 0.3F;
        var description = "Healing targets under " + Skills.bakedPercent(healthThreshold)
                + " health, grants them " + effect.title
                + ", reducing damage taken by "
                + TooltipTokens.effect(SkillEffects.PAIN_SUPPRESSION.id, 0, null, TooltipTokens.Format.ABS)
                + ", for {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHeal(1F);
        trigger.stage = Spell.Trigger.Stage.PRE;
        trigger.target_conditions = List.of(SpellBuilder.TargetConditions.lowHP(healthThreshold));
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 0);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.2F, 0.2F))
        );
        impact.sound = new Sound(SkillSounds.priest_pain_suppression.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
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
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.BURST)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.6F, 0.8F))
        );
        impact.sound = new Sound(SkillSounds.priest_holy_blast.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.PRIEST));
    }
}
