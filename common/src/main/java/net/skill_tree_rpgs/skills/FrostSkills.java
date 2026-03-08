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

public class FrostSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Skills.Entry frost_tier_2_spell_1_modifier_1 = add(frost_tier_2_spell_1_modifier_1());
    private static Skills.Entry frost_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_2_spell_1_modifier_1");
        var title = "Frost Splinters";
        var description = "Frost Nova causes secondary explosions, dealing {damage} damage to nearby enemies.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.deliver.delay = 7;

        var trigger = SpellBuilder.Triggers.specificSpellHit("wizards:frost_nova");
        spell.passive.triggers = List.of(trigger);

        var radius = 3.0F;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0.2F);
        var area_impact = new Spell.AreaImpact();
        area_impact.force_indirect = true;
        area_impact.radius = radius;
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        30, 0.4F, 0.4F),
                new ParticleBatch(
                        SpellEngineParticles.area_effect_293.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0,0)
                        .scale(radius - 0.5F)
                        .color(Color.FROST.toRGBA())
        };
        area_impact.sound = new Sound("wizards:frost_nova_damage_impact");
        spell.area_impact = area_impact;
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_2_spell_1_modifier_2 = add(frost_tier_2_spell_1_modifier_2());
    private static Skills.Entry frost_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_2_spell_1_modifier_2");
        var title = "Deep Freeze";
        var description = "Frost Nova applies {effect_amplifier_add} more stack of Freeze effect.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_nova";
        modifier.effect_amplifier_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_3_spell_1_modifier_1 = add(frost_tier_3_spell_1_modifier_1());
    private static Skills.Entry frost_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_3_spell_1_modifier_1");
        var title = "Nimble Shield";
        var description = "Allows normal movement speed during the effect of Frost Shield.";
        var effect = SkillEffects.FROST_SHIELD_SPEED;

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_shield";
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 8F, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_3_spell_1_modifier_2 = add(frost_tier_3_spell_1_modifier_2());
    private static Skills.Entry frost_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_3_spell_1_modifier_2");
        var title = "Durable Shield";
        var description = "Increases the duration of Frost Shield by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_shield";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_4_spell_1_modifier_1 = add(frost_tier_4_spell_1_modifier_1());
    private static Skills.Entry frost_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_4_spell_1_modifier_1");
        var title = "Hail Storm";
        var description = "Blizzard damage increased by {power_multiplier}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_blizzard";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.2F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_4_spell_1_modifier_2 = add(frost_tier_4_spell_1_modifier_2());
    private static Skills.Entry frost_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_4_spell_1_modifier_2");
        var title = "Snow Storm";
        var description = "Blizzard applies Slowness for {effect_duration} sec, stacking up to {effect_amplifier_cap} times.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        var effect = SkillEffects.BLIZZARD_SLOW;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_blizzard";
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 3, 1, 2);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_1_passive_1 = add(frost_tier_1_passive_1());
    private static Skills.Entry frost_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_1_passive_1");
        var effect = SkillEffects.FROST_VULNERABILITY;
        var title = "Winter's Chill";
        var description = "Frost spell impacts have {trigger_chance} chance to apply Winter's Chill effect."
                + " Increasing damage taken from frost spell critical strikes by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            return args.description().replace("{bonus}", SpellTooltip.percent(SkillEffects.FROST_VULNERABILITY_MULTIPLIER));
        };
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, "frost");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 4);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        25, 0.1F, 0.3F),
        };
        impact.sound = new Sound(SkillSounds.frost_winters_chill.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.FROST));
    }

    public static final String WIZARDS_FREEZE_EFFECT = "wizards:frozen";

    public static final Skills.Entry frost_tier_1_passive_2 = add(frost_tier_1_passive_2());
    private static Skills.Entry frost_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_1_passive_2");
        var title = "Frostbite";
        var description = "Frost spell impacts have {trigger_chance} chance, to freeze the target for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.05F, "frost");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(WIZARDS_FREEZE_EFFECT, 3F, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.FROST,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.25F, 0.3F)
                        .color(SkillsCommon.FROST_COLOR)
        };
        impact.sound = new Sound("wizards:frost_nova_effect_impact");
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_2_passive_1 = add(frost_tier_2_passive_1()); // Frost Trap
    private static Skills.Entry frost_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_2_passive_1");
        var title = "Frost Trap";
        var description = "Upon rolling, you leave behind a Frost Trap, lasting {cloud_duration} sec, applying Freeze effect to entering enemies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.passive.triggers = List.of(SpellBuilder.Triggers.roll());

        var radius = 1.5F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloudParticles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.CENTER,
                        2, 0.01F, 0.02F)
                        .color(Color.FROST.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.CENTER,
                        2, 0.02F, 0.05F),
        };
        var cloud = SpellBuilder.Deliver.cloud(
                5,
                1.5F,
                SkillSounds.frost_trap_activate.id(),
                8,
                cloudParticles
        );
        cloud.impact_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.FROST,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        25, 0.4F, 0.4F)
                        .color(Color.FROST.toRGBA())
        };
        cloud.impact_cap = 1; // Trap

        cloud.client_data.interval_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_715.id().toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.GROUND,
                        1, 0F, 0F)
                        .scale(radius * 1.5F) // 1.5F is asset specific
                        .color(Color.FROST.toRGBA())
        };
        cloud.client_data.particle_spawn_interval = 20;

        spell.deliver.clouds = List.of(cloud);

        var debuff = SpellBuilder.Impacts.effectAdd(WIZARDS_FREEZE_EFFECT, 6, 1, 4);
        spell.impacts = List.of(debuff);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_2_passive_2 = add(frost_tier_2_passive_2());
    private static Skills.Entry frost_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_2_passive_2");
        var title = "Arctic Reflex";
        var description = "Upon rolling, you have {trigger_chance_1} chance to instantly cast a spell, within the next {stash_duration} sec.";
        var effect = SkillEffects.ARCTIC_REFLEX;
        var duration = 5F;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = Sound.withVolume(SpellEngineSounds.SIGNAL_INSTANT_CAST.id(), 0.75F);

        // Release particle `sign_cast`
        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_cast.id(), Color.FROST),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F).color(Color.FROST.toRGBA())
        };

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.specificSpellCast("#wizards:frost");
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), duration, stashTrigger);

        // No impacts, stash will just be consumed

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_3_passive_1 = add(frost_tier_3_passive_1());
    private static Skills.Entry frost_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_3_passive_1");
        var title = "Cold Snap";
        var description = "Taking damage has {trigger_chance} chance to reset cooldowns of Frost spells.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.damageTaken();
        trigger.chance = 0.1F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive("#wizards:frost");
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.FROST),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        25, 0.2F, 0.2F)
                        .color(Color.FROST.toRGBA())
        };
        impact.sound = new Sound(SkillSounds.frost_cold_snap.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_3_passive_2 = add(frost_tier_3_passive_2()); // Frost Shield
    private static Skills.Entry frost_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_3_passive_2");
        var effect = SkillEffects.FROST_WARD;
        var title = effect.title;
        var description = "Frost spells have {trigger_chance_1} chance, to grant you " + effect.title + ", absorbing damage and slowing attackers, lasts {stash_duration} sec.";
        var duration = SkillsCommon.WIZARD_WARD_DURATION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = new Sound(SkillSounds.frost_ward_activate.id());

        var spell_trigger = SpellBuilder.Triggers.activeSpellCast(SpellSchools.FROST);
        spell_trigger.chance = SkillsCommon.WIZARD_WARD_CHANCE;
        spell_trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(spell_trigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.duration = duration;
        spell.deliver.stash_effect.amplifier = 0;
        spell.deliver.stash_effect.amplifier_power_multiplier = 0.2F;
        spell.deliver.stash_effect.consume = 0;

        var stash_trigger = SpellBuilder.Triggers.damageTaken();
        spell.deliver.stash_effect.triggers = List.of(stash_trigger);

        var impact = SpellBuilder.Impacts.effectAdd("wizards:frost_slowness", 5, 2, 9);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.FROST,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.25F, 0.3F)
                        .color(SkillsCommon.FROST_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.25F, 0.3F)
        };
        impact.sound = new Sound("wizards:frost_nova_effect_impact");
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }
}
