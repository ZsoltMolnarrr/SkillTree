package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.utils.RegistryIds;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
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

    public static final String SLICE_AND_DICE = "rogues:slice_and_dice";
    public static final String SHOCK_POWDER = "rogues:shock_powder";
    public static final String SHADOW_STEP = "rogues:shadow_step";
    public static final String VANISH = "rogues:vanish";
    public static final String BEAR_TRAP = "rogues:bear_trap";
    public static final String MUTILATE = "rogues:mutilate";
    public static final String ROGUE_SPELL_TAG = "#rogues:rogue";

    public static final Skills.Entry rogue_tier_2_spell_1_modifier_1 = add(rogue_tier_2_spell_1_modifier_1());
    private static Skills.Entry rogue_tier_2_spell_1_modifier_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_spell_1_modifier_1");
        var title = "Explosive Powder";
        var description = "Shock Powder has {trigger_chance} chance to create secondary explosions dealing {damage} damage, and its stun lasts 1 sec longer.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit(SHOCK_POWDER);
        trigger.impact.impact_type = null;
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        SkillsCommon.explosionImpact(spell, 0.6F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    /// Hidden companion to Explosive Powder: the node grants this MODIFIER spell alongside the
    /// passive above (a passive spell's `modifiers` are never collected — only MODIFIER-type
    /// spells are — so the stun extension needs its own spell in a second container).
    public static final Skills.Entry rogue_tier_2_spell_1_modifier_1_bonus = add(rogue_tier_2_spell_1_modifier_1_bonus());
    private static Skills.Entry rogue_tier_2_spell_1_modifier_1_bonus() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_spell_1_modifier_1_bonus");
        var title = "Explosive Powder";
        var description = "Shock Powder's stun lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SHOCK_POWDER;
        modifier.effect_duration_add = 1F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_spell_1_modifier_2 = add(rogue_tier_2_spell_1_modifier_2());
    private static Skills.Entry rogue_tier_2_spell_1_modifier_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_spell_1_modifier_2");
        var title = "Smoke Screen";
        var effect = SkillEffects.SMOKE_SCREEN;
        var description = "Shock Powder leaves a smoke screen behind for {cloud_duration} sec, increasing evasion chance of allies inside by " + TooltipTokens.effect(SkillEffects.SMOKE_SCREEN.id) + ".";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast(SHOCK_POWDER);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        // A lingering smoke cloud matching Shock Powder's 5 block radius, refreshing a short
        // evasion buff on allies inside it every half second.
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 5F;
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 5;
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_large)
                        .color(Color.from(0x999999).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(3).speed(0.01F, 0.05F).anchor(ParticleGroup.Anchor.GROUND))
        );
        spell.deliver.clouds = List.of(cloud);

        var evasion = SpellBuilder.Impacts.effectSet(effect.id.toString(), 1, 0);
        spell.impacts = List.of(evasion);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_spell_2_modifier_1 = add(rogue_tier_2_spell_2_modifier_1());
    private static Skills.Entry rogue_tier_2_spell_2_modifier_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_spell_2_modifier_1");
        var title = "Blade Fury";
        var description = "Increases the maximum number of Slice and Dice stacks by {effect_amplifier_cap_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SLICE_AND_DICE;
        modifier.effect_amplifier_cap_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_spell_2_modifier_2 = add(rogue_tier_2_spell_2_modifier_2());
    private static Skills.Entry rogue_tier_2_spell_2_modifier_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_spell_2_modifier_2");
        var title = "Fleet Footed";
        var effect = SkillEffects.FLEET_FOOTED;
        var description = "Slice and Dice attacks increases movement speed by " + TooltipTokens.effect(SkillEffects.FLEET_FOOTED.id) + ", stacking up to {effect_amplifier_cap}, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SLICE_AND_DICE;

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 4, 1, 4);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_spell_1_modifier_1 = add(rogue_tier_3_spell_1_modifier_1());
    private static Skills.Entry rogue_tier_3_spell_1_modifier_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_3_spell_1_modifier_1");
        var title = "Cloak of Shadows";
        var description = "Shadowstep grants you Cloak of Shadows effect, protecting your from {effect_amplifier} incoming attack for {effect_duration} sec.";

        var effect = SkillEffects.CLOAK_OF_SHADOWS;

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SHADOW_STEP;

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 0, 1);
        impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.aura(SpellEngineParticles.area_effect_538.id())
                        .appearance(a -> a.scale(1.2F).color(ROGUE_SHADOW_COLOR.alpha(0.75F).toRGBA()))
        );
        impact.action.apply_to_caster = true;
        impact.sound = new Sound(SkillSounds.rogue_shadows_activate.id());

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_spell_1_modifier_2 = add(rogue_tier_3_spell_1_modifier_2());
    private static Skills.Entry rogue_tier_3_spell_1_modifier_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_3_spell_1_modifier_2");
        var title = "Ambush";
        var description = "Next attack after Shadowstep, within {effect_duration} sec, deals " + TooltipTokens.effect(SkillEffects.AMBUSH.id) + " extra damage.";

        var effect = SkillEffects.AMBUSH;

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SHADOW_STEP;

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 5, 0);
        impact.action.apply_to_caster = true;

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_4_spell_1_modifier_1 = add(rogue_tier_4_spell_1_modifier_1());
    private static Skills.Entry rogue_tier_4_spell_1_modifier_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_4_spell_1_modifier_1");
        var title = "Stealth Speed";
        var description = "Stealth no longer slows you down.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = VANISH;

        var impact = SpellBuilder.Impacts.effectSet("rogues:stealth_speed", 8, 0);
        impact.action.apply_to_caster = true;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_4_spell_1_modifier_2 = add(rogue_tier_4_spell_1_modifier_2());
    private static Skills.Entry rogue_tier_4_spell_1_modifier_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_4_spell_1_modifier_2");
        var title = "Deep Stealth";
        var description = "Increases the duration of Stealth by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = VANISH;
        modifier.effect_duration_add = 8;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry rogue_tier_2_spell_1_root = add(SkillsCommon.lingerRoot(
            Skills.Category.ROGUE, ExternalSpellSchools.PHYSICAL_MELEE,
            "rogue_tier_2_spell_1_root", SHOCK_POWDER, "Shock Powder", 1F));
    public static final Skills.Entry rogue_tier_3_spell_1_root = add(SkillsCommon.reachRoot(
            Skills.Category.ROGUE, ExternalSpellSchools.PHYSICAL_MELEE,
            "rogue_tier_3_spell_1_root", SHADOW_STEP, "Shadow Step", 3F));
    public static final Skills.Entry rogue_tier_4_spell_1_root = add(SkillsCommon.lingerRoot(
            Skills.Category.ROGUE, ExternalSpellSchools.PHYSICAL_MELEE,
            "rogue_tier_4_spell_1_root", VANISH, "Vanish", 2F));
    public static final Skills.Entry rogue_tier_2_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.ROGUE, ExternalSpellSchools.PHYSICAL_MELEE,
            "rogue_tier_2_spell_2_root", SLICE_AND_DICE, "Slice and Dice", 2F));
    public static final Skills.Entry rogue_tier_3_spell_2_root = add(SkillsCommon.critRoot(
            Skills.Category.ROGUE, ExternalSpellSchools.PHYSICAL_MELEE,
            "rogue_tier_3_spell_2_root", BEAR_TRAP, "Bear Trap", 0.05F));
    public static final Skills.Entry rogue_tier_4_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.ROGUE, ExternalSpellSchools.PHYSICAL_MELEE,
            "rogue_tier_4_spell_2_root", MUTILATE, "Mutilate", 2F));

    // ===================================================================================
    // Powerful mutex nodes for the second spell of tiers 3 and 4 (spell_2):
    // Bear Trap (T3), Mutilate (T4).
    // ===================================================================================

    public static final Skills.Entry rogue_tier_3_spell_2_modifier_1 = add(rogue_tier_3_spell_2_modifier_1());
    private static Skills.Entry rogue_tier_3_spell_2_modifier_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_3_spell_2_modifier_1");
        var title = "Serrated Traps";
        var description = "Sprung traps cause their victim to Bleed for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BEAR_TRAP;

        // The same Bleed Mortal Strike applies, amplifier scaled by power.
        var bleed = SpellBuilder.Impacts.effectSet_ScaledAmplifier(
                SpellEngineEffects.BLEED.id.toString(), 6, 1, 0.25F);
        bleed.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.dripping_blood)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(40).speed(0.2F, 0.4F))
        );
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(bleed);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_spell_2_modifier_2 = add(rogue_tier_3_spell_2_modifier_2());
    private static Skills.Entry rogue_tier_3_spell_2_modifier_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_3_spell_2_modifier_2");
        var title = "Extensive Coverage";
        var description = "Bear Trap places 3 additional traps at twice the distance, rotated between the inner ones.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BEAR_TRAP;

        // An outer ring at double the base radius, rotated so the traps fall between the
        // built-in ones (the base ring sits at 0/120/240 — a symmetric 3-ring repeats every
        // 120, so 60 is the rotated formation). Delays continue the base placement cascade.
        var outer = SpellBuilder.Placements.ring(3, 4F, 60F, SpellBuilder.Placements.template());
        for (int i = 0; i < outer.size(); i++) {
            outer.get(i).delay_ticks = 9 + i * 3;
        }
        modifier.additional_placements = outer;

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_4_spell_2_modifier_1 = add(rogue_tier_4_spell_2_modifier_1());
    private static Skills.Entry rogue_tier_4_spell_2_modifier_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_4_spell_2_modifier_1");
        var title = "Crimson Strikes";
        var description = "Mutilate heals you for {heal} per enemy struck.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = MUTILATE;

        // The self-heal Mutilate used to have as part of its base kit, now opt-in here.
        var leech = SpellBuilder.Impacts.heal(0.1F);
        leech.action.apply_to_caster = true;
        leech.visuals = SkillsCommon.leechImpactParticles();
        leech.sound = Sound.of(SpellEngineSounds.LEECHING_IMPACT.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(leech);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_4_spell_2_modifier_2 = add(rogue_tier_4_spell_2_modifier_2());
    private static Skills.Entry rogue_tier_4_spell_2_modifier_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_4_spell_2_modifier_2");
        var title = "Envenom";
        var description = "Mutilate applies a stack of Poison lasting {effect_duration} sec, stacking up based on your attack damage.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = MUTILATE;

        // Same additive poison convention as the Coated Blades passive (power-scaled stack
        // cap), so the two build the same poison together.
        var poison = SpellBuilder.Impacts.effectAdd(RegistryIds.effect(StatusEffects.POISON), 8, 1, 1);
        poison.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
        poison.visuals = SkillsCommon.poisonImpactParticles();
        poison.sound = new Sound(SpellEngineSounds.GENERIC_POISON_IMPACT.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(poison);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_1_passive_1 = add(rogue_tier_1_passive_1());
    private static Skills.Entry rogue_tier_1_passive_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_1_passive_1");
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

        var impact = SpellBuilder.Impacts.effectAdd(RegistryIds.effect(StatusEffects.POISON), 8, 1, 1);
        impact.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
        impact.visuals = SkillsCommon.poisonImpactParticles();
        impact.sound = new Sound(SpellEngineSounds.GENERIC_POISON_IMPACT.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_1_passive_2 = add(rogue_tier_1_passive_2());
    private static Skills.Entry rogue_tier_1_passive_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_1_passive_2");
        var effect = SkillEffects.FRACTURE;
        var title = effect.title;
        var description = "Melee attacks have {trigger_chance_1} chance to wound the enemy, dealing {damage} damage and reducing its armor by " + TooltipTokens.effect(SkillEffects.FRACTURE.id, 0, null, TooltipTokens.Format.ABS) + ", for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();

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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST)
                        .color(Color.BLOOD.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(40).speed(0.5F, 0.8F)),
                SpellBuilder.Particles.aura(SpellEngineParticles.area_effect_409.id())
                        .appearance(a -> a.color(Color.BLOOD.toRGBA()))
        );
        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 6, 1, 1);
        debuff.sound = new Sound(SkillSounds.rogue_fracture_impact.id());
        spell.impacts = List.of(damage, debuff);

        SpellBuilder.Cost.cooldown(spell, 6F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_passive_2 = add(rogue_tier_2_passive_2()); // Opportunist (upon roll, next melee attack crits)
    private static Skills.Entry rogue_tier_2_passive_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_passive_2");
        var effect = SkillEffects.OPPORTUNIST;
        var title = effect.title;
        var description = "Upon rolling, you have {trigger_chance_1} chance for your next melee attack within 5 sec to be a guaranteed critical strike.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_crit.id(), Color.from(0xffcc66)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.from(0xcc2900).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(25).speed(0.3F, 0.5F).verticalOrigin(Batches.FEET))
        );
        spell.release.sound = new Sound(SpellEngineSounds.SIGNAL_SPELL_CRIT.id());

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 5, SpellBuilder.Triggers.meleeAttackImpact());
        spell.deliver.stash_effect.consumed_next_tick = true;

        spell.impacts = List.of();

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_2_passive_1 = add(rogue_tier_2_passive_1());
    private static Skills.Entry rogue_tier_2_passive_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_2_passive_1");
        var effect = SkillEffects.SIDE_STEP;
        var title = effect.title;
        var description = "Upon rolling, you gain a stack of Sidestep, increasing your Evasion Chance by " + TooltipTokens.effect(SkillEffects.SIDE_STEP.id) + ", stacking up to {stash_amplifier} times. Removed when taking damage.";


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

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_passive_1 = add(rogue_tier_3_passive_1()); // Cheat Death
    private static Skills.Entry rogue_tier_3_passive_1() {
        var id = new Identifier(NAMESPACE, "rogue_tier_3_passive_1");
        var effect = SkillEffects.CHEAT_DEATH;
        var title = effect.title;
        var description = "Protects you from an attack that would be fatal, and you become invulnerable for {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.damageIncomingFatal();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 3, 0);
        buff.action.apply_to_caster = true;
        buff.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.aura(SpellEngineParticles.area_effect_728.id())
                        .appearance(a -> a.scale(1.2F).color(ROGUE_SHADOW_COLOR.alpha(0.5F).toRGBA()))
        );
        buff.sound = new Sound(SkillSounds.rogue_cheat_death.id());
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }

    public static final Skills.Entry rogue_tier_3_passive_2 = add(rogue_tier_3_passive_2()); // Preparation (reset all cooldowns on evade)
    private static Skills.Entry rogue_tier_3_passive_2() {
        var id = new Identifier(NAMESPACE, "rogue_tier_3_passive_2");
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

        var impact = SpellBuilder.Impacts.resetCooldownActive(ROGUE_SPELL_TAG);
        impact.action.apply_to_caster = true;
        impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), SkillsCommon.MIGHT_COLOR),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(25).speed(0.3F, 0.4F))
        );
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.ROGUE));
    }
}
