package net.skill_tree_rpgs.skills;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
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
import net.spell_power.api.SpellSchools;

import org.joml.Vector3f;

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

    public static final String FIRE_BREATH = "wizards:fire_breath";
    public static final String FIRE_SLASH = "wizards:fire_slash";
    public static final String FIRE_METEOR = "wizards:fire_meteor";
    public static final String FIRE_STORM = "wizards:fire_storm";
    public static final String FIRE_WALL = "wizards:fire_wall";
    public static final String FIRE_HYDRA = "wizards:fire_hydra";

    public static final Skills.Entry fire_tier_2_spell_1_modifier_1 = add(fire_tier_2_spell_1_modifier_1());
    private static Skills.Entry fire_tier_2_spell_1_modifier_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_2_spell_1_modifier_1");
        var title = "Explosive Breath";
        var description = "Fire Breath hits have {trigger_chance} chance to explode a target, dealing {damage} damage to nearby enemies.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit(FIRE_BREATH);
        trigger.chance = 0.1F;
        spell.passive.triggers = List.of(trigger);

        SkillsCommon.explosionImpact(spell, 0.5F);

        SpellBuilder.Cost.cooldown(spell, 0.5F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_spell_1_modifier_2 = add(fire_tier_2_spell_1_modifier_2());
    private static Skills.Entry fire_tier_2_spell_1_modifier_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_2_spell_1_modifier_2");
        var title = "Flame Throwing";
        var description = "Increased the range of Fire Breath by {range_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_BREATH;
        modifier.range_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_spell_1_modifier_1 = add(fire_tier_3_spell_1_modifier_1());
    private static Skills.Entry fire_tier_3_spell_1_modifier_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_3_spell_1_modifier_1");
        var title = "Meteor Shower";
        var description = "Meteor launches {extra_launch} extra projectile.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_METEOR;
        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_spell_1_modifier_2 = add(fire_tier_3_spell_1_modifier_2());
    private static Skills.Entry fire_tier_3_spell_1_modifier_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_3_spell_1_modifier_2");
        var title = "Meteor Splash";
        var description = "Meteor impacts leave a fiery area behind, lasting {cloud_duration} sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellAreaImpact(FIRE_METEOR);
        spell.passive.triggers = List.of(trigger);

        SpellBuilder.Complex.flameCloud(spell, 3.0F, 0.3F, 6, null);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_4_spell_1_modifier_1 = add(fire_tier_4_spell_1_modifier_1());
    private static Skills.Entry fire_tier_4_spell_1_modifier_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_4_spell_1_modifier_1");
        var title = "Great Wall";
        var description = "Wall of Flames spawns 2 additional columns.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_WALL;
        modifier.additional_placements = List.of(
                SpellBuilder.Deliver.placementByLook(6.4f, -72, 4),
                SpellBuilder.Deliver.placementByLook(6.4f, 72, 4)
        );

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_4_spell_1_modifier_2 = add(fire_tier_4_spell_1_modifier_2());
    private static Skills.Entry fire_tier_4_spell_1_modifier_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_4_spell_1_modifier_2");
        var title = "Healing Flames";
        var description = "Wall of Flames heals you and allies for {heal}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_WALL;
        var impact = SpellBuilder.Impacts.heal(0.025F);
        impact.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_4.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry fire_tier_2_spell_1_root = add(SkillsCommon.critRoot(
            Skills.Category.FIRE, SpellSchools.FIRE,
            "fire_tier_2_spell_1_root", FIRE_BREATH, "Fire Breath", 0.05F));
    public static final Skills.Entry fire_tier_2_spell_2_root = add(SkillsCommon.reachRoot(
            Skills.Category.FIRE, SpellSchools.FIRE,
            "fire_tier_2_spell_2_root", FIRE_SLASH, "Flame Slash", 5F));
    public static final Skills.Entry fire_tier_3_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.FIRE, SpellSchools.FIRE,
            "fire_tier_3_spell_1_root", FIRE_METEOR, "Meteor", 2F));
    public static final Skills.Entry fire_tier_3_spell_2_root = add(SkillsCommon.powerRoot(
            Skills.Category.FIRE, SpellSchools.FIRE,
            "fire_tier_3_spell_2_root", FIRE_STORM, "Firestorm", 0.1F));
    public static final Skills.Entry fire_tier_4_spell_1_root = add(SkillsCommon.fieldRoot(
            Skills.Category.FIRE, SpellSchools.FIRE,
            "fire_tier_4_spell_1_root", FIRE_WALL, "Wall of Flames", 2F));
    public static final Skills.Entry fire_tier_4_spell_2_root = add(SkillsCommon.companionRoot(
            Skills.Category.FIRE, SpellSchools.FIRE,
            "fire_tier_4_spell_2_root", FIRE_HYDRA, "Fire Hydra", 5));

    // ===================================================================================
    // Powerful mutex modifiers for the second spell of each tier (spell_2).
    // fire_slash / Flame Slash (T2), fire_storm / Firestorm (T3), fire_hydra (T4, summon).
    // ===================================================================================

    public static final Skills.Entry fire_tier_2_spell_2_modifier_1 = add(fire_tier_2_spell_2_modifier_1());
    private static Skills.Entry fire_tier_2_spell_2_modifier_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_2_spell_2_modifier_1");
        var title = "Towering Slash";
        var bonus = 0.33F;
        var description = "Flame Slash is " + Skills.bakedPercent(bonus) + " larger.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_SLASH;
        // Stacks on top of the base spell's charge growth (up to 2x at full charge -> up to 2.33x).
        modifier.projectile_scale_multiply = bonus;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_spell_2_modifier_2 = add(fire_tier_2_spell_2_modifier_2());
    private static Skills.Entry fire_tier_2_spell_2_modifier_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_2_spell_2_modifier_2");
        var title = "Wave after Wave";
        var description = "Flame Slash hits reduce its own remaining cooldown by 1 sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        // True modifier: inject a cooldown-shaving impact into Flame Slash itself, so it fires on
        // every enemy struck (chaining through a crowd shortens the cooldown faster — "wave after
        // wave"). apply_to_caster routes the deduction onto the caster, not the victim; duration_add
        // trims the remaining cooldown (multiplier stays 1, so it's not a reset). NOTE: the engine
        // applies duration_add in TICKS against the remaining tick count (SpellImpacts.modifyCooldowns,
        // no seconds->ticks conversion), so -20 ticks == -1 second per hit.
        var shave = new Spell.Impact();
        shave.action = new Spell.Impact.Action();
        shave.action.type = Spell.Impact.Action.Type.COOLDOWN;
        shave.action.apply_to_caster = true;
        shave.action.cooldown = new Spell.Impact.Action.Cooldown();
        shave.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        shave.action.cooldown.actives.id = FIRE_SLASH;
        shave.action.cooldown.actives.duration_add = -20F; // 20 ticks = 1 second

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_SLASH;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(shave);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_spell_2_modifier_1 = add(fire_tier_3_spell_2_modifier_1());
    private static Skills.Entry fire_tier_3_spell_2_modifier_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_3_spell_2_modifier_1");
        var title = "Flame Whirlpool";
        var description = "Firestorm drags enemies towards you, briefly slowing them.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_STORM;

        var slow = SpellBuilder.Impacts.effectSet(MobEffects.SLOWNESS.getRegisteredName(), 1, 0);

        // Gentle radial pull: -Z in the ORIGIN frame points towards the storm's centre (the
        // caster); reapplied on every channel burst, with a slight lift to beat ground friction.
        var pull = SpellBuilder.Impacts.velocity(
                Spell.Impact.Action.Velocity.Frame.ORIGIN, new Vector3f(0, 0.1F, -0.3F));
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(slow, pull);

        // A converging ground ring at the caster, replayed with each burst's release FX.
        modifier.release = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_678)
                        .scale(3.5F)
                        .color(FIRE_MAGIC_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );

        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_spell_2_modifier_2 = add(fire_tier_3_spell_2_modifier_2());
    private static Skills.Entry fire_tier_3_spell_2_modifier_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_3_spell_2_modifier_2");
        var title = "Raging Firestorm";
        var description = "Firestorm channels {channel_ticks_add} additional times, knocking enemies away with {knockback_multiply_base} increased force.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_STORM;
        modifier.channel_ticks_add = 2;
        modifier.knockback_multiply_base = 1F;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_4_spell_2_modifier_1 = add(fire_tier_4_spell_2_modifier_1());
    private static Skills.Entry fire_tier_4_spell_2_modifier_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_4_spell_2_modifier_1");
        var title = "Hydra Brood";
        var description = "Conjures an additional Fire Hydra head.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_HYDRA;
        modifier.summon_spawn_count_add = 1;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_4_spell_2_modifier_2 = add(fire_tier_4_spell_2_modifier_2());
    private static Skills.Entry fire_tier_4_spell_2_modifier_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_4_spell_2_modifier_2");
        var title = "Ancient Hydra";
        var seconds = 15;
        var description = "Fire Hydra lasts " + seconds + " sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FIRE_HYDRA;
        modifier.summon_behaviour.lifespan.active_seconds_add = seconds;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final int FIRE_VULNERABILITY_DURATION = 8; // seconds

    public static final Skills.Entry fire_tier_1_passive_1 = add(fire_tier_1_passive_1());
    private static Skills.Entry fire_tier_1_passive_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_1_passive_1");
        var effect = SkillEffects.FIRE_VULNERABILITY;
        var title = "Scorching Flames";
        var description = "Fire spell impacts have {trigger_chance} chance to apply Fire Vulnerability. Increasing damage taken from fire spells by "
                + Skills.bakedPercent(SkillEffects.FIRE_VULNERABILITY_MULTIPLIER)
                + ", stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, "fire");
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), FIRE_VULNERABILITY_DURATION, 1, 4);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_a)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(5).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(5).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound("wizards:fire_scorch_impact");
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_1_passive_2 = add(fire_tier_1_passive_2());
    private static Skills.Entry fire_tier_1_passive_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_1_passive_2");
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

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_passive_1 = add(fire_tier_2_passive_1()); // Fire trap
    private static Skills.Entry fire_tier_2_passive_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_2_passive_1");
        var title = "Flame Trap";
        var description = "Upon rolling, you leave behind a Flame Trap, lasting {cloud_duration} sec, dealing {damage} damage and applying Fire Vulnerability to entering enemies.";

        var effect = SkillEffects.FIRE_VULNERABILITY;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.passive.triggers = List.of(SpellBuilder.Triggers.roll());

        var radius = 1.5F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloudParticles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_ground)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(2).speed(0.01F, 0.02F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(1).speed(0.02F, 0.05F))
        );
        var cloud = SpellBuilder.Deliver.cloud(
                5,
                1.5F,
                SkillSounds.fire_trap_activate.id(),
                8,
                cloudParticles
        );
        cloud.impact = Fx.Visuals.of(
                ParticleGroupBuilder.of("lava")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(20).speed(0.4F, 0.4F).verticalOrigin(Batches.FEET))
        );
        cloud.impact_cap = 1; // Trap

        cloud.client_data.interval_particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_715)
                        .scale(radius * 1.5F) // 1.5F is asset specific
                        .color(FIRE_MAGIC_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );
        cloud.client_data.particle_spawn_interval = 20;

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.5F, 0.5F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.15F, 0.2F))
        );
        damage.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IMPACT_3.id());
        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), FIRE_VULNERABILITY_DURATION, 1, 4);
        spell.impacts = List.of(damage, debuff);

        var area_impact = new Spell.AreaImpact();
        area_impact.radius = radius;
        spell.area_impact = area_impact;

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_2_passive_2 = add(fire_tier_2_passive_2()); // Blazing Speed
    private static Skills.Entry fire_tier_2_passive_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_2_passive_2");
        var title = "Blazing Speed";
        var description = "Upon rolling, you have {trigger_chance} chance to gain " + TooltipTokens.effect(SkillEffects.BLAZING_SPEED.id) + " movement speed for {effect_duration} sec.";
        var effect = SkillEffects.BLAZING_SPEED;


        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_speed.id(), FIRE_MAGIC_COLOR),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(FIRE_MAGIC_COLOR.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET))
        );
        impact.sound = new Sound(SpellEngineSounds.SPEED_BOOST.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_passive_1 = add(fire_tier_3_passive_1());
    private static Skills.Entry fire_tier_3_passive_1() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_3_passive_1");
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

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_609.id())
                        .appearance(a -> a.scale(radius).color(FIRE_MAGIC_COLOR.toRGBA())),
                ParticleGroupBuilder.of("lava")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.15F, 0.2F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_spark)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(15).speed(0.2F, 0.2F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of("flame")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(15).speed(0.2F, 0.2F).verticalOrigin(Batches.FEET))
        );

        var damage = SpellBuilder.Impacts.damage(0.5F, 1.2F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.15F, 0.2F))
        );
        damage.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IMPACT_2.id());
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

    public static final Skills.Entry fire_tier_3_passive_2 = add(fire_tier_3_passive_2()); // Flame Shield
    private static Skills.Entry fire_tier_3_passive_2() {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, "fire_tier_3_passive_2");
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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.15F, 0.2F))
        );
        damage.sound = new Sound("wizards:fire_scorch_impact");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, EnumSet.of(Skills.Category.FIRE));
    }

}
