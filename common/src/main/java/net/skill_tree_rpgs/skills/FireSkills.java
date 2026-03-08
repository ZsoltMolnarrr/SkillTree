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

public class FireSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Color FIRE_MAGIC_COLOR = Color.from(0xff6600);

    public static final Skills.Entry fire_tier_2_spell_1_modifier_1 = add(fire_tier_2_spell_1_modifier_1());
    private static Skills.Entry fire_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_tier_2_spell_1_modifier_1");
        var title = "Explosive Breath";
        var description = "Fire Breath hits have {trigger_chance} chance to explode a target, dealing {damage} damage to nearby enemies.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit("wizards:fire_breath");
        trigger.chance = 0.1F;
        spell.passive.triggers = List.of(trigger);

        SkillsCommon.explosionImpact(spell, 0.5F);

        SpellBuilder.Cost.cooldown(spell, 0.5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_spell_1_modifier_2 = add(fire_tier_2_spell_1_modifier_2());
    private static Skills.Entry fire_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "fire_tier_2_spell_1_modifier_2");
        var title = "Flame Throwing";
        var description = "Increased the range of Fire Breath by {range_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_breath";
        modifier.range_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_spell_1_modifier_1 = add(fire_tier_3_spell_1_modifier_1());
    private static Skills.Entry fire_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_tier_3_spell_1_modifier_1");
        var title = "Meteor Shower";
        var description = "Meteor launches {extra_launch} extra projectile.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            return args.description().replace("{bonus}", SpellTooltip.percent(SkillEffects.FIRE_VULNERABILITY_MULTIPLIER));
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_meteor";
        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_spell_1_modifier_2 = add(fire_tier_3_spell_1_modifier_2());
    private static Skills.Entry fire_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "fire_tier_3_spell_1_modifier_2");
        var title = "Meteor Splash";
        var description = "Meteor impacts leave a fiery area behind, lasting {cloud_duration} sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellAreaImpact("wizards:fire_meteor");
        spell.passive.triggers = List.of(trigger);

        SpellBuilder.Complex.flameCloud(spell, 3.0F, 0.3F, 6, null);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_4_spell_1_modifier_1 = add(fire_tier_4_spell_1_modifier_1());
    private static Skills.Entry fire_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_tier_4_spell_1_modifier_1");
        var title = "Great Wall";
        var description = "Wall of Flames spawns 2 additional columns.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_wall";
        modifier.additional_placements = List.of(
                SpellBuilder.Deliver.placementByLook(6.4f, -72, 4),
                SpellBuilder.Deliver.placementByLook(6.4f, 72, 4)
        );

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_4_spell_1_modifier_2 = add(fire_tier_4_spell_1_modifier_2());
    private static Skills.Entry fire_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "fire_tier_4_spell_1_modifier_2");
        var title = "Healing Flames";
        var description = "Wall of Flames heals you and allies for {heal}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_wall";
        var impact = SpellBuilder.Impacts.heal(0.025F);
        impact.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_4.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final int FIRE_VULNERABILITY_DURATION = 8; // seconds

    public static final Skills.Entry fire_tier_1_passive_1 = add(fire_tier_1_passive_1());
    private static Skills.Entry fire_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "fire_tier_1_passive_1");
        var effect = SkillEffects.FIRE_VULNERABILITY;
        var title = "Scorching Flames";
        var description = "Fire spell impacts have {trigger_chance} chance to apply Fire Vulnerability. Increasing damage taken from fire spells by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            return args.description().replace("{bonus}", SpellTooltip.percent(SkillEffects.FIRE_VULNERABILITY_MULTIPLIER));
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, "fire");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), FIRE_VULNERABILITY_DURATION, 1, 4);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        5, 0.1F, 0.3F),
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        5, 0.1F, 0.3F)
        };
        impact.sound = new Sound("wizards:fire_scorch_impact");
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_1_passive_2 = add(fire_tier_1_passive_2());
    private static Skills.Entry fire_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "fire_tier_1_passive_2");
        var title = "Hot Impact";
        var description = "Fire spell impacts have {trigger_chance} chance to stun the target for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.spellHit(0.2F, "fire");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.stun(2F);
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_passive_1 = add(fire_tier_2_passive_1()); // Fire trap
    private static Skills.Entry fire_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "fire_tier_2_passive_1");
        var title = "Flame Trap";
        var description = "Upon rolling, you leave behind a Flame Trap, lasting {cloud_duration} sec, dealing {damage} damage and applying Fire Vulnerability to entering enemies.";

        var effect = SkillEffects.FIRE_VULNERABILITY;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.passive.triggers = List.of(SpellBuilder.Triggers.roll());

        var radius = 1.5F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloudParticles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.flame_ground.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.CENTER,
                        2, 0.01F, 0.02F),
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.CENTER,
                        1, 0.02F, 0.05F),
        };
        var cloud = SpellBuilder.Deliver.cloud(
                5,
                1.5F,
                SkillSounds.fire_trap_activate.id(),
                8,
                cloudParticles
        );
        cloud.impact_particles = new ParticleBatch[] {
                new ParticleBatch(
                        "lava",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        20, 0.4F, 0.4F)
        };
        cloud.impact_cap = 1; // Trap

        cloud.client_data.interval_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_715.id().toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.GROUND,
                        1, 0F, 0F)
                        .scale(radius * 1.5F) // 1.5F is asset specific
                        .color(FIRE_MAGIC_COLOR.toRGBA())
        };
        cloud.client_data.particle_spawn_interval = 20;

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.5F, 0.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.15F, 0.2F)
        };
        damage.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IMPACT_3.id());
        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), FIRE_VULNERABILITY_DURATION, 1, 4);
        spell.impacts = List.of(damage, debuff);

        var area_impact = new Spell.AreaImpact();
        area_impact.radius = radius;
        spell.area_impact = area_impact;

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_passive_2 = add(fire_tier_2_passive_2()); // Blazing Speed
    private static Skills.Entry fire_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "fire_tier_2_passive_2");
        var title = "Blazing Speed";
        var description = "Upon rolling, you have {trigger_chance} chance to gain {bonus} movement speed for {effect_duration} sec.";
        var effect = SkillEffects.BLAZING_SPEED;

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_speed.id(), FIRE_MAGIC_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND
                        ).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F).color(FIRE_MAGIC_COLOR.toRGBA())
        };
        impact.sound = new Sound(SpellEngineSounds.SPEED_BOOST.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_passive_1 = add(fire_tier_3_passive_1());
    private static Skills.Entry fire_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "fire_tier_3_passive_1");
        var title = "Eruption";
        var description = "Taking damage has {trigger_chance} chance to cause a strong explosion, dealing {damage} damage to nearby enemies.";
        var radius = 5F;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = radius;

        var trigger = SpellBuilder.Triggers.damageTaken();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        spell.release.sound = new Sound("wizards:fire_meteor_impact");

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_609.id())
                        .scale(radius)
                        .color(FIRE_MAGIC_COLOR.toRGBA()),
                new ParticleBatch(
                        "lava",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.15F, 0.2F),
                new ParticleBatch(
                        SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.2F),
                new ParticleBatch(
                        "flame",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.2F)
        };

        var damage = SpellBuilder.Impacts.damage(0.5F, 1.2F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.15F, 0.2F)
        };
        damage.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IMPACT_2.id());
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_passive_2 = add(fire_tier_3_passive_2()); // Flame Shield
    private static Skills.Entry fire_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "fire_tier_3_passive_2");
        var effect = SkillEffects.FIRE_WARD;
        var title = effect.title;
        var description = "Fire spells have {trigger_chance_1} chance, to grant you " + effect.title + ", absorbing damage and dealing {damage} damage to attackers, lasts {stash_duration} sec.";
        var duration = SkillsCommon.WIZARD_WARD_DURATION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = new Sound(SkillSounds.fire_ward_activate.id());

        var spell_trigger = SpellBuilder.Triggers.activeSpellCast(SpellSchools.FIRE);
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

        var damage = SpellBuilder.Impacts.damage(0.3F, 0.2F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.15F, 0.2F)
        };
        damage.sound = new Sound("wizards:fire_scorch_impact");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FIRE));
    }

}
