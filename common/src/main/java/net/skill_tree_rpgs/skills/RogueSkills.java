package net.skill_tree_rpgs.skills;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
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

public class RogueSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Color ROGUE_SHADOW_COLOR = Color.from(0x6600FF);

//    public static final Skills.Entry rogue_tier_1_spell_1_modifier_1 = add(rogue_tier_1_spell_1_modifier_1());
//    private static Skills.Entry rogue_tier_1_spell_1_modifier_1() {
//        var id = Identifier.of(NAMESPACE, "rogue_tier_1_spell_1_modifier_1");
//        var title = "Fleet Footed";
//        var effect = SkillEffects.FLEET_FOOTED;
//        var description = "Slice and Dice attacks increases movement speed by {bonus}, stacking up to {effect_amplifier_cap}, lasting {effect_duration} sec.";
//        SpellTooltip.DescriptionMutator mutator = (args) -> {
//            var modifier = effect.config().firstModifier();
//            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
//            return args.description().replace("{bonus}", bonus);
//        };
//        var spell = SpellBuilder.createSpellModifier();
//        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
//
//        var modifier = new Spell.Modifier();
//        modifier.spell_pattern = "rogues:slice_and_dice";
//
//        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 4, 1, 4);
//        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
//        modifier.impacts = List.of(impact);
//
//        spell.modifiers = List.of(modifier);
//
//        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ROGUE));
//    }

    public static final Skills.Entry rogue_tier_2_spell_1_modifier_1 = add(rogue_tier_2_spell_1_modifier_1());
    private static Skills.Entry rogue_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_2_spell_1_modifier_1");
        var title = "Blade Fury";
        var description = "Increases the maximum number of Slice and Dice stacks by {effect_amplifier_cap_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:slice_and_dice";
        modifier.effect_amplifier_cap_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

//    public static final Skills.Entry rogue_tier_2_spell_1_modifier_1 = add(rogue_tier_2_spell_1_modifier_1());
//    private static Skills.Entry rogue_tier_2_spell_1_modifier_1() {
//        var id = Identifier.of(NAMESPACE, "rogue_tier_2_spell_1_modifier_1");
//        var title = "Toxic Shock";
//        var description = "Shock Powder deals extra {damage} damage to poisoned targets.";
//
//        var spell = SpellBuilder.createSpellModifier();
//        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
//
//        var modifier = new Spell.Modifier();
//        modifier.spell_pattern = "rogues:shock_powder";
//
//        var impact = SpellBuilder.Impacts.damage(0.6F, 0F);
//        SpellBuilder.configureImpactEnableCondition(impact,
//                SpellBuilder.TargetConditions.ofPredicate(SpellEntityPredicates.IS_POISONED));
//        impact.particles = new ParticleBatch[] {
//                new ParticleBatch(SpellEngineParticles.smoke_large.id().toString(),
//                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
//                        5, 0.01F, 0.05F)
//                        .color(Color.POISON_LIGHT.toRGBA()),
//                new ParticleBatch(SpellEngineParticles.smoke_large.id().toString(),
//                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
//                        5, 0.01F, 0.05F)
//                        .color(Color.POISON_MID.toRGBA()),
//        };
//        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
//        modifier.impacts = List.of(impact);
//
//        spell.modifiers = List.of(modifier);
//
//        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
//    }

    public static final Skills.Entry rogue_tier_2_spell_1_modifier_2 = add(rogue_tier_2_spell_1_modifier_2());
    private static Skills.Entry rogue_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_2_spell_1_modifier_2");
        var title = "Explosive Powder";
        var description = "Shock Powder has {trigger_chance} chance to create secondary explosions, dealing {damage} damage.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit("rogues:shock_powder");
        trigger.impact.impact_type = null;
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        SkillsCommon.explosionImpact(spell, 0.6F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_spell_1_modifier_1 = add(rogue_tier_3_spell_1_modifier_1());
    private static Skills.Entry rogue_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_3_spell_1_modifier_1");
        var title = "Cloak of Shadows";
        var description = "Shadowstep grants you Cloak of Shadows effect, protecting your from {effect_amplifier} incoming attack for {effect_duration} sec.";

        var effect = SkillEffects.CLOAK_OF_SHADOWS;

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:shadow_step";

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 0, 1);
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.aura(SpellEngineParticles.aura_effect_538.id())
                        .scale(1.2F)
                        .color(ROGUE_SHADOW_COLOR.alpha(0.75F).toRGBA())
        };
        impact.action.apply_to_caster = true;
        impact.sound = new Sound(SkillSounds.rogue_shadows_activate.id());

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_spell_1_modifier_2 = add(rogue_tier_3_spell_1_modifier_2());
    private static Skills.Entry rogue_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_3_spell_1_modifier_2");
        var title = "Ambush";
        var description = "Next attack after Shadowstep, within {effect_duration} sec, deals {bonus} extra damage.";

        var effect = SkillEffects.AMBUSH;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:shadow_step";

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 5, 0);
        impact.action.apply_to_caster = true;

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_4_spell_1_modifier_1 = add(rogue_tier_4_spell_1_modifier_1());
    private static Skills.Entry rogue_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_4_spell_1_modifier_1");
        var title = "Stealth Speed";
        var description = "Stealth no longer slows you down.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:vanish";

        var impact = SpellBuilder.Impacts.effectSet("rogues:stealth_speed", 8, 0);
        impact.action.apply_to_caster = true;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_4_spell_1_modifier_2 = add(rogue_tier_4_spell_1_modifier_2());
    private static Skills.Entry rogue_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_4_spell_1_modifier_2");
        var title = "Deep Stealth";
        var description = "Increases the duration of Stealth by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:vanish";
        modifier.effect_duration_add = 8;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_1_passive_1 = add(rogue_tier_1_passive_1());
    private static Skills.Entry rogue_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_1_passive_1");
        var title = "Coated Blades";
        var description = "Melee attacks have {trigger_chance_1} chance, to apply poison effect lasting {effect_duration} sec, stacking up based on your attack damage.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var triggers = SpellBuilder.Triggers.meleeImpact();
        for (var trigger : triggers) {
            trigger.chance = 0.2F;
        }
        spell.passive.triggers = triggers;

        var impact = SpellBuilder.Impacts.effectAdd(StatusEffects.POISON.getIdAsString(), 8, 1, 1);
        impact.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
        impact.particles = SkillsCommon.poisonImpactParticles();
        impact.sound = new Sound(SpellEngineSounds.GENERIC_POISON_IMPACT.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_1_passive_2 = add(rogue_tier_1_passive_2());
    private static Skills.Entry rogue_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_1_passive_2");
        var effect = SkillEffects.FRACTURE;
        var title = effect.title;
        var description = "Melee attacks have {trigger_chance_1} chance to wound the enemy, dealing {damage} damage and reducing its armor by {bonus}, for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(effect.config().firstModifier().value));
            return args.description().replace("{bonus}", bonus);
        };

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver.delay = 1;

        var triggers = SpellBuilder.Triggers.meleeImpact();
        for (var trigger : triggers) {
            trigger.chance = 0.25F;
        }
        spell.passive.triggers = triggers;

        var damage = SpellBuilder.Impacts.damage(0.5F, 0F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.5F, 0.8F)
                        .color(Color.BLOOD.toRGBA()),
                SpellBuilder.Particles.aura(SpellEngineParticles.aura_effect_409.id())
                        .color(Color.BLOOD.toRGBA())
        };
        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 6, 1, 1);
        debuff.sound = new Sound(SkillSounds.rogue_fracture_impact.id());
        spell.impacts = List.of(damage, debuff);

        SpellBuilder.Cost.cooldown(spell, 6F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_passive_2 = add(rogue_tier_2_passive_2()); // Leeching Strike (upon roll, next attack life steal)
    private static Skills.Entry rogue_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_2_passive_2");
        var effect = SkillEffects.LEECHING_STRIKE;
        var title = effect.title;
        var description = "Upon rolling, you have {trigger_chance_1} chance for your next melee attack to heal you by {heal}.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.3F)
                        .color(Color.BLOOD.toRGBA())
        };

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 5, SpellBuilder.Triggers.meleeAttackImpact());

        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.action.apply_to_caster = true;
        impact.particles = SkillsCommon.leechImpactParticles();
        impact.sound = new Sound(SpellEngineSounds.LEECHING_IMPACT.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_passive_1 = add(rogue_tier_2_passive_1());
    private static Skills.Entry rogue_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_2_passive_1");
        var effect = SkillEffects.SIDE_STEP;
        var title = effect.title;
        var description = "Upon rolling, you gain a stack of Sidestep, increasing your Evasion Chance by {bonus}, stacking up to {stash_amplifier} times. Removed when taking damage.";

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.release.sound = new Sound(SkillSounds.rogue_sidestep_activate.id());
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        var stacks = 5;
        var stashTrigger = SpellBuilder.Triggers.damageTaken();
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 12, stashTrigger);
        spell.deliver.stash_effect.amplifier = stacks - 1;
        spell.deliver.stash_effect.stacking = true;
        spell.deliver.stash_effect.consume = stacks;
        spell.deliver.stash_effect.consumed_next_tick = true;
        spell.deliver.stash_effect.consume_any_stacks = true;

        var impact = SpellBuilder.Impacts.effectRemove(effect.id.toString());
        impact.action.apply_to_caster = true;
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_passive_1 = add(rogue_tier_3_passive_1()); // Cheat Death
    private static Skills.Entry rogue_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_3_passive_1");
        var effect = SkillEffects.CHEAT_DEATH;
        var title = effect.title;
        var description = "When taking damage that would be fatal, you become invulnerable for {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.damageIncomingFatal();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 3, 0);
        buff.action.apply_to_caster = true;
        buff.particles = new ParticleBatch[]{
                SpellBuilder.Particles.aura(SpellEngineParticles.aura_effect_728.id())
                        .scale(1.2F)
                        .color(ROGUE_SHADOW_COLOR.alpha(0.5F).toRGBA())
        };
        buff.sound = new Sound(SkillSounds.rogue_cheat_death.id());
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_passive_2 = add(rogue_tier_3_passive_2()); // Preparation (reset all cooldowns on evade)
    private static Skills.Entry rogue_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "rogue_tier_3_passive_2");
        var title = "Preparation";
        var description = "Upon evading an attack, you have {trigger_chance} chance for all your cooldowns to reset.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.evade();
        trigger.chance = 0.25F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive("#rogues:rogue");
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), SkillsCommon.MIGHT_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        25, 0.3F, 0.4F)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
        };
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ROGUE));
    }
}
