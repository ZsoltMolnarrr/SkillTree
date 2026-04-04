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

public class ArcaneSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Skills.Entry arcane_tier_2_spell_1_modifier_1 = add(arcane_tier_2_spell_1_modifier_1());
    private static Skills.Entry arcane_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_spell_1_modifier_1");
        var title = "Conjured Missile";
        var description = "Arcane Missile shoots {extra_launch} additional missile per batch.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_missile";

        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1;
        modifier.projectile_launch.extra_launch_delay = 2;
        modifier.projectile_launch.extra_launch_mod = 3;
        modifier.power_modifier = new Spell.Impact.Modifier();
//        modifier.power_modifier.power_multiplier = -0.3F;
//        modifier.knockback_multiply_base = -0.1F;

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_2_spell_1_modifier_2 = add(arcane_tier_2_spell_1_modifier_2());
    private static Skills.Entry arcane_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_spell_1_modifier_2");
        var effect = SkillEffects.ARCANE_SLOWNESS;
        var title = "Crippling Missiles";
        var description = "Arcane Missiles apply slowness, reducing movement speed by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_missile";

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 4, 0, 2);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_3_spell_1_modifier_1 = add(arcane_tier_3_spell_1_modifier_1());
    private static Skills.Entry arcane_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_spell_1_modifier_1");
        var title = "Beam Exposure";
        var description = "Arcane Beam applies Arcane Exposure increasing Arcane damage taken by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var effect = SkillEffects.ARCANE_EXPOSURE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            return args.description().replace("{bonus}", SpellTooltip.percent(SkillEffects.ARCANE_EXPOSURE_MULTIPLIER));
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_beam";
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 6, 1, 9);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_3_spell_1_modifier_2 = add(arcane_tier_3_spell_1_modifier_2());
    private static Skills.Entry arcane_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_spell_1_modifier_2");
        var title = "Beam Propulsion";
        var description = "Arcane Beam hits increase your speed and jump strength by {bonus} for {effect_duration} sec, stacking up to {effect_amplifier_cap} times.";
        var effect = SkillEffects.ARCANE_SPEED;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_beam";
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 3, 1, 4);
        impact.action.apply_to_caster = true;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_4_spell_1_modifier_1 = add(arcane_tier_4_spell_1_modifier_1());
    private static Skills.Entry arcane_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_4_spell_1_modifier_1");
        var title = "Presence of Mind";
        var description = "Blink turns your next spell cast instant, within the next {stash_duration} sec.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        var duration = 5F;

        var effect = SkillEffects.PRESENCE_OF_MIND;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = Sound.withVolume(SpellEngineSounds.SIGNAL_INSTANT_CAST.id(), 0.75F);

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_cast.id(), Color.ARCANE),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F).color(Color.ARCANE.toRGBA())
        };

        var trigger = SpellBuilder.Triggers.specificSpellCast("wizards:arcane_blink");
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.specificSpellCast("#wizards:arcane");
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), duration, stashTrigger);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_4_spell_1_modifier_2 = add(arcane_tier_4_spell_1_modifier_2());
    private static Skills.Entry arcane_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_4_spell_1_modifier_2");
        var title = "Purge";
        var description = "Blink attempts to remove 2 negative effects from you entirely.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var impact1 = SpellBuilder.Impacts.effectCleanse();
        impact1.action.status_effect.amplifier = -1;
        impact1.particles = new ParticleBatch[]{
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
        impact1.sound = new Sound(SpellEngineSounds.GENERIC_DISPEL_1.id());
        var impact2 = SpellBuilder.Impacts.effectCleanse();
        impact2.action.status_effect.amplifier = -1;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blink";
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact1, impact2);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_1_passive_1 = add(arcane_tier_1_passive_1());
    private static Skills.Entry arcane_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_1_passive_1");
        var title = "Fissile Magic";
        var description = "Arcane spell impacts have {trigger_chance} chance, to cause a small explosion, dealing {damage} damage.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.2F, "arcane");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.damage(0.4F, 0.2F);
        impact.action.allow_on_center_target = false;
        spell.impacts = List.of(impact);
        var area_impact = new Spell.AreaImpact();
        area_impact.radius = 2.5F;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.5F, 0.5F)
                        .color(Color.from(SpellSchools.ARCANE.color).toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.aura_effect_642.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1, 0, 0)
                        .color(SkillsCommon.ARCANE_COLOR),
        };
        area_impact.sound = new Sound(SkillSounds.arcane_fissile_impact.id());
        spell.area_impact = area_impact;

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_1_passive_2 = add(arcane_tier_1_passive_2());
    private static Skills.Entry arcane_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_1_passive_2");
        var title = "Evocation Radiance";
        var description = "Arcane spell impacts have {trigger_chance} chance, to heal the you for {heal}.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.1F, "arcane");
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(SkillsCommon.SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.1F, 0.1F)
                        .color(SkillsCommon.ARCANE_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.area_circle_1.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                        1, 0.2F, 0.2F)
                        .followEntity(true)
                        .scale(0.8F)
                        .maxAge(0.8F)
                        .color(SkillsCommon.ARCANE_COLOR),
                new ParticleBatch(
                        SkillsCommon.HEAL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F)
                        .color(SkillsCommon.ARCANE_COLOR)
        };
        impact.sound = new Sound(SkillSounds.arcane_radiance.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_2_passive_1 = add(arcane_tier_2_passive_1());
    private static Skills.Entry arcane_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_passive_1");
        var title = "Arcane Trap";
        var description = "Upon rolling, you leave behind an Arcane Trap, lasting {cloud_duration} sec, dealing {damage} damage to entering enemies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.passive.triggers = List.of(SpellBuilder.Triggers.roll());

        var radius = 1.5F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloudParticles = SpellBuilder.Particles.zoneMagic(
                Color.ARCANE.toRGBA(),
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE
                ).id(),
                List.of(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE
                        ).id()
                ),
                1
        );
        var cloud = SpellBuilder.Deliver.cloud(
                5,
                1.5F,
                SkillSounds.arcane_trap_activate.id(),
                8,
                cloudParticles
        );
        cloud.impact_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        20, 0.4F, 0.4F)
                        .color(Color.from(SpellSchools.ARCANE.color).toRGBA())
        };
        cloud.impact_cap = 1; // Trap

        cloud.client_data.interval_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_715.id().toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.GROUND,
                        1, 0F, 0F)
                        .scale(radius * 1.5F) // 1.5F is asset specific
                        .color(Color.from(SpellSchools.ARCANE.color).toRGBA())
        };
        cloud.client_data.particle_spawn_interval = 20;

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.75F, 0.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.45F, 0.75F)
                        .color(Color.from(SpellSchools.ARCANE.color).toRGBA()),
        };
        damage.sound = new Sound("wizards:arcane_blast_impact");
        spell.impacts = List.of(damage);

        var area_impact = new Spell.AreaImpact();
        area_impact.radius = radius;
        spell.area_impact = area_impact;

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_2_passive_2 = add(arcane_tier_2_passive_2());
    private static Skills.Entry arcane_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_passive_2");
        var title = "Phase Shift";
        var description = "Upon rolling, you become invulnerable for {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.passive.triggers = List.of(SpellBuilder.Triggers.roll());

        var effect = SkillEffects.PHASE_SHIFT;

        var duration = 2F;
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), duration, 0, 0);
        impact.sound = new Sound(SkillSounds.arcane_phase_shift.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_3_passive_1 = add(arcane_tier_3_passive_1());
    private static Skills.Entry arcane_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_passive_1");
        var title = "Spell Riposte";
        var description = "Upon taking damage, an Arcane Bolt is launched at the attacker, dealing {damage} damage.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 30;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.damageTaken();
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 1F;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 10;
        projectile.client_data.travel_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND
                        ).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 1, 0.05F, 0.1F, 0.0F, 0F)
                        .color(SkillsCommon.ARCANE_COLOR)
        };
        projectile.client_data.model = new Spell.ProjectileModel();
        projectile.client_data.model.model_id = "wizards:spell_projectile/arcane_bolt";
        projectile.client_data.model.scale = 0.5F;
        spell.deliver.projectile.projectile = projectile;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0.5F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST
                        ).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.45F, 0.85F)
                        .color(Color.from(SpellSchools.ARCANE.color).toRGBA()),
        };
        impact.sound = new Sound("wizards:arcane_missile_impact");
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 2F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }


    public static final Skills.Entry arcane_tier_3_passive_2 = add(arcane_tier_3_passive_2());
    private static Skills.Entry arcane_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_passive_2");
        var effect = SkillEffects.ARCANE_WARD;
        var title = effect.title;
        var description = "Arcane spells have {trigger_chance} chance, to grant you " + effect.title + ", absorbing high amount of damage, lasting {effect_duration} sec.";
        var duration = SkillsCommon.WIZARD_WARD_DURATION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCast(SpellSchools.ARCANE);
        trigger.chance = SkillsCommon.WIZARD_WARD_CHANCE;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), duration, 0);
        impact.action.status_effect.amplifier_power_multiplier = 0.4F;
        impact.action.apply_to_caster = true;
        impact.sound = new Sound(SkillSounds.arcane_ward_activate.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }
}
