package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
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

public class PaladinSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

//    public static final Skills.Entry paladin_tier_1_spell_1_modifier_1 = add(paladin_tier_1_spell_1_modifier_1());
//    private static Skills.Entry paladin_tier_1_spell_1_modifier_1() {
//        var effect = SkillEffects.DIVINE_STRENGTH;
//
//        var id = Identifier.of(NAMESPACE, "paladin_tier_1_spell_1_modifier_1");
//        var title = "Divine Strength";
//        var description = "Flash Heal increases Attack Damage by {bonus} for {effect_duration} sec.";
//
//        SpellTooltip.DescriptionMutator mutator = (args) -> {
//            var modifier = effect.config().firstModifier();
//            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
//            return args.description().replace("{bonus}", bonus);
//        };
//
//        var spell = SpellBuilder.createSpellModifier();
//        spell.school = SpellSchools.HEALING;
//
//        var modifier = new Spell.Modifier();
//        modifier.spell_pattern = "paladins:flash_heal";
//
//        var impact = SpellBuilder.Impacts.effectSet(SkillEffects.DIVINE_STRENGTH.id.toString(), 8, 0);
//        impact.particles = new ParticleBatch[]{
//                new ParticleBatch(
//                        SpellEngineParticles.MagicParticles.get(
//                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
//                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
//                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
//                        20, 0.05F, 0.1F)
//                        .color(SkillsCommon.MIGHT_COLOR.toRGBA()),
//                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), SkillsCommon.MIGHT_COLOR)
//        };
//        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
//        modifier.impacts = List.of(impact);
//
//        spell.modifiers = List.of(modifier);
//
//        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
//    }

    public static final Skills.Entry paladin_tier_2_spell_1_modifier_1 = add(paladin_tier_2_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_1");
        var title = "Cleanse";
        var cleanseCount = 1;
        var description = "Flash Heal attempts to cure the target, by reducing the strength of a harmful effect.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:flash_heal";
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        var impact = SpellBuilder.Impacts.effectCleanse();
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SkillsCommon.HEAL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.25F, 0.3F
                ).color(Color.HOLY.toRGBA())
        };
        impact.action.status_effect.amplifier = cleanseCount;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

//    public static final Skills.Entry paladin_tier_2_spell_1_modifier_1 = add(paladin_tier_2_spell_1_modifier_1());
//    private static Skills.Entry paladin_tier_2_spell_1_modifier_1() {
//        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_1");
//        var title = "Pursuit of Justice";
//        var description = "Divine Protection also increases your movement speed by {bonus}, for {effect_duration} sec.";
//        var effect = SkillEffects.PURSUIT_OF_JUSTICE;
//        SpellTooltip.DescriptionMutator mutator = (args) -> {
//            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
//            return args.description().replace("{bonus}", bonus);
//        };
//
//        var spell = SpellBuilder.createSpellModifier();
//        spell.school = SpellSchools.HEALING;
//
//        var modifier = new Spell.Modifier();
//        modifier.spell_pattern = "paladins:divine_protection";
//
//        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);
//        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
//        modifier.impacts = List.of(impact);
//
//        spell.modifiers = List.of(modifier);
//
//        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
//    }

    public static final Skills.Entry paladin_tier_2_spell_1_modifier_2 = add(paladin_tier_2_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_2");
        var title = "Blessed Protection";
        var description = "Divine Protection provides {effect_amplifier_add} extra effect stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:divine_protection";
        modifier.effect_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_1_modifier_1 = add(paladin_tier_3_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_1_modifier_1");
        var title = "Empowered Judgement";
        var description = "Increases the damage of Judgement by {power_multiplier}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:judgement";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.2F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_1_modifier_2 = add(paladin_tier_3_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_1_modifier_2");
        var title = "Judgement of Command";
        var description = "Judgement taunts enemies hit, forcing them to attack you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:judgement";

        var impact = SpellBuilder.Impacts.taunt();
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_aggro.id(), Color.RAGE),
        };
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
        modifier.spell_pattern = "paladins:battle_banner";
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
        modifier.spell_pattern = "paladins:battle_banner";
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_1_passive_1 = add(paladin_tier_1_passive_1());
    private static Skills.Entry paladin_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_1_passive_1");
        var title = "Seal of Righteousness";
        var description = "Melee attacks have {trigger_chance_1} chance, to deal additional {damage} damage based on Healing Power.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var triggers = SpellBuilder.Triggers.meleeImpact();
        for (var trigger : triggers) {
            trigger.chance = 0.5F;
        }
        spell.passive.triggers = triggers;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.5F, 0.8F)
                        .color(SkillsCommon.HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.5F, 0.8F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SkillSounds.paladin_seal_impact.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_1_passive_2 = add(paladin_tier_1_passive_2());
    private static Skills.Entry paladin_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_1_passive_2");
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
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.3F)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
        };
        impact.sound = new Sound(SkillSounds.paladin_redoubt.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_passive_1 = add(paladin_tier_2_passive_1()); // Crusader Strike
    private static Skills.Entry paladin_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_passive_1");
        var title = "Crusader Strike";
        var debuffEffect = SkillEffects.CRUSADERS_MARK;
        var description = "Upon rolling, you have {trigger_chance_1} chance for your next melee attack to apply " + debuffEffect.title + ", increasing damage taken by {bonus}, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(debuffEffect.config().firstModifier().value));
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = new Sound(SkillSounds.paladin_crusader_activate.id());

        // Roll to stash

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.3F)
                        .color(SkillsCommon.HOLY_COLOR),
        };

        var stashEffect = SkillEffects.SEAL_OF_CRUSADER;
        var strashTrigger = SpellBuilder.Triggers.meleeAttackImpact();
        SpellBuilder.Deliver.stash(spell, stashEffect.id.toString(), 5, strashTrigger);

        var debuff = SpellBuilder.Impacts.effectAdd(debuffEffect.id.toString(), 15, 1, 2);
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.7F, 0.8F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        debuff.sound = new Sound(SkillSounds.paladin_crusader_impact.id());
        spell.impacts = List.of(debuff);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_passive_2 = add(paladin_tier_2_passive_2()); // Conviction
    private static Skills.Entry paladin_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_passive_2");
        var title = "Conviction";
        var description = "Upon rolling, you have {trigger_chance} chance to reset the cooldown of Divine Protection.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        //var impact = SpellBuilder.Impacts.resetCooldownActive("paladins:flash_heal"); // Used to be in place with 50% chance
        var impact = SpellBuilder.Impacts.resetCooldownActive("paladins:divine_protection");

        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.HOLY),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.3F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        spell.impacts = List.of(impact);

        // Cooldown applied due to resetting a strong defensive cooldown
        // Flash Heal wouldn't need this
        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_passive_1 = add(paladin_tier_3_passive_1()); // Divine Hammer
    private static Skills.Entry paladin_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_passive_1");
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

        var model = new Spell.ProjectileModel();
        model.light_emission = LightEmission.RADIATE;
        model.model_id = "paladins:spell_projectile/judgement";
        model.scale = 0.8F;
        model.rotate_degrees_per_tick = 20F;

        spell.deliver.projectile.projectile.client_data = new Spell.ProjectileData.Client();
        spell.deliver.projectile.projectile.client_data.model = model;


        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.6F, 0.8F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SkillSounds.paladin_divine_hammer_impact.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_passive_2 = add(paladin_tier_3_passive_2()); // Ardent Defender (hp boost on low HP)
    private static Skills.Entry paladin_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_passive_2");
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
        buff.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        30, 0.2F, 0.2F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        20, 0.3F, 0.3F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        30, 0.1F, 0.3F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        30, 0.1F, 0.2F)
                        .color(Color.HOLY.toRGBA()),
                SpellBuilder.Particles.area(SpellEngineParticles.aura_effect_415.id())
                        .scale(1.5F)
                        .color(Color.HOLY.toRGBA())
        };
        buff.sound = new Sound(SkillSounds.paladin_ardent_defender.id());
        var heal = SpellBuilder.Impacts.heal(0.5F);
        spell.impacts = List.of(buff, heal);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }
}
