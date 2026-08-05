package net.skill_tree_rpgs.skills;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.summon.AttributeScaling;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellPowerMechanics;
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

    public static final String ARCANE_MISSILE = "wizards:arcane_missile";
    public static final String ARCANE_BEAM = "wizards:arcane_beam";
    public static final String ARCANE_BLINK = "wizards:arcane_blink";
    public static final String ARCANE_EXPLOSION = "wizards:arcane_explosion";
    public static final String ARCANE_BARRAGE = "wizards:arcane_barrage";
    public static final String ARCANE_EVOCATION = "wizards:arcane_evocation";
    public static final String ARCANE_SPELL_TAG = "#wizards:arcane";

    // Wizards is not a compile dependency of SkillTree, so its sounds are referenced by raw id.
    private static final String SOUND_ARCANE_BLAST_IMPACT = "wizards:arcane_blast_impact";

    /// Mirror of `arcane_explosion`'s own cooldown. Same reason as the sounds above: the base spell
    /// lives in Wizards and cannot be referenced from here, so this must be kept in sync by hand.
    private static final float ARCANE_EXPLOSION_COOLDOWN = 10F;


    /// A third of the way from the arcane school color towards white — lands on the same light
    /// arcane tone as Arcane Slowness (0xff99ff). Reads brighter than Arcane Blast's own
    /// particles, which are tinted with the school color flat.
    private static final long ARCANE_LIGHT_COLOR = Color.ARCANE.blend(Color.WHITE, 0.33F).toRGBA();

    /// The default arcane impact flourish: an ARCANE burst tinted to the school color.
    private static ParticleGroup arcaneBurst(ParticleGroup.Shape shape, int count, float spread, float speed) {
        return arcaneBurst(shape, count, spread, speed, SkillsCommon.ARCANE_COLOR);
    }

    /// The `origin` parameter is gone: every call site placed this at the target's centre, which is
    /// the batch default.
    private static ParticleGroup arcaneBurst(ParticleGroup.Shape shape, int count, float spread, float speed, long color) {
        return ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST)
                .color(color)
                .batch(b -> b.shape(shape).count(count).speed(spread, speed));
    }

    // ===================================================================================
    // MARK: Tier 1 passives
    // ===================================================================================

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
        // One shared roll per cast rather than one per enemy struck, so the advertised chance holds
        // for AoE arcane spells too (an independent roll per target would read as ~67% against 5).
        trigger.chance_batching = true;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.damage(0.4F, 0.2F);
        impact.action.allow_on_center_target = false;
        spell.impacts = List.of(impact);
        var area_impact = new Spell.AreaImpact();
        area_impact.radius = 2.5F;
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(30).speed(0.5F, 0.5F)),
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_642)
                        .facing(ParticleGroup.Facing.CAMERA)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1))
        );
        area_impact.sound = new Sound(SkillSounds.arcane_fissile_impact.id());
        spell.area_impact = area_impact;

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_1_passive_2 = add(arcane_tier_1_passive_2());
    private static Skills.Entry arcane_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_1_passive_2");
        var title = "Arcane Radiance";
        var description = "Arcane spell impacts have {trigger_chance} chance, to heal you for {heal}.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.1F, "arcane");
        // See Fissile Magic: batched so the tooltip chance matches the real per-cast rate.
        trigger.chance_batching = true;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(20).speed(0.1F, 0.1F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of(SpellEngineParticles.area_circle_1)
                        .attached()
                        .scale(0.8F)
                        .playbackSpeed(1.25F)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL).count(1).speed(0.2F, 0.2F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_heal, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.2F, 0.25F))
        );
        impact.sound = new Sound(SkillSounds.arcane_radiance.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===================================================================================
    // MARK: Tier 2 — Arcane Missile
    //
    // Each book spell's cluster is a weak "root" modifier (patterns come from the shared
    // palette in SkillsCommon, picked per spell) leading to two powerful mutex nodes.
    // ===================================================================================

    public static final Skills.Entry arcane_tier_2_spell_1_root = add(SkillsCommon.powerRoot(
            Skills.Category.ARCANE, SpellSchools.ARCANE,
            "arcane_tier_2_spell_1_root", ARCANE_MISSILE, "Arcane Missiles", 0.1F));

    public static final Skills.Entry arcane_tier_2_spell_1_modifier_1 = add(arcane_tier_2_spell_1_modifier_1());
    private static Skills.Entry arcane_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_spell_1_modifier_1");
        var title = "Conjured Missile";
        var description = "Arcane Missile shoots {extra_launch} additional missile per batch.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_MISSILE;

        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1;
        modifier.projectile_launch.extra_launch_delay = 2;
        modifier.projectile_launch.extra_launch_mod = 3;

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
        modifier.spell_pattern = ARCANE_MISSILE;

        // In ADD mode the amplifier argument is the per-hit increment, so it must be 1 for the
        // effect to actually stack up to its cap (0 would pin it at a single stack forever).
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 4, 1, 2);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===================================================================================
    // MARK: Tier 2 — Arcane Explosion
    // ===================================================================================

    public static final Skills.Entry arcane_tier_2_spell_2_root = add(SkillsCommon.radiusRoot(
            Skills.Category.ARCANE, SpellSchools.ARCANE,
            "arcane_tier_2_spell_2_root", ARCANE_EXPLOSION, "Arcane Explosion", 1F));

    public static final Skills.Entry arcane_tier_2_spell_2_modifier_1 = add(arcane_tier_2_spell_2_modifier_1());
    private static Skills.Entry arcane_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_spell_2_modifier_1");
        var title = "Echoing Blast";
        var description = "Arcane Explosion hits have {trigger_chance} chance to reset its cooldown.";
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit(ARCANE_EXPLOSION);
        trigger.chance = 0.25F;
        trigger.cap_per_tick = 1;
        // One shared roll per cast, not one per enemy struck — so multi-hit AoE doesn't inflate the
        // effective reset chance above the advertised value (e.g. 25% vs ~76% against 5 targets).
        trigger.chance_batching = true;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_cast.id(), Color.ARCANE)
        );
        spell.release.sound = new Sound(SpellEngineSounds.SIGNAL_SPELL_CRIT.id());

        var reset = SpellBuilder.Impacts.resetCooldownActive(ARCANE_EXPLOSION);
        reset.action.apply_to_caster = true;
        spell.impacts = List.of(reset);

        // Internal cooldown matching the base spell's, so one cast can grant at most one reset.
        SpellBuilder.Cost.cooldown(spell, ARCANE_EXPLOSION_COOLDOWN);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_2_spell_2_modifier_2 = add(arcane_tier_2_spell_2_modifier_2());
    private static Skills.Entry arcane_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_2_spell_2_modifier_2");
        var title = "Chain Detonation";
        var maxTargets = 4;
        var description = "Arcane Explosion causes up to " + maxTargets + " secondary explosions, dealing {damage} damage to nearby enemies.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        // Matches arcane_explosion's own range, so the secondary blasts cover the enemies the
        // parent explosion could have hit. Selection is centered on the caster, not the trigger.
        spell.range = 6F;

        // Detonate on up to 4 targets rather than on whichever enemy happened to trigger this.
        // `cap` keeps the nearest N (SpellHelper sorts by distance to caster); the engine has no
        // random selection, so ordering is by proximity.
        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area(); // Required: Target.area has no default instance
        spell.target.cap = maxTargets;
        spell.deliver.delay = 7;

        var trigger = SpellBuilder.Triggers.specificSpellHit(ARCANE_EXPLOSION);
        spell.passive.triggers = List.of(trigger);

        var radius = 4.0F;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0.2F);
        var area_impact = new Spell.AreaImpact();
        area_impact.force_indirect = true;
        area_impact.radius = radius;
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        // Ground ring + vertical aura of the same asset family, reading as a small Arcane Blast.
        area_impact.visuals = Fx.Visuals.of(
                arcaneBurst(ParticleGroup.Shape.CIRCLE, 30, 0.4F, 0.4F, ARCANE_LIGHT_COLOR),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_574.id())
                        .appearance(a -> a.scale(radius - 0.5F).color(ARCANE_LIGHT_COLOR)),
                SpellBuilder.Particles.aura(SpellEngineParticles.area_effect_574.id())
                        .appearance(a -> a.scale(radius - 0.5F).color(ARCANE_LIGHT_COLOR))
        );
        area_impact.sound = new Sound(SOUND_ARCANE_BLAST_IMPACT, 1F, 1.2F, 0.1F);
        spell.area_impact = area_impact;
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===================================================================================
    // MARK: Tier 2 passives
    // ===================================================================================

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
                SkillsCommon.ARCANE_COLOR,
                SpellEngineParticles.magic_spell.id(),
                List.of(SpellEngineParticles.magic_spark.id()),
                1
        );
        // `zoneMagic` builds each effect with its entry's own default motion (FLOAT); the V1 ids
        // this replaced carried DECELERATE, so restore it.
        cloudParticles.forEach(p -> p.appearance.motion(ParticleGroup.Motion.DECELERATE));
        var cloud = SpellBuilder.Deliver.cloud(
                5,
                1.5F,
                SkillSounds.arcane_trap_activate.id(),
                8,
                cloudParticles
        );
        cloud.impact = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(20).speed(0.4F, 0.4F).verticalOrigin(Batches.FEET))
        );
        cloud.impact_cap = 1; // Trap

        cloud.client_data.interval_particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_715)
                        .scale(radius * 1.5F) // 1.5F is asset specific
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );
        cloud.client_data.particle_spawn_interval = 20;

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.75F, 0.5F);
        damage.visuals = Fx.Visuals.of(
                arcaneBurst(ParticleGroup.Shape.SPHERE, 15, 0.45F, 0.75F)
        );
        damage.sound = new Sound(SOUND_ARCANE_BLAST_IMPACT);
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

    // ===================================================================================
    // MARK: Tier 3 — Arcane Beam
    // ===================================================================================

    public static final Skills.Entry arcane_tier_3_spell_1_root = add(SkillsCommon.critRoot(
            Skills.Category.ARCANE, SpellSchools.ARCANE,
            "arcane_tier_3_spell_1_root", ARCANE_BEAM, "Arcane Beam", 0.05F));

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
        modifier.spell_pattern = ARCANE_BEAM;
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
        modifier.spell_pattern = ARCANE_BEAM;
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 3, 1, 4);
        impact.action.apply_to_caster = true;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===================================================================================
    // MARK: Tier 3 — Arcane Barrage (summons emitters)
    // ===================================================================================

    // Flat +10% critical strike chance on the summoned emitters (crit chance attribute is
    // baseline-100, so a flat +10 with no owner scaling reads as +10%).
    public static final Skills.Entry arcane_tier_3_spell_2_root = add(SkillsCommon.spellRoot(
            Skills.Category.ARCANE, SpellSchools.ARCANE,
            "arcane_tier_3_spell_2_root", ARCANE_BARRAGE, "Arcane Barrage",
            "Arcane Barrage emitters gain 10%% increased critical strike chance.",
            modifier -> {
                var critChance = new AttributeScaling.Entry();
                critChance.attribute_id = SpellPowerMechanics.CRITICAL_CHANCE.id.toString();
                critChance.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                        SpellPowerMechanics.CRITICAL_CHANCE.id.toString(),
                        EntityAttributeModifier.Operation.ADD_VALUE, 10.0, 0.0));
                modifier.summon_attribute_scaling = new AttributeScaling();
                modifier.summon_attribute_scaling.entries = List.of(critChance);
            }));

    public static final Skills.Entry arcane_tier_3_spell_2_modifier_1 = add(arcane_tier_3_spell_2_modifier_1());
    private static Skills.Entry arcane_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_spell_2_modifier_1");
        var title = "Arcane Battery";
        var description = "Conjures an additional Arcane Emitter.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_BARRAGE;
        modifier.summon_spawn_count_add = 1;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_3_spell_2_modifier_2 = add(arcane_tier_3_spell_2_modifier_2());
    private static Skills.Entry arcane_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_spell_2_modifier_2");
        var title = "Attuned Emitters";
        var description = "Arcane Emitters fire faster, matching your spell haste.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_BARRAGE;

        // Mirror the owner's Haste onto the emitters, so their (haste-affected) firing cadence
        // matches the caster's. Haste is a percent stat where 100 = neutral; the emitter seeds
        // at 100, so add (owner - 100): base -100, coefficient 1.
        var haste = new AttributeScaling.Entry();
        haste.attribute_id = SpellPowerMechanics.HASTE.id.toString();
        haste.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                SpellPowerMechanics.HASTE.id.toString(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                -SpellPowerMechanics.PERCENT_ATTRIBUTE_BASELINE, 1.0));
        modifier.summon_attribute_scaling = new AttributeScaling();
        modifier.summon_attribute_scaling.entries = List.of(haste);

        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===================================================================================
    // MARK: Tier 3 passives
    // ===================================================================================

    public static final Skills.Entry arcane_tier_3_passive_1 = add(arcane_tier_3_passive_1());
    private static Skills.Entry arcane_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_3_passive_1");
        var title = "Presence of Mind";
        var description = "Blink and Evocation have {trigger_chance_1} chance, to turn your next spell cast instant, within the next {stash_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;
        var duration = 5F;

        var effect = SkillEffects.PRESENCE_OF_MIND;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = Sound.withVolume(SpellEngineSounds.SIGNAL_INSTANT_CAST.id(), 0.75F);

        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_cast.id(), Color.ARCANE),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(SkillsCommon.ARCANE_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET))
        );

        // Either cast can prime the instant. Two triggers means SpellTooltip indexes the chance
        // token, so the description reads {trigger_chance_1} — the plain {trigger_chance} is only
        // emitted for single-trigger spells and would be left unresolved here. Both are equal.
        var chance = 0.5F;
        var blinkTrigger = SpellBuilder.Triggers.specificSpellCast(ARCANE_BLINK);
        blinkTrigger.chance = chance;
        var evocationTrigger = SpellBuilder.Triggers.specificSpellCast(ARCANE_EVOCATION);
        evocationTrigger.chance = chance;
        spell.passive.triggers = List.of(blinkTrigger, evocationTrigger);

        var stashTrigger = SpellBuilder.Triggers.specificSpellCast(ARCANE_SPELL_TAG);
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), duration, stashTrigger);

        SpellBuilder.Cost.cooldown(spell, 15F);

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

    // ===================================================================================
    // MARK: Tier 4 — Blink
    // ===================================================================================

    public static final Skills.Entry arcane_tier_4_spell_1_root = add(SkillsCommon.teleportRoot(
            Skills.Category.ARCANE, SpellSchools.ARCANE,
            "arcane_tier_4_spell_1_root", ARCANE_BLINK, "Blink", 3F));

    public static final Skills.Entry arcane_tier_4_spell_1_modifier_1 = add(arcane_tier_4_spell_1_modifier_1());
    private static Skills.Entry arcane_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_4_spell_1_modifier_1");
        var title = "Slipstream";
        var description = "Reduces the cooldown of Blink by {cooldown_duration_deduct} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_BLINK;
        modifier.cooldown_duration_deduct = 4F;
        spell.modifiers = List.of(modifier);
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
        impact1.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST)
                        .color(Color.WHITE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.6F, 0.6F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(Color.WHITE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(10).speed(0.2F, 0.4F))
        );
        impact1.sound = new Sound(SpellEngineSounds.GENERIC_DISPEL_1.id());
        var impact2 = SpellBuilder.Impacts.effectCleanse();
        impact2.action.status_effect.amplifier = -1;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_BLINK;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact1, impact2);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===================================================================================
    // MARK: Tier 4 — Evocation
    // ===================================================================================

    public static final Skills.Entry arcane_tier_4_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.ARCANE, SpellSchools.ARCANE,
            "arcane_tier_4_spell_2_root", ARCANE_EVOCATION, "Evocation", 5F));

    public static final Skills.Entry arcane_tier_4_spell_2_modifier_1 = add(arcane_tier_4_spell_2_modifier_1());
    private static Skills.Entry arcane_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_4_spell_2_modifier_1");
        var title = "Rapid Evocation";
        var extraChannels = 2;
        var description = "Evocation channels " + extraChannels + " additional times, granting more stacks.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_EVOCATION;
        modifier.channel_ticks_add = extraChannels;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry arcane_tier_4_spell_2_modifier_2 = add(arcane_tier_4_spell_2_modifier_2());
    private static Skills.Entry arcane_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_tier_4_spell_2_modifier_2");
        var title = "Lasting Evocation";
        var description = "Increases the duration of Evocation by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = ARCANE_EVOCATION;
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }
}
