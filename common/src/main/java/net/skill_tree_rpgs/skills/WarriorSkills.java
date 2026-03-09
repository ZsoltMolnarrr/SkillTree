package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
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

public class WarriorSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Skills.Entry warrior_tier_2_spell_1_modifier_1 = add(warrior_tier_2_spell_1_modifier_1());
    private static Skills.Entry warrior_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_spell_1_modifier_1");
        var title = "Bouncing Throw";
        var description = "Shattering Throw ricochets to {ricochet} additional target.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:throw";
        modifier.projectile_perks = Spell.ProjectileData.Perks.EMPTY();
        modifier.projectile_perks.ricochet = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_spell_1_modifier_2 = add(warrior_tier_2_spell_1_modifier_2());
    private static Skills.Entry warrior_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_spell_1_modifier_2");
        var title = "Punching Throw";
        var description = "Shattering Throw deals {knockback_multiply_base} more knockback.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var bonus = 0.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:throw";
        modifier.knockback_multiply_base = bonus;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_spell_1_modifier_1 = add(warrior_tier_3_spell_1_modifier_1());
    private static Skills.Entry warrior_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_spell_1_modifier_1");
        var title = "Battle Shout";
        var description = "Shout increases Attack Damage of allies by {bonus}, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        spell.tooltip.show_range = false;

        var effect = SkillEffects.BATTLE_SHOUT;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 12;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.include_caster = true;

        var trigger = SpellBuilder.Triggers.specificSpellCast("rogues:shout");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 6, 0);
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), Color.RAGE)
        };
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_spell_1_modifier_2 = add(warrior_tier_3_spell_1_modifier_2());
    private static Skills.Entry warrior_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_spell_1_modifier_2");
        var title = "Challenging Shout";
        var description = "Shout taunts all affected enemies.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:shout";

        var impact = SpellBuilder.Impacts.taunt();
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), Color.RAGE)
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_4_spell_1_modifier_1 = add(warrior_tier_4_spell_1_modifier_1());
    private static Skills.Entry warrior_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_4_spell_1_modifier_1");
        var title = "Endurance";
        var description = "Charge lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:charge";
        modifier.effect_duration_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_4_spell_1_modifier_2 = add(warrior_tier_4_spell_1_modifier_2());
    private static Skills.Entry warrior_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_4_spell_1_modifier_2");
        var title = "Concussion Blow";
        var description = "Next attack after using Charge, stuns the target for {effect_duration} sec.";
        var stashEffect = SkillEffects.CONCUSSION_BLOW;

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.specificSpellCast("rogues:charge");
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.triggers = List.of(
                SpellBuilder.Triggers.meleeAttackImpact());
        spell.deliver.stash_effect.consumed_next_tick = true;

        var impact = SpellBuilder.Impacts.stun(2F);
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_1_passive_1 = add(warrior_tier_1_passive_1());
    private static Skills.Entry warrior_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_1_passive_1");
        var title = "Killing Spree";
        var description = "Killing an enemy increases Attack Damage by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var effect = SkillEffects.KILLING_SPREE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.passive.triggers = SpellBuilder.Triggers.meleeKills();

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.3F)
                        .color(Color.RAGE.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_1_passive_2 = add(warrior_tier_1_passive_2());
    private static Skills.Entry warrior_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_1_passive_2");
        var effect = SkillEffects.VITALITY;
        var title = "Vitality";
        var description = "Blocking with shield has {trigger_chance} chance to increase your Evasion Chance by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        trigger.chance = 0.5F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.3F)
                        .color(Color.NATURE.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_passive_1 = add(warrior_tier_2_passive_1());
    private static Skills.Entry warrior_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_passive_1");
        var title = "Intercept";
        var description = "Upon rolling, you have {trigger_chance} chance to reset the cooldown of Charge.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive("rogues:charge");
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.RAGE)
        };
        impact.action.apply_to_caster = true;
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_passive_2 = add(warrior_tier_2_passive_2());
    private static Skills.Entry warrior_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_passive_2");
        var title = "Second Wind";
        var description = "Upon rolling, you have {trigger_chance} chance to restore 10%% of your total health.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.HEALTH;   // power = max HP → heal(0.1) = 10% HP
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.action.apply_to_caster = true;
        impact.particles = SkillsCommon.leechImpactParticles();
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_passive_1 = add(warrior_tier_3_passive_1()); // Enrage (on damage taken, gain Enrage effect)
    private static Skills.Entry warrior_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_passive_1");
        var effect = SkillEffects.ENRAGE;
        var title = effect.title;
        var description = "Taking damage has {trigger_chance_1} chance to apply Enrage effect, increasing your Size and Attack Speed by {bonus} but also the damage you take, stacking up to {effect_amplifier_cap} times, lasting {stash_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.damageTaken();
        trigger.chance = 0.25F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var activateParticles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.3F, 0.5F)
                        .color(Color.RAGE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.3F, 0.5F)
                        .invert()
                        .color(Color.RAGE.toRGBA()),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .origin(ParticleBatch.Origin.CENTER)
                        .scale(1.5F)
                        .color(Color.RAGE.toRGBA())
        };

        spell.release.particles = activateParticles;
        spell.release.sound = new Sound(SkillSounds.warrior_enrage.id());

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 10F, List.of(
                SpellBuilder.Triggers.damageTaken()
        ));
        spell.deliver.stash_effect.consume = 0;

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10F, 1, 2);
        buff.action.apply_to_caster = true;
        buff.action.status_effect.refresh_duration = false;
        buff.particles = activateParticles;
        // buff.sound = new Sound(SkillTreeSounds.warrior_enrage.id());
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_passive_2 = add(warrior_tier_3_passive_2()); // Shockwave (like Ardent Defender)
    private static Skills.Entry warrior_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_passive_2");
        var title = "Shockwave";
        float healthThreshold = 0.3F;
        float radius = 5F;
        var description = "Taking damage below {threshold} causes a shockwave, stunning enemies nearby for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = radius;

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var threshold = SpellTooltip.percent(healthThreshold);
            return args.description().replace("{threshold}", threshold);
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.6F, 0.8F),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        20, 0.4F, 0.4F),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        20, 0.6F, 0.6F),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .scale(radius * 0.8F)
                        .color(Color.from(0xe6e6e6).toRGBA()),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .scale(radius)
                        .color(Color.from(0xa6a6a6).toRGBA())
        };
        spell.release.sound = new Sound(SkillSounds.warrior_shockwave.id());

        var trigger = SpellBuilder.Triggers.becomingLowHP(healthThreshold);
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var stun = SpellBuilder.Impacts.effectSet(SpellEngineEffects.STUN.id.toString(), 4, 0);
        stun.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.2F, 0.3F)
        };
        spell.impacts = List.of(stun);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }
}
