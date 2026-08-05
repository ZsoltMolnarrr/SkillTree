package net.skill_tree_rpgs.skills;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.summon.AttributeScaling;
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

    public static final String FROST_NOVA = "wizards:frost_nova";
    public static final String FROST_SHIELD = "wizards:frost_shield";
    public static final String FROST_BLIZZARD = "wizards:frost_blizzard";
    public static final String FROST_SPIKES = "wizards:frost_spikes";
    public static final String FROST_LANCE = "wizards:frost_lance";
    public static final String FROST_ELEMENTAL = "wizards:frost_elemental";
    public static final String FROST_SPELL_TAG = "#wizards:frost";

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

        var trigger = SpellBuilder.Triggers.specificSpellHit(FROST_NOVA);
        spell.passive.triggers = List.of(trigger);

        var radius = 3.0F;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0.2F);
        var area_impact = new Spell.AreaImpact();
        area_impact.force_indirect = true;
        area_impact.radius = radius;
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(30).speed(0.4F, 0.4F).verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_293)
                        .scale(radius - 0.5F)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );
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
        modifier.spell_pattern = FROST_NOVA;
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
        modifier.spell_pattern = FROST_SHIELD;
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
        modifier.spell_pattern = FROST_SHIELD;
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
        modifier.spell_pattern = FROST_BLIZZARD;
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
        modifier.spell_pattern = FROST_BLIZZARD;
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 3, 1, 2);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes.
    // Each acts as the structural parent gating the two powerful mutex nodes of a spell.
    // Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry frost_tier_2_spell_1_root = add(SkillsCommon.lingerRoot(
            Skills.Category.FROST, SpellSchools.FROST,
            "frost_tier_2_spell_1_root", FROST_NOVA, "Frost Nova", 1F));
    public static final Skills.Entry frost_tier_2_spell_2_root = add(SkillsCommon.powerRoot(
            Skills.Category.FROST, SpellSchools.FROST,
            "frost_tier_2_spell_2_root", FROST_SPIKES, "Frost Spikes", 0.1F));
    public static final Skills.Entry frost_tier_3_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.FROST, SpellSchools.FROST,
            "frost_tier_3_spell_1_root", FROST_SHIELD, "Frost Shield", 3F));
    public static final Skills.Entry frost_tier_3_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.FROST, SpellSchools.FROST,
            "frost_tier_3_spell_2_root", FROST_LANCE, "Ice Lance", 2F));
    public static final Skills.Entry frost_tier_4_spell_1_root = add(SkillsCommon.channelRoot(
            Skills.Category.FROST, SpellSchools.FROST,
            "frost_tier_4_spell_1_root", FROST_BLIZZARD, "Blizzard", 3));
    public static final Skills.Entry frost_tier_4_spell_2_root = add(SkillsCommon.companionRoot(
            Skills.Category.FROST, SpellSchools.FROST,
            "frost_tier_4_spell_2_root", FROST_ELEMENTAL, "Frost Elemental", 5));

    // ===================================================================================
    // Powerful mutex modifiers for the second spell of each tier (spell_2).
    // frost_spikes (T2), frost_lance / Ice Lance (T3), frost_elemental (T4).
    // ===================================================================================

    public static final Skills.Entry frost_tier_2_spell_2_modifier_1 = add(frost_tier_2_spell_2_modifier_1());
    private static Skills.Entry frost_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_2_spell_2_modifier_1");
        var title = "Glacial Ridge";
        var description = "Frost Spikes raises 4 additional spikes, extending the row.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FROST_SPIKES;

        // Continues the base row: 5 spikes end at 1.5 + 4 * 1.5 = 7.5 blocks, erupting at delays
        // 0/2/4/6/8 — the extension starts one spacing further and keeps the same cascade.
        var extension = SpellBuilder.Placements.ray(4, 1.5F, 9F);
        for (int i = 0; i < extension.size(); i++) {
            extension.get(i).delay_ticks = 10 + i * 2;
        }
        modifier.additional_placements = extension;

        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_2_spell_2_modifier_2 = add(frost_tier_2_spell_2_modifier_2());
    private static Skills.Entry frost_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_2_spell_2_modifier_2");
        var title = "Frost Fan";
        var description = "Frost Spikes raises 2 additional rows of spikes, fanning out to the sides.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FROST_SPIKES;

        // Two rows mirroring the base one at +/-45 degrees, sharing its 2-tick eruption cascade
        // so all three rows march outward together.
        var left = SpellBuilder.Placements.ray(5, 1.5F, 1.5F, -45F);
        var right = SpellBuilder.Placements.ray(5, 1.5F, 1.5F, 45F);
        SpellBuilder.Placements.delayCascade(left, 2);
        SpellBuilder.Placements.delayCascade(right, 2);
        var placements = new ArrayList<Spell.EntityPlacement>(left);
        placements.addAll(right);
        modifier.additional_placements = placements;

        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_3_spell_2_modifier_1 = add(frost_tier_3_spell_2_modifier_1());
    private static Skills.Entry frost_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_3_spell_2_modifier_1");
        var title = "Colossal Lance";
        var bonus = 0.5F;
        var critChance = 0.15F;
        var description = "Ice Lance is " + SpellTooltip.percent(bonus) + "% larger and gains {critical_chance_bonus} increased critical strike chance.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FROST_LANCE;
        // Purely cosmetic: frost_lance has no projectile hitbox, so it uses raycast collision and
        // scale never reaches hit detection. The reward the node actually grants is the crit chance.
        modifier.projectile_scale_multiply = bonus;
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_chance_bonus = critChance;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_3_spell_2_modifier_2 = add(frost_tier_3_spell_2_modifier_2());
    private static Skills.Entry frost_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_3_spell_2_modifier_2");
        var title = "Shattering Lance";
        var description = "Ice Lance hits explode, damaging enemies within {impact_range} blocks.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        var radius = 2.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FROST_LANCE;

        // Same shape as the Holy Blast weapon-skill node: each damaging hit re-executes the
        // spell's damage as a small burst around the struck enemy.
        var area_impact = new Spell.AreaImpact();
        area_impact.triggering_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.radius = radius;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.BURST)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(40).speed(0.5F, 0.5F)),
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_676)
                        .facing(ParticleGroup.Facing.CAMERA)
                        .color(Color.FROST.toRGBA())
                        .scale(radius - 0.5F)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1))
        );
        area_impact.sound = new Sound("wizards:frost_nova_damage_impact");
        modifier.replacing_area_impact = area_impact;

        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_4_spell_2_modifier_1 = add(frost_tier_4_spell_2_modifier_1());
    private static Skills.Entry frost_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_tier_4_spell_2_modifier_1");
        var title = "Elemental Legion";
        var description = "Summons an additional Frost Elemental.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FROST_ELEMENTAL;
        modifier.summon_spawn_count_add = 1;
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    public static final Skills.Entry frost_tier_4_spell_2_modifier_2 = add(frost_tier_4_spell_2_modifier_2());
    private static Skills.Entry frost_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_tier_4_spell_2_modifier_2");
        var title = "Elemental Colossus";
        var description = "Your Frost Elemental grows in size with your Frost Spell Power, and is far more durable.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FROST_ELEMENTAL;

        // Restores what the base summon gave up for this node: the size scaling, plus the
        // halved-away portion of the defensive inheritance (entries merge additively with
        // the summon's own by attribute_id).
        var s = SpellSchools.FROST.id.toString();
        modifier.summon_attribute_scaling = new AttributeScaling();
        modifier.summon_attribute_scaling.entries = List.of(
                summonScaling(EntityAttributes.GENERIC_SCALE.getIdAsString(), s, 0, 0.05),
                summonScaling(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), s, 0, 1.0),
                summonScaling(EntityAttributes.GENERIC_ARMOR.getIdAsString(), s, 5, 0.05),
                summonScaling(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.getIdAsString(), s, 2.5, 0.025)
        );

        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }

    /// A single owner-scaled attribute entry: `targetAttribute += base + ownerAttribute * coefficient`.
    private static AttributeScaling.Entry summonScaling(String targetAttribute, String ownerAttribute,
                                                        double base, double coefficient) {
        var entry = new AttributeScaling.Entry();
        entry.attribute_id = targetAttribute;
        entry.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                ownerAttribute, EntityAttributeModifier.Operation.ADD_VALUE, base, coefficient));
        return entry;
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
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(25).speed(0.1F, 0.3F))
        );
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
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.DECELERATE)
                        .color(SkillsCommon.FROST_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.25F, 0.3F))
        );
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

        var cloudParticles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(2).speed(0.01F, 0.02F)),
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(2).speed(0.02F, 0.05F))
        );
        var cloud = SpellBuilder.Deliver.cloud(
                5,
                1.5F,
                SkillSounds.frost_trap_activate.id(),
                8,
                cloudParticles
        );
        cloud.impact = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.DECELERATE)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.4F, 0.4F).verticalOrigin(Batches.FEET))
        );
        cloud.impact_cap = 1; // Trap

        cloud.client_data.interval_particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_715)
                        .scale(radius * 1.5F) // 1.5F is asset specific
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );
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
        spell.release.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_cast.id(), Color.FROST),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.ASCEND)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.1F, 0.3F).verticalOrigin(Batches.FEET))
        );

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.specificSpellCast(FROST_SPELL_TAG);
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

        var impact = SpellBuilder.Impacts.resetCooldownActive(FROST_SPELL_TAG);
        impact.visuals = Fx.Visuals.of(
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.FROST),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(25).speed(0.2F, 0.2F))
        );
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
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.BURST)
                        .color(SkillsCommon.FROST_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.25F, 0.3F)),
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.25F, 0.3F))
        );
        impact.sound = new Sound("wizards:frost_nova_effect_impact");
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.FROST));
    }
}
