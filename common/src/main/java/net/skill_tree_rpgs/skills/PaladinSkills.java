package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PaladinSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final String FLASH_HEAL = "paladins:flash_heal";
    public static final String DIVINE_PROTECTION = "paladins:divine_protection";
    public static final String JUDGEMENT = "paladins:judgement";
    public static final String BATTLE_BANNER = "paladins:battle_banner";
    public static final String BLESSED_STRIKES = "paladins:blessed_strikes";
    public static final String IMMOLATION = "paladins:immolation";

    public static final Skills.Entry paladin_tier_2_spell_1_modifier_1 = add(paladin_tier_2_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_2_spell_1_modifier_1() {
        var effect = SkillEffects.DIVINE_STRENGTH;

        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_1");
        var title = "Divine Strength";
        var description = "Flash Heal increases Attack Damage by {bonus} for {effect_duration} sec.";

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FLASH_HEAL;

        var impact = SpellBuilder.Impacts.effectSet(SkillEffects.DIVINE_STRENGTH.id.toString(), 8, 0);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.FLOAT)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(20).speed(0.05F, 0.1F).verticalOrigin(Batches.FEET)),
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), SkillsCommon.MIGHT_COLOR)
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_spell_1_modifier_2 = add(paladin_tier_2_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_2");
        var title = "Cleanse";
        var cleanseCount = 1;
        var description = "Flash Heal attempts to cure the target, by reducing the strength of a harmful effect.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FLASH_HEAL;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        var impact = SpellBuilder.Impacts.effectCleanse();
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_heal, ParticleGroup.Motion.DECELERATE)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(20).speed(0.25F, 0.3F))
        );
        impact.action.status_effect.amplifier = cleanseCount;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_1_modifier_1 = add(paladin_tier_3_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_1_modifier_1");
        var title = "Pursuit of Justice";
        var description = "Divine Protection also increases your movement speed by {bonus}, for {effect_duration} sec.";
        var effect = SkillEffects.PURSUIT_OF_JUSTICE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = DIVINE_PROTECTION;

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_1_modifier_2 = add(paladin_tier_3_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_1_modifier_2");
        var title = "Blessed Protection";
        var description = "Divine Protection provides {effect_amplifier_add} extra effect stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = DIVINE_PROTECTION;
        modifier.effect_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_2_modifier_1 = add(paladin_tier_3_spell_2_modifier_1());
    private static Skills.Entry paladin_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_2_modifier_1");
        var title = "Empowered Judgement";
        var description = "Increases the damage of Judgement by {power_multiplier}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = JUDGEMENT;
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.2F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_2_modifier_2 = add(paladin_tier_3_spell_2_modifier_2());
    private static Skills.Entry paladin_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_2_modifier_2");
        var title = "Judgement of Command";
        var description = "Judgement taunts enemies hit, forcing them to attack you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = JUDGEMENT;

        var impact = SpellBuilder.Impacts.taunt();
        impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_aggro.id(), Color.RAGE)
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_4_spell_1_modifier_1 = add(paladin_tier_4_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_1_modifier_1");
        var title = "Persistent Banner";
        var description = "Increases the duration of Battle Banner by {spawn_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BATTLE_BANNER;
        modifier.spawn_duration_add = 4;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_4_spell_1_modifier_2 = add(paladin_tier_4_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_1_modifier_2");
        var title = "Protective Banner";
        var description = "Battle Banner also reduces damage taken by {bonus}.";
        var effect = SkillEffects.BANNER_PROTECTION;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent( Math.abs( effect.config().firstModifier().value ) );
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BATTLE_BANNER;
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry paladin_tier_2_spell_1_root = add(SkillsCommon.powerRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_2_spell_1_root", FLASH_HEAL, "Flash Heal", 0.1F));
    public static final Skills.Entry paladin_tier_3_spell_1_root = add(SkillsCommon.lingerRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_3_spell_1_root", DIVINE_PROTECTION, "Divine Protection", 2F));
    public static final Skills.Entry paladin_tier_4_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_4_spell_1_root", BATTLE_BANNER, "Battle Banner", 5F));
    public static final Skills.Entry paladin_tier_2_spell_2_root = add(SkillsCommon.channelRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_2_spell_2_root", BLESSED_STRIKES, "Blessed Strikes", 1));
    public static final Skills.Entry paladin_tier_3_spell_2_root = add(SkillsCommon.critRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_3_spell_2_root", JUDGEMENT, "Judgement", 0.05F));
    public static final Skills.Entry paladin_tier_4_spell_2_root = add(SkillsCommon.radiusRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_4_spell_2_root", IMMOLATION, "Immolation", 1F));

    public static final Skills.Entry paladin_tier_2_spell_2_modifier_1 = add(paladin_tier_2_spell_2_modifier_1()); // Seal of Wrath
    private static Skills.Entry paladin_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_2_modifier_1");
        var title = "Seal of Wrath";
        var description = "Increases the damage of Blessed Strikes by {power_multiplier}.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        // Flat power boost to the seared holy damage — no buff, no icon: the sole tracked state
        // stays the seal count itself. {power_multiplier} auto-resolves from the value below.
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BLESSED_STRIKES;
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.2F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_spell_2_modifier_2 = add(paladin_tier_2_spell_2_modifier_2()); // Seal of Light
    private static Skills.Entry paladin_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_2_modifier_2");
        var title = "Seal of Light";
        var description = "Empowered strikes of Blessed Strikes also heal you for {heal}.";

        // HEALING school so the tooltip's {heal} estimation resolves against the same base
        // school the impact uses at runtime (Blessed Strikes' own school).
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BLESSED_STRIKES;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;

        // Joins the stashed payload: heals the paladin on each seal-spending strike. Same hybrid
        // power split as the base spell's damage (25% melee / 75% healing, crit blended alike).
        var heal = SpellBuilder.Impacts.heal(0.25F);
        heal.action.apply_to_caster = true;
        heal.power_blend = List.of(SpellBuilder.Impacts.powerBlend(
                ExternalSpellSchools.PHYSICAL_MELEE, 1F / 3F, true, true, true));
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_heal, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(8).speed(0.15F, 0.25F))
        );
        modifier.impacts = List.of(heal);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }
    public static final Skills.Entry paladin_tier_4_spell_2_modifier_1 = add(paladin_tier_4_spell_2_modifier_1());
    private static Skills.Entry paladin_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_2_modifier_1");
        var title = "Condemn";
        var description = "Immolation drags struck enemies towards you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = IMMOLATION;

        // Radial pull: -Z in the ORIGIN frame points towards the blast centre (the caster),
        // with a small upward pop so victims are lifted off their footing.
        var pull = SpellBuilder.Impacts.velocity(
                Spell.Impact.Action.Velocity.Frame.ORIGIN, new Vector3f(0, 0.3F, -0.6F));
        pull.action.velocity.reset_velocity = true;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(pull);

        // A converging ground ring at the caster, played with Immolation's release FX.
        modifier.release = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_678)
                        .scale(4.5F)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_4_spell_2_modifier_2 = add(paladin_tier_4_spell_2_modifier_2());
    private static Skills.Entry paladin_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_2_modifier_2");
        var title = "Consecration";
        var description = "Immolation consecrates the ground beneath you, dealing {damage} damage to enemies, for {cloud_duration} sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast(IMMOLATION);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        // The consecrated-ground cloud formerly attached to Circle of Healing, re-homed
        // here at Immolation's own radius.
        var radius = 5.0F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = radius;
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 5;
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.FLOAT)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(12).speed(0.05F, 0.1F).anchor(ParticleGroup.Anchor.GROUND)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.BURST)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(12).speed(0.05F, 0.15F).anchor(ParticleGroup.Anchor.GROUND)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(12).speed(0.05F, 0.15F).anchor(ParticleGroup.Anchor.GROUND).extent(radius))
        );
        spell.deliver.clouds = List.of(cloud);

        var impact = SpellBuilder.Impacts.damage(0.2F, 0.1F);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.BURST)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.4F, 0.4F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound(SkillSounds.priest_consecration_impact.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_1_passive_1 = add(paladin_tier_1_passive_1()); // Redoubt
    private static Skills.Entry paladin_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_1_passive_1");
        var title = "Redoubt";
        var description = "Blocking with shield grants {bonus} armor, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";

        var effect = SkillEffects.REDOUBT;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(SkillEffects.REDOUBT.id.toString(), 8, 1, 2);
        impact.action.apply_to_caster = true;
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(20).speed(0.2F, 0.3F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound(SkillSounds.paladin_redoubt.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_1_passive_2 = add(paladin_tier_1_passive_2()); // Vengeance
    private static Skills.Entry paladin_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_1_passive_2");
        var effect = SkillEffects.VENGEANCE;
        var title = "Vengeance";
        var description = "Critical strikes grant " + effect.title
                + ", increasing Attack Damage by {bonus}, stacking up to {effect_amplifier_cap} times, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        // Critical melee hits only — regular attacks and melee weapon skills alike.
        // No internal cooldown: every crit stacks and refreshes Vengeance.
        var attackTrigger = SpellBuilder.Triggers.meleeAttackImpact();
        attackTrigger.melee = new Spell.Trigger.MeleeCondition();
        attackTrigger.melee.critical = true;
        var skillTrigger = SpellBuilder.Triggers.meleeSkillImpact();
        skillTrigger.impact = new Spell.Trigger.ImpactCondition();
        skillTrigger.impact.critical = true;
        spell.passive.triggers = List.of(attackTrigger, skillTrigger);

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10, 1, 2);
        buff.action.apply_to_caster = true;
        buff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.2F, 0.3F).verticalOrigin(Batches.FEET))
        );
        // No impact particles: the Vengeance status effect renders its own ground decal
        // (see SkillTreeClientMod), so gaining a stack only chimes.
        buff.sound = Sound.withVolume(SkillSounds.paladin_crusader_activate.id(), 0.25F);
        spell.impacts = List.of(buff);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_passive_1 = add(paladin_tier_2_passive_1()); // Conviction
    private static Skills.Entry paladin_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_passive_1");
        var title = "Conviction";
        var description = "Upon rolling, you have {trigger_chance} chance to reset the cooldown of Blessed Strikes and Flash Heal.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        // Resets both tier 2 book spells — the retribution and the protection pick alike
        var impact = SpellBuilder.Impacts.resetCooldownActive(BLESSED_STRIKES);

        impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.HOLY),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.2F, 0.3F))
        );
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        var flashHealReset = SpellBuilder.Impacts.resetCooldownActive(FLASH_HEAL);
        spell.impacts = List.of(impact, flashHealReset);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Color FREEDOM_COLOR = Color.from(0xff9933);

    public static final Skills.Entry paladin_tier_2_passive_2 = add(paladin_tier_2_passive_2()); // Blessing of Freedom
    private static Skills.Entry paladin_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_passive_2");
        var title = "Blessing of Freedom";
        var description = "Rolling breaks you free, removing all movement impairing effects.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        // ALL-selector dispel: strips every harmful movement-impairing effect at once
        // (classification-based, so modded slows/snares are covered too)
        var impact = SpellBuilder.Impacts.effectRemoveMovementImpairing();
        impact.action.apply_to_caster = true;
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.FLOAT)
                        .color(FREEDOM_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.3F, 0.4F).verticalOrigin(Batches.FEET)),
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), FREEDOM_COLOR)
        );
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_passive_1 = add(paladin_tier_3_passive_1()); // Ardent Defender (hp boost on low HP)
    private static Skills.Entry paladin_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_passive_1");
        var effect = SkillEffects.ARDENT_DEFENDER;
        var title = "Ardent Defender";
        var healthThreshold = 0.3F;
        var description = "Upon taking damage below {threshold} health, your max health is increased by {bonus}, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(effect.config().firstModifier().value));
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{threshold}", SpellTooltip.percent(healthThreshold));
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.HEALTH;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger1 = SpellBuilder.Triggers.becomingLowHP(healthThreshold);
        trigger1.target_override = Spell.Trigger.TargetSelector.CASTER;
        var trigger2 = SpellBuilder.Triggers.damageIncomingFatal();
        trigger2.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger1, trigger2);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 0);
        buff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(30).speed(0.2F, 0.2F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(20).speed(0.3F, 0.3F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.FLOAT)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(30).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.ASCEND)
                        .color(Color.HOLY.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(30).speed(0.1F, 0.2F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_415)
                        .facing(ParticleGroup.Facing.CAMERA)
                        .scale(1.5F)
                        .color(Color.HOLY.toRGBA())
                        .batch(Batches.ground(1))
        );
        buff.sound = new Sound(SkillSounds.paladin_ardent_defender.id());
        var heal = SpellBuilder.Impacts.heal(0.5F);
        spell.impacts = List.of(buff, heal);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_passive_2 = add(paladin_tier_3_passive_2()); // Divine Hammer
    private static Skills.Entry paladin_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_passive_2");
        var title = "Divine Hammer";
        var description = "Melee attacks throw a hammer at the target, dealing {damage} damage, ricocheting {ricochet} to nearby enemies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 5;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        var triggers = SpellBuilder.Triggers.meleeImpact();
        for (var trigger : triggers) {
            trigger.chance = 1F;
        }
        spell.passive.triggers = triggers;

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.direct_towards_target = true;
        spell.deliver.projectile.launch_properties.velocity = 0.6F;
        spell.deliver.projectile.projectile = new Spell.ProjectileData();
        spell.deliver.projectile.projectile.perks = new Spell.ProjectileData.Perks();
        spell.deliver.projectile.projectile.perks.ricochet_range = 8F;
        spell.deliver.projectile.projectile.perks.ricochet = 2;
        spell.deliver.projectile.projectile.perks.bounce = 3;

        var model = SpellBuilder.ProjectileModels.model("paladins:spell_projectile/judgement", 0.8F, LightEmission.RADIATE);
        model.rotate_degrees_per_tick = 20F;

        spell.deliver.projectile.projectile.client_data = new Spell.ProjectileData.Client();
        spell.deliver.projectile.projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(model);


        // Same hybrid power split as Judgement: 75% melee / 25% healing
        // (base PHYSICAL_MELEE weighs 1, healing weighs 1/3).
        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        impact.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 1F / 3F));
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.BURST)
                        .color(SkillsCommon.HOLY_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.6F, 0.8F))
        );
        impact.sound = new Sound(SkillSounds.paladin_divine_hammer_impact.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }
}
