package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.ClassSkillsMod;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.entity.SpellEntityPredicates;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Spells {
    public static final String NAMESPACE = ClassSkillsMod.NAMESPACE;
    public enum Category {
        ARCANE, FIRE, FROST, PRIEST, PALADIN, ROGUE, WARRIOR, ARCHER
    }
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator, EnumSet<Category> categories) {
        public Entry(Identifier id, Spell spell, String title, String description,
                     @Nullable SpellTooltip.DescriptionMutator mutator, Category category) {
            this(id, spell, title, description, mutator, EnumSet.of(category));
        }
        public String key() {
            return id.getPath();
        }
    }

    public static final List<Entry> all = new ArrayList<>();
    private static Entry add(Entry entry) {
        all.add(entry);
        return entry;
    }

    private static Spell createModifierAlikePassiveSpell() {
        var spell = SpellBuilder.createSpellPassive();
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        return spell;
    }

    private static final Identifier HOLY_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HOLY,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();
    private static final Identifier SPARK_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();
    private static final Identifier SPARK_FLOAT = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.FLOAT
    ).id();
    private static final Identifier HEAL_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HEAL,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();


    public static final long ARCANE_COLOR = Color.from(SpellSchools.ARCANE.color).toRGBA();
    public static final long FROST_COLOR = Color.from(SpellSchools.FROST.color).toRGBA();
    public static final long HOLY_COLOR = Color.HOLY.toRGBA();
    public static final Color MIGHT_COLOR = Color.from(0xccffff);

    public static final Entry arcane_spec_a_modifier_1 = add(arcane_spec_a_modifier_1());
    private static Entry arcane_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_modifier_1");
        var title = "Conjured Arcane Charge";
        var description = "Increases the maximum number of Arcane Charges by {effect_amplifier_cap_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_modifier_1 = add(arcane_spec_b_modifier_1());
    private static Entry arcane_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_modifier_1");
        var title = "Arcane Endurance";
        var description = "Increases the duration of Arcane Charges by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_modifier_2 = add(arcane_spec_a_modifier_2());
    private static Entry arcane_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_modifier_2");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_modifier_2 = add(arcane_spec_b_modifier_2());
    private static Entry arcane_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_modifier_2");
        var effect = SkillEffects.ARCANE_SLOWNESS;
        var title = "Crippling Barrage";
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_modifier_3 = add(arcane_spec_a_modifier_3());
    private static Entry arcane_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_modifier_3");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_modifier_3 = add(arcane_spec_b_modifier_3());
    private static Entry arcane_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_modifier_3");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_modifier_4 = add(arcane_spec_a_modifier_4());
    private static Entry arcane_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_modifier_4");
        var title = "Presence of Mind";
        var description = "Blink turns your next spell cast instant, within the next {stash_duration} sec.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        var effect = SkillEffects.PRESENCE_OF_MIND;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast("wizards:arcane_blink");
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.specificSpellCast("#wizards:arcane");
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 5, stashTrigger);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_modifier_4 = add(arcane_spec_b_modifier_4());
    private static Entry arcane_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_modifier_4");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_passive_1 = add(arcane_spec_a_passive_1());
    private static Entry arcane_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_passive_1");
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
                        .color(ARCANE_COLOR),
        };
        spell.area_impact = area_impact;

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_passive_1 = add(arcane_spec_b_passive_1());
    private static Entry arcane_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_passive_1");
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
                new ParticleBatch(SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.1F, 0.1F)
                        .color(ARCANE_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.area_circle_1.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                        1, 0.2F, 0.2F)
                        .followEntity(true)
                        .scale(0.8F)
                        .maxAge(0.8F)
                        .color(ARCANE_COLOR),
                new ParticleBatch(
                        HEAL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F)
                        .color(ARCANE_COLOR)
        };

        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_passive_2 = add(arcane_spec_a_passive_2());
    private static Entry arcane_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_passive_2");
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
                SpellEngineSounds.GENERIC_ARCANE_RELEASE.id(), // FIXME
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_passive_2 = add(arcane_spec_b_passive_2());
    private static Entry arcane_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_passive_2");
        var title = "Phase Shift";
        var description = "Upon rolling, you become invulnerable for {effect_duration} sec.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.passive.triggers = List.of(SpellBuilder.Triggers.roll());

        var effect = SkillEffects.PHASE_SHIFT;

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 2, 0, 0);
        // impact.sound =  FIXME
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_passive_3 = add(arcane_spec_a_passive_3());
    private static Entry arcane_spec_a_passive_3() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_passive_3");
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
                        .color(ARCANE_COLOR)
        };
        projectile.client_data.model = new Spell.ProjectileModel();
        projectile.client_data.model.model_id = "wizards:projectile/arcane_bolt";
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final float WIZARD_WARD_CHANCE = 0.25F;
    public static final float WIZARD_WARD_DURATION = 8F;

    public static final Entry arcane_spec_b_passive_3 = add(arcane_spec_b_passive_3());
    private static Entry arcane_spec_b_passive_3() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_passive_3");
        var effect = SkillEffects.ARCANE_WARD;
        var title = effect.title;
        var description = "Arcane spells have {trigger_chance} chance, to grant you " + effect.title + ", absorbing high amount of damage, lasting {effect_duration} sec.";
        var duration = WIZARD_WARD_DURATION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCast(SpellSchools.ARCANE);
        trigger.chance = WIZARD_WARD_CHANCE;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), duration, 0);
        impact.action.status_effect.amplifier_power_multiplier = 0.4F;
        impact.action.apply_to_caster = true;
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    //
    // FIRE
    //

    public static final Color FIRE_MAGIC_COLOR = Color.from(0xff6600);

    public static final Entry fire_spec_a_modifier_1 = add(fire_spec_a_modifier_1());
    private static Entry fire_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_modifier_1");
        var title = "Blast Radius";

        var bonus = 0.5F;

        var description = "Increases the area of effect of Pyroblast by {bonus}.";
        var mutator = new SpellTooltip.DescriptionMutator() {
            @Override
            public String mutate(Args args) {
                return args.description().replace("{bonus}", SpellTooltip.percent(bonus));
            }
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_blast";
        var extendedRadius = 2.5F * (1F + bonus);
        modifier.replacing_area_impact = SpellBuilder.Complex.fireExplosion(extendedRadius);

        modifier.replacing_area_impact.sound = new Sound("wizards:fireball_impact");

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_modifier_1 = add(fire_spec_b_modifier_1());
    private static Entry fire_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_modifier_1");
        var title = "Blast Punch";
        var description = "Increases the knockback of Pyroblast by {knockback_multiply_base}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var bonus = 0.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_blast";
        modifier.knockback_multiply_base = bonus;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_a_modifier_2 = add(fire_spec_a_modifier_2());
    private static Entry fire_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_modifier_2");
        var title = "Explosive Breath";
        var description = "Fire Breath hits have {trigger_chance} chance to explode a target, dealing {damage} damage to nearby enemies.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit("wizards:fire_breath");
        trigger.chance = 0.1F;
        spell.passive.triggers = List.of(trigger);

        explosionImpact(spell, 0.5F);

        SpellBuilder.Cost.cooldown(spell, 0.5F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    private static void explosionImpact(Spell spell, float coefficient) {
        var impact = SpellBuilder.Impacts.damage(coefficient, 0.2F);
        spell.area_impact = SpellBuilder.Complex.fireExplosion(2.5F);
        spell.impacts = List.of(impact);
    }

    public static final Entry fire_spec_b_modifier_2 = add(fire_spec_b_modifier_2());
    private static Entry fire_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_modifier_2");
        var title = "Flame Throwing";
        var description = "Increased the range of Fire Breath by {range_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_breath";
        modifier.range_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_a_modifier_3 = add(fire_spec_a_modifier_3());
    private static Entry fire_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_modifier_3");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_modifier_3 = add(fire_spec_b_modifier_3());
    private static Entry fire_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_modifier_3");
        var title = "Meteor Splash";
        var description = "Meteor impacts leave a fiery area behind, lasting {cloud_duration} sec.";

        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellAreaImpact("wizards:fire_meteor");
        spell.passive.triggers = List.of(trigger);

        SpellBuilder.Complex.flameCloud(spell, 3.0F, 0.3F, 6, null);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_a_modifier_4 = add(fire_spec_a_modifier_4());
    private static Entry fire_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_modifier_4");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_modifier_4 = add(fire_spec_b_modifier_4());
    private static Entry fire_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_modifier_4");
        var title = "Healing Flames";
        var description = "Wall of Flames heals you and allies for {heal}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_wall";
        var impact = SpellBuilder.Impacts.heal(0.025F);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final int FIRE_VULNERABILITY_DURATION = 8; // seconds

    public static final Entry fire_spec_a_passive_1 = add(fire_spec_a_passive_1());
    private static Entry fire_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_passive_1");
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
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_passive_1 = add(fire_spec_b_passive_1());
    private static Entry fire_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_passive_1");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_a_passive_2 = add(fire_spec_a_passive_2()); // Fire trap
    private static Entry fire_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_passive_2");
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
                SpellEngineSounds.GENERIC_ARCANE_RELEASE.id(), // FIXME
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
        damage.sound = new Sound("wizards:fire_scorch_impact");
        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), FIRE_VULNERABILITY_DURATION, 1, 4);
        spell.impacts = List.of(damage, debuff);

        var area_impact = new Spell.AreaImpact();
        area_impact.radius = radius;
        spell.area_impact = area_impact;

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_passive_2 = add(fire_spec_b_passive_2()); // Blazing Speed
    private static Entry fire_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_passive_2");
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
        // impact.sound = SpellEngineSounds.
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_a_passive_3 = add(fire_spec_a_passive_3());
    private static Entry fire_spec_a_passive_3() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_passive_3");
        var title = "Eruption";
        var description = "Taking damage has {trigger_chance} chance to cause a strong explosion, dealing {damage} damage to nearby enemies.";
        var radius = 5F;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = radius;

        var trigger = SpellBuilder.Triggers.damageTaken();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

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
        damage.sound = new Sound("wizards:fire_meteor_impact");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_passive_3 = add(fire_spec_b_passive_3()); // Flame Shield
    private static Entry fire_spec_b_passive_3() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_passive_3");
        var effect = SkillEffects.FIRE_WARD;
        var title = effect.title;
        var description = "Fire spells have {trigger_chance_1} chance, to grant you " + effect.title + ", absorbing damage and dealing {damage} damage to attackers, lasts {stash_duration} sec.";
        var duration = WIZARD_WARD_DURATION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var spell_trigger = SpellBuilder.Triggers.activeSpellCast(SpellSchools.FIRE);
        spell_trigger.chance = WIZARD_WARD_CHANCE;
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    //
    // FROST
    //

    public static final Entry frost_spec_a_modifier_1 = add(frost_spec_a_modifier_1());
    private static Entry frost_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_modifier_1");
        var title = "Frost Bounce";
        var description = "Frostbolt ricochets to {ricochet} additional target.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.projectile_perks = Spell.ProjectileData.Perks.EMPTY();
        modifier.projectile_perks.ricochet = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_modifier_1 = add(frost_spec_b_modifier_1());
    private static Entry frost_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_modifier_1");
        var title = "Lingering Chill";
        var description = "Frostbolt slow effect lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_a_modifier_2 = add(frost_spec_a_modifier_2());
    private static Entry frost_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_modifier_2");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_modifier_2 = add(frost_spec_b_modifier_2());
    private static Entry frost_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_modifier_2");
        var title = "Deep Freeze";
        var description = "Frost Nova applies {effect_amplifier_add} more stack of Freeze effect.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_nova";
        modifier.effect_amplifier_add = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_a_modifier_3 = add(frost_spec_a_modifier_3());
    private static Entry frost_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_modifier_3");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_modifier_3 = add(frost_spec_b_modifier_3());
    private static Entry frost_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_modifier_3");
        var title = "Durable Shield";
        var description = "Increases the duration of Frost Shield by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frost_shield";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_a_modifier_4 = add(frost_spec_a_modifier_4());
    private static Entry frost_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_modifier_4");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_modifier_4 = add(frost_spec_b_modifier_4());
    private static Entry frost_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_modifier_4");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_a_passive_1 = add(frost_spec_a_passive_1());
    private static Entry frost_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_passive_1");
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
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.FROST));
    }

    public static final String WIZARDS_FREEZE_EFFECT = "wizards:frozen";

    public static final Entry frost_spec_b_passive_1 = add(frost_spec_b_passive_1());
    private static Entry frost_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_passive_1");
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
                        .color(FROST_COLOR)
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_a_passive_2 = add(frost_spec_a_passive_2()); // Frost Trap
    private static Entry frost_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_passive_2");
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
                SpellEngineSounds.GENERIC_ARCANE_RELEASE.id(), // FIXME
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_passive_2 = add(frost_spec_b_passive_2());
    private static Entry frost_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_passive_2");
        var title = "Arctic Reflex";
        var description = "Upon rolling, you have {trigger_chance_1} chance to instantly cast a spell, within the next {stash_duration} sec.";
        var effect = SkillEffects.ARCTIC_REFLEX;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.specificSpellCast("#wizards:frost");
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 5, stashTrigger);

        // No impacts, stash will just be consumed

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_a_passive_3 = add(frost_spec_a_passive_3());
    private static Entry frost_spec_a_passive_3() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_passive_3");
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
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_passive_3 = add(frost_spec_b_passive_3()); // Frost Shield
    private static Entry frost_spec_b_passive_3() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_passive_3");
        var effect = SkillEffects.FROST_WARD;
        var title = effect.title;
        var description = "Frost spells have {trigger_chance_1} chance, to grant you " + effect.title + ", absorbing damage and slowing attackers, lasts {stash_duration} sec.";
        var duration = WIZARD_WARD_DURATION;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var spell_trigger = SpellBuilder.Triggers.activeSpellCast(SpellSchools.FROST);
        spell_trigger.chance = WIZARD_WARD_CHANCE;
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
                        .color(FROST_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.25F, 0.3F)
        };
        impact.sound = new Sound("wizards:frost_nova_effect_impact");
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, duration * 2);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    //
    // PRIEST
    //

    public static final Entry priest_spec_a_modifier_1 = add(priest_spec_a_modifier_1());
    private static Entry priest_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_modifier_1");
        var title = "Improved Healing";
        var description = "Holy Shock heals for {power_multiplier} more.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var bonus = 0.2F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_shock";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = bonus;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_modifier_1 = add(priest_spec_b_modifier_1());
    private static Entry priest_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_modifier_1");
        var title = "Holy Blast";
        var description = "Damaging with Holy Shock causes small explosion, hitting enemies within {impact_range} blocks radius.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;
        var radius = 2.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_shock";
        modifier.replacing_area_impact = new Spell.AreaImpact();
        modifier.replacing_area_impact.triggering_action_type = Spell.Impact.Action.Type.DAMAGE;
        modifier.replacing_area_impact.radius = radius;
        modifier.replacing_area_impact.area = new Spell.Target.Area();
        modifier.replacing_area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;

        modifier.replacing_area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        HOLY_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.5F, 0.5F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.aura_effect_649.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1, 0, 0)
                        .color(Color.HOLY.toRGBA())
                        .scale(radius - 0.5F),
        };

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_a_modifier_2 = add(priest_spec_a_modifier_2());
    private static Entry priest_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_modifier_2");
        var title = "Graceful Channeling";
        var description = "Reduces the cooldown of Holy Light by {cooldown_duration_deduct} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_beam";
        modifier.cooldown_duration_deduct = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_modifier_2 = add(priest_spec_b_modifier_2());
    private static Entry priest_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_modifier_2");
        var title = "Searing Light";
        var description = "Holy Light deals {power_multiplier} more damage, and lights enemies on fire.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_beam";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.1F;

        var impact = SpellBuilder.Impacts.fire(2F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        1, 0.1F, 0.2F),
                new ParticleBatch(
                        SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        1, 0.1F, 0.2F)
        };
        impact.sound = Sound.withVolume(SpellEngineSounds.GENERIC_FIRE_IGNITE.id(), 0.6F);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_a_modifier_3 = add(priest_spec_a_modifier_3());
    private static Entry priest_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_modifier_3");
        var title = "Mass Dispel";
        var description = "Circle of Healing removes {effect_amplifier} negative effect from allies.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:circle_of_healing";

        var impact = SpellBuilder.Impacts.effectCleanse();
        impact.particles = new ParticleBatch[]{
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
        impact.sound = new Sound(SpellEngineSounds.GENERIC_DISPEL_1.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_modifier_3 = add(priest_spec_b_modifier_3());
    private static Entry priest_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_modifier_3");
        var title = "Consecration";
        var description = "Circle of Healing leaves a consecrated area behind, dealing {damage} damage to enemies, for {cloud_duration} sec.";

        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast("paladins:circle_of_healing");
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var radius = 6.0F;
        consecration(spell, 0.2F, radius, 4);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    private static void consecration(Spell spell, float coefficient, float radius, float particleMultiplier) {
        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = radius;
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 5;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
                        3 * particleMultiplier, 0.05F, 0.1F)
                        .color(HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
                        3 * particleMultiplier, 0.05F, 0.15F)
                        .color(HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        3 * particleMultiplier, 0.05F, 0.15F)
                        .color(HOLY_COLOR).extent(radius),
        };
        spell.deliver.clouds = List.of(cloud);

        var impact = SpellBuilder.Impacts.damage(coefficient, 0.1F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        10, 0.4F, 0.4F)
                        .color(HOLY_COLOR),
        };
        spell.impacts = List.of(impact);
    }

    public static final Entry priest_spec_a_modifier_4 = add(priest_spec_a_modifier_4());
    private static Entry priest_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_modifier_4");
        var title = "Barrier Recovery";
        var description = "Reduces the cooldown of Barrier by {cooldown_duration_deduct} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:barrier";
        modifier.cooldown_duration_deduct = 10;

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_modifier_4 = add(priest_spec_b_modifier_4());
    private static Entry priest_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_modifier_4");
        var title = "Barrier Duration";
        var description = "Increases the duration of Barrier by {spawn_duration_add} sec.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:barrier";
        modifier.spawn_duration_add = 4;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_a_passive_1 = add(priest_spec_a_passive_1());
    private static Entry priest_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_passive_1");
        var effect = SkillEffects.HEALING_FOCUS;
        var title = "Healing Focus";
        var description = "Healing spells apply Healing Focus effect. Increasing healing received by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHeal(1F);
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 1, 4);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.area_circle_1.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                        1, 0.15F, 0.16F)
                        .followEntity(true)
                        .scale(0.8F)
                        .maxAge(0.8F)
                        .color(Color.HOLY.toRGBA()),
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_passive_1 = add(priest_spec_b_passive_1());
    private static Entry priest_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_passive_1");
        var effect = SkillEffects.INCANTER_CADENCE;
        var title = "Incanters' Cadence";
        var description = "Spell hits have {trigger_chance} chance to increase spell haste by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.5F, null);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        10, 0.15F, 0.3F)
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_a_passive_2 = add(priest_spec_a_passive_2()); // Fade
    private static Entry priest_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_passive_2");
        var title = "Fade";
        var description = "Upon rolling, nearby mobs stop attacking you, allowing them to target your allies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 15;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();

        var impact = SpellBuilder.Impacts.disengage(true);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.2F)
                        .color(Color.HOLY.toRGBA())
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_passive_2 = add(priest_spec_b_passive_2()); // Divine Favor
    private static Entry priest_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_passive_2");
        var effect = SkillEffects.DIVINE_FAVOR;
        var title = effect.title;
        var description = "Upon rolling, you have {trigger_chance_1} chance to guarantee critical strike for your next spell cast.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        spell.release.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.HOLY),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        25, 0.2F, 0.2F)
                        .color(Color.HOLY.toRGBA())
        };
        spell.release.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());

        var cooldownDuration = 15F;
        var stashTriggers = List.of(
                SpellBuilder.Triggers.activeSpellCast(),
                SpellBuilder.Triggers.activeSpellHit(1, null)
        );
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), cooldownDuration, stashTriggers);
        spell.deliver.stash_effect.consumed_next_tick = true;

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_a_passive_3 = add(priest_spec_a_passive_3()); // Pain Suppression
    private static Entry priest_spec_a_passive_3() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_passive_3");
        var effect = SkillEffects.PAIN_SUPPRESSION;
        var title = effect.title;
        var healthThreshold = 0.3F;
        var description = "Healing targets under {threshold} health, grants them " + effect.title + ", reducing damage taken by {bonus}, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(effect.config().firstModifier().value));
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{threshold}", SpellTooltip.percent(healthThreshold));
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHeal(1F);
        trigger.stage = Spell.Trigger.Stage.PRE;
        trigger.target_conditions = List.of(SpellBuilder.TargetConditions.lowHP(healthThreshold));
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.2F)
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PRIEST));
    }

    public static final Entry priest_spec_b_passive_3 = add(priest_spec_b_passive_3()); // Celestial Orbs
    private static Entry priest_spec_b_passive_3() {
        var id = Identifier.of(NAMESPACE, "priest_spec_b_passive_3");
        var effect = SkillEffects.CELESTIAL_ORB;
        var title = "Celestial Orbs";
        var description = "Spell critical strikes and heals grant you {stash_amplifier} Divine Charges. Charges damage enemies attacking you, dealing {damage} spell damage.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCrit();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 15, SpellBuilder.Triggers.damageTaken());
        spell.deliver.stash_effect.amplifier = 2;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0.1F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.2F)
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }

    //
    // PALADIN
    //

    public static final Entry paladin_spec_a_modifier_1 = add(paladin_spec_a_modifier_1());
    private static Entry paladin_spec_a_modifier_1() {
        var effect = SkillEffects.DIVINE_STRENGTH;

        var id = Identifier.of(NAMESPACE, "paladin_spec_a_modifier_1");
        var title = "Divine Strength";
        var description = "Flash Heal increases Attack Damage by {bonus} for {effect_duration} sec.";

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:flash_heal";

        var impact = SpellBuilder.Impacts.effectSet(SkillEffects.DIVINE_STRENGTH.id.toString(), 8, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.05F, 0.1F)
                        .color(MIGHT_COLOR.toRGBA()),
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), MIGHT_COLOR)
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_modifier_1 = add(paladin_spec_b_modifier_1());
    private static Entry paladin_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_modifier_1");
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
                    HEAL_DECELERATE.toString(),
                    ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                    20, 0.25F, 0.3F
                ).color(Color.HOLY.toRGBA())
        };
        impact.action.status_effect.amplifier = cleanseCount;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_a_modifier_2 = add(paladin_spec_a_modifier_2());
    private static Entry paladin_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_a_modifier_2");
        var title = "Pursuit of Justice";
        var description = "Divine Protection also increases your movement speed by {bonus}, for {effect_duration} sec.";
        var effect = SkillEffects.PURSUIT_OF_JUSTICE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:divine_protection";

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_modifier_2 = add(paladin_spec_b_modifier_2());
    private static Entry paladin_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_modifier_2");
        var title = "Blessed Protection";
        var description = "Divine Protection provides {effect_amplifier_add} extra effect stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:divine_protection";
        modifier.effect_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_a_modifier_3 = add(paladin_spec_a_modifier_3());
    private static Entry paladin_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_a_modifier_3");
        var title = "Empowered Judgement";
        var description = "Increases the damage of Judgement by {power_multiplier}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:judgement";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.2F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_modifier_3 = add(paladin_spec_b_modifier_3());
    private static Entry paladin_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_modifier_3");
        var title = "Judgement of Command";
        var description = "Judgement taunts enemies hit, forcing them to attack you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:judgement";

        var impact = SpellBuilder.Impacts.taunt();
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_a_modifier_4 = add(paladin_spec_a_modifier_4());
    private static Entry paladin_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_a_modifier_4");
        var title = "Persistent Banner";
        var description = "Increases the duration of Battle Banner by {spawn_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:battle_banner";
        modifier.spawn_duration_add = 4;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_modifier_4 = add(paladin_spec_b_modifier_4());
    private static Entry paladin_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_modifier_4");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_a_passive_1 = add(paladin_spec_a_passive_1());
    private static Entry paladin_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_a_passive_1");
        var title = "Seal of Righteousness";
        var description = "Melee attacks have {trigger_chance} chance, to deal additional {damage} damage based on Healing Power.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.meleeAttack(false);
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.5F, 0.8F)
                        .color(HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.5F, 0.8F)
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_passive_1 = add(paladin_spec_b_passive_1());
    private static Entry paladin_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_passive_1");
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
                        .color(MIGHT_COLOR.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_a_passive_2 = add(paladin_spec_a_passive_2()); // Crusader Strike
    private static Entry paladin_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_a_passive_2");
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
                        .color(HOLY_COLOR),
        };

        var stashEffect = SkillEffects.SEAL_OF_CRUSADER;
        var strashTrigger = SpellBuilder.Triggers.meleeAttack(false);
        SpellBuilder.Deliver.stash(spell, stashEffect.id.toString(), 5, strashTrigger);

        var debuff = SpellBuilder.Impacts.effectAdd(debuffEffect.id.toString(), 15, 1, 2);
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.7F, 0.8F)
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(debuff);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_passive_2 = add(paladin_spec_b_passive_2()); // Conviction (rolling resets Flash Heal cooldown)
    private static Entry paladin_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_passive_2");
        var title = "Conviction";
        var description = "Upon rolling, you have {trigger_chance} chance to reset the cooldown of Flash Heal.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive("paladins:flash_heal");
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.HOLY),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.3F)
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_a_passive_3 = add(paladin_spec_a_passive_3()); // Divine Hammer
    private static Entry paladin_spec_a_passive_3() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_a_passive_3");
        var title = "Divine Hammer";
        var description = "Melee attacks throw a hammer at the target, dealing {damage} damage, ricocheting {ricochet} to nearby enemies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 5;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.meleeAttack(false);
        trigger.chance = 1F;
        spell.passive.triggers = List.of(trigger);

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
        model.model_id = "paladins:projectile/judgement";
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
                        .color(HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PALADIN));
    }

    public static final Entry paladin_spec_b_passive_3 = add(paladin_spec_b_passive_3()); // Ardent Defender (hp boost on low HP)
    private static Entry paladin_spec_b_passive_3() {
        var id = Identifier.of(NAMESPACE, "paladin_spec_b_passive_3");
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

        var trigger = SpellBuilder.Triggers.becomingLowHP(healthThreshold);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

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
                        .color(Color.HOLY.toRGBA())
        };
        var heal = SpellBuilder.Impacts.heal(0.5F);
        spell.impacts = List.of(buff, heal);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.PALADIN));
    }

    //
    // ROGUE
    //

    public static final Entry rogue_spec_a_modifier_1 = add(rogue_spec_a_modifier_1());
    private static Entry rogue_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_modifier_1");
        var title = "Fleet Footed";
        var effect = SkillEffects.FLEET_FOOTED;
        var description = "Slice and Dice attacks increases movement speed by {bonus}, stacking up to {effect_amplifier_cap}, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:slice_and_dice";

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 4, 1, 4);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_b_modifier_1 = add(rogue_spec_b_modifier_1());
    private static Entry rogue_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_b_modifier_1");
        var title = "Blade Fury";
        var description = "Increases the maximum number of Slice and Dice stacks by {effect_amplifier_cap_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:slice_and_dice";
        modifier.effect_amplifier_cap_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_a_modifier_2 = add(rogue_spec_a_modifier_2());
    private static Entry rogue_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_modifier_2");
        var title = "Toxic Shock";
        var description = "Shock Powder deals extra {damage} damage to poisoned targets.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:shock_powder";

        var impact = SpellBuilder.Impacts.damage(0.6F, 0F);
        SpellBuilder.configureImpactEnableCondition(impact,
                SpellBuilder.TargetConditions.ofPredicate(SpellEntityPredicates.IS_POISONED));
        impact.particles = new ParticleBatch[] {
                new ParticleBatch(SpellEngineParticles.smoke_large.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        5, 0.01F, 0.05F)
                        .color(Color.POISON_LIGHT.toRGBA()),
                new ParticleBatch(SpellEngineParticles.smoke_large.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        5, 0.01F, 0.05F)
                        .color(Color.POISON_MID.toRGBA()),
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_b_modifier_2 = add(rogue_spec_b_modifier_2());
    private static Entry rogue_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_b_modifier_2");
        var title = "Explosive Powder";
        var description = "Shock Powder has {trigger_chance} chance to create secondary explosions, dealing {damage} damage.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellHit("rogues:shock_powder");
        trigger.impact.impact_type = null;
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        explosionImpact(spell, 0.6F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry rogue_spec_a_modifier_3 = add(rogue_spec_a_modifier_3());
    private static Entry rogue_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_modifier_3");
        var title = "Cloak of Shadows";
        var description = "Shadowstep grants you Cloak of Shadows effect, protecting your from {effect_amplifier} incoming attack for {effect_duration} sec.";

        var effect = SkillEffects.CLOAK_OF_SHADOWS;

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:shadow_step";

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 5, 0, 1);
        impact.action.apply_to_caster = true;

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_b_modifier_3 = add(rogue_spec_b_modifier_3());
    private static Entry rogue_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_b_modifier_3");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_a_modifier_4 = add(rogue_spec_a_modifier_4());
    private static Entry rogue_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_modifier_4");
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_b_modifier_4 = add(rogue_spec_b_modifier_4());
    private static Entry rogue_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_b_modifier_4");
        var title = "Deep Stealth";
        var description = "Increases the duration of Stealth by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:vanish";
        modifier.effect_duration_add = 8;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_a_passive_1 = add(rogue_spec_a_passive_1());
    private static Entry rogue_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_passive_1");
        var title = "Coated Blades";
        var description = "Melee attacks have {trigger_chance} chance, to apply poison effect lasting {effect_duration} sec, stacking up based on your attack damage.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.meleeAttack(false);
        trigger.chance = 0.2F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(StatusEffects.POISON.getIdAsString(), 8, 1, 1);
        impact.action.status_effect.amplifier_cap_power_multiplier = 0.5F;
        impact.particles = poisonImpactParticles();
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ROGUE));
    }

    private static ParticleBatch[] poisonImpactParticles() {
        return new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.5F, 0.8F)
                        .color(Color.POISON_MID.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.5F, 0.8F)
                        .color(Color.POISON_DARK.toRGBA()),
        };
    }

    public static final Entry rogue_spec_b_passive_1 = add(rogue_spec_b_passive_1());
    private static Entry rogue_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_b_passive_1");
        var effect = SkillEffects.FRACTURE;
        var title = effect.title;
        var description = "Melee attacks have {trigger_chance} chance to wound the enemy, dealing {damage} damage and reducing its armor by {bonus}, for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(effect.config().firstModifier().value));
            return args.description().replace("{bonus}", bonus);
        };

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver.delay = 1;

        var trigger = SpellBuilder.Triggers.meleeAttack(false);
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var damage = SpellBuilder.Impacts.damage(0.5F, 0F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.5F, 0.8F)
                        .color(Color.BLOOD.toRGBA()),
        };
        var debuff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 6, 1, 1);
        spell.impacts = List.of(damage, debuff);

        SpellBuilder.Cost.cooldown(spell, 6F);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ROGUE));
    }

    public static final Entry rogue_spec_a_passive_2 = add(rogue_spec_a_passive_2()); // Sprint (movement speed on roll)
    private static Entry rogue_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_passive_2");
        var effect = SkillEffects.SPRINT;
        var title = effect.title;
        var description = "Upon rolling, you have {trigger_chance} chance to gain Sprint effect, increasing your movement speed by {bonus}, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.bonus(effect.config().firstModifier().value, effect.config().firstModifier().operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        buff.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_speed.id(), MIGHT_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND
                        ).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.3F).color(MIGHT_COLOR.toRGBA())
        };
        spell.impacts = List.of(buff);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ROGUE));
    }

//    public static final Entry rogue_spec_b_passive_2 = add(rogue_spec_b_passive_2()); // Sidestep (stacking stashed evasion on roll)
//    private static Entry rogue_spec_b_passive_2() {
//        var id = Identifier.of(NAMESPACE, "rogue_spec_b_passive_2");
//        var title = "Sidestep";
//        var description = "Upon rolling, you gain a stack of Sidestep, increasing your Evasion Chance by {bonus}, stacking up to {stash_amplifier} times. Removed when taking damage.";
//
//        var effect = SkillEffects.;
//        SpellTooltip.DescriptionMutator mutator = (args) -> {
//            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
//            return args.description().replace("{bonus}", bonus);
//        };
//
//        var spell = SpellBuilder.createSpellPassive();
//        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
//        spell.range = 0;
//
//        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
//
//        var trigger = SpellBuilder.Triggers.roll();
//        spell.passive.triggers = List.of(trigger);
//
//        var stacks = 5;
//        var stashTrigger = SpellBuilder.Triggers.meleeAttack(false);
//        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 12, stashTrigger);
//        spell.deliver.stash_effect.amplifier = stacks - 1;
//        spell.deliver.stash_effect.stacking = true;
//        spell.deliver.stash_effect.consume = stacks;
//        spell.deliver.stash_effect.consumed_next_tick = true;
//        spell.deliver.stash_effect.consume_any_stacks = true;
//
//        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 8, 0);
//        buff.particles = new ParticleBatch[]{
//                new ParticleBatch(
//                        SpellEngineParticles.MagicParticles.get(
//                                SpellEngineParticles.MagicParticles.Shape.SPARK,
//                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
//                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
//                        20, 0.4F, 0.5F)
//                        .color(MIGHT_COLOR.toRGBA()),
//                new ParticleBatch(
//                        SpellEngineParticles.MagicParticles.get(
//                                SpellEngineParticles.MagicParticles.Shape.SPARK,
//                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
//                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
//                        20, 0.7F, 0.8F)
//                        .color(MIGHT_COLOR.toRGBA())
//        }
//
//    }

    public static final Entry rogue_spec_b_passive_2 = add(rogue_spec_b_passive_2()); // Dynamo (stacking damage on roll)
    private static Entry rogue_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_b_passive_2");
        var title = "Dynamo";
        var description = "Upon rolling, you gain a stack of Dynamo, increasing your next attack damage by {bonus}, stacking up to {stash_amplifier} times.";

        var effect = SkillEffects.DYNAMO;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        var stacks = 5;
        var stashTrigger = SpellBuilder.Triggers.meleeAttack(false);
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 12, stashTrigger);
        spell.deliver.stash_effect.amplifier = stacks - 1;
        spell.deliver.stash_effect.stacking = true;
        spell.deliver.stash_effect.consume = stacks;
        spell.deliver.stash_effect.consumed_next_tick = true;
        spell.deliver.stash_effect.consume_any_stacks = true;

        var damage = SpellBuilder.Impacts.damage(0F, 0);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.4F, 0.5F)
                        .color(MIGHT_COLOR.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.7F, 0.8F)
                        .color(MIGHT_COLOR.toRGBA())
        };
        spell.impacts = List.of(damage);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ROGUE));
    }

    //
    // WARRIOR
    //

    public static final Entry warrior_spec_a_modifier_1 = add(warrior_spec_a_modifier_1());
    private static Entry warrior_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_modifier_1");
        var title = "Bouncing Throw";
        var description = "Shattering Throw ricochets to {ricochet} additional target.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:throw";
        modifier.projectile_perks = Spell.ProjectileData.Perks.EMPTY();
        modifier.projectile_perks.ricochet = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_modifier_1 = add(warrior_spec_b_modifier_1());
    private static Entry warrior_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_modifier_1");
        var title = "Punching Throw";
        var description = "Shattering Throw deals {knockback_multiply_base} more knockback.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var bonus = 0.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:throw";
        modifier.knockback_multiply_base = bonus;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_a_modifier_2 = add(warrior_spec_a_modifier_2());
    private static Entry warrior_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_modifier_2");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_modifier_2 = add(warrior_spec_b_modifier_2());
    private static Entry warrior_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_modifier_2");
        var title = "Challenging Shout";
        var description = "Shout taunts all affected enemies.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:shout";

        var impact = SpellBuilder.Impacts.taunt();
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_a_modifier_3 = add(warrior_spec_a_modifier_3());
    private static Entry warrior_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_modifier_3");
        var title = "Endurance";
        var description = "Charge lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:charge";
        modifier.effect_duration_add = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_modifier_3 = add(warrior_spec_b_modifier_3());
    private static Entry warrior_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_modifier_3");
        var title = "Concussion Blow";
        var description = "Next attack after using Charge, stuns the target for {effect_duration} sec.";
        var stashEffect = SkillEffects.CONCUSSION_BLOW;

        var spell = createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.specificSpellCast("rogues:charge");
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.triggers = List.of(
                SpellBuilder.Triggers.meleeAttack(false));
        spell.deliver.stash_effect.consumed_next_tick = true;

        var impact = SpellBuilder.Impacts.stun(2F);
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_a_modifier_4 = add(warrior_spec_a_modifier_4());
    private static Entry warrior_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_modifier_4");
        var title = "Whirlwind Mastery";
        var description = "Whirlwind deals {power_multiplier} more damage.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:whirlwind";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.3F;

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_modifier_4 = add(warrior_spec_b_modifier_4());
    private static Entry warrior_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_modifier_4");
        var title = "Hamstring";
        var description = "Whirlwind has {impact_chance} chance to immobilize the target for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var effect = SkillEffects.HAMSTRING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:whirlwind";

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 3, 0);
        impact.chance = 0.3F;

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_a_passive_1 = add(warrior_spec_a_passive_1());
    private static Entry warrior_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_passive_1");
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

        var trigger = SpellBuilder.Triggers.meleeKill(false);
        spell.passive.triggers = List.of(trigger);

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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_passive_1 = add(warrior_spec_b_passive_1());
    private static Entry warrior_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_passive_1");
        var title = "Vitality";
        var description = "Blocking with shield has {trigger_chance} chance to heal you for {heal} health.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        trigger.chance = 0.5F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        impact.action.apply_to_caster = true;
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

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_a_passive_2 = add(warrior_spec_a_passive_2());
    private static Entry warrior_spec_a_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_passive_2");
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
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_passive_2 = add(warrior_spec_b_passive_2());
    private static Entry warrior_spec_b_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_passive_2");
        var title = "Trample";
        var description = "Shortly after rolling, you deal {damage} damage to nearby enemies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var stashEffect = SkillEffects.TRAMPLE;
        var stashTrigger = SpellBuilder.Triggers.effectTick(stashEffect.id.toString());
        SpellBuilder.Deliver.stash(spell, stashEffect.id.toString(), 0.5F, List.of(stashTrigger));
        spell.deliver.stash_effect.consume = 0;

        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        spell.impacts = List.of(impact);
        var areaImpact = new Spell.AreaImpact();
        areaImpact.radius = 2F;
        areaImpact.force_indirect = true;
        areaImpact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        10, 0.3F, 0.3F)
        };
        spell.area_impact = areaImpact;

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_a_passive_3 = add(warrior_spec_a_passive_3()); // Enrage (on damage taken, gain Enrage effect)
    private static Entry warrior_spec_a_passive_3() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_a_passive_3");
        var effect = SkillEffects.ENRAGE;
        var title = effect.title;
        var description = "Taking damage has {trigger_chance_1} chance to apply Enrage effect, increasing your Size and Attack Speed by {bonus} but also the damage you take, staking up to {effect_amplifier_cap} times, lasting {stash_duration} sec.";
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

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 10F, List.of(
                SpellBuilder.Triggers.damageTaken()
        ));
        spell.deliver.stash_effect.consume = 0;

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10F, 1, 2);
        buff.action.apply_to_caster = true;
        buff.action.status_effect.refresh_duration = false;
        buff.particles = activateParticles;
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.WARRIOR));
    }

    public static final Entry warrior_spec_b_passive_3 = add(warrior_spec_b_passive_3()); // Shockwave (like Ardent Defender)
    private static Entry warrior_spec_b_passive_3() {
        var id = Identifier.of(NAMESPACE, "warrior_spec_b_passive_3");
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

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.WARRIOR));
    }

    //
    // ARCHER
    //

    public static final Entry archer_spec_a_modifier_1 = add(archer_spec_a_modifier_1());
    private static Entry archer_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_spec_a_modifier_1");
        var title = "Improved Hunter's Mark";
        var description = "Power Shot applies {stash_amplifier_add} additional Hunter's Mark stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:power_shot";
        modifier.stash_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_b_modifier_1 = add(archer_spec_b_modifier_1());
    private static Entry archer_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "archer_spec_b_modifier_1");
        var title = "Charged Shot";
        var description = "Power Shot deals {damage} damage around the target hit.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var radius = 3F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:power_shot";

        var impact = SpellBuilder.Impacts.damage(0.5F, 0);
        impact.action.allow_on_center_target = false;

        var area_impact = new Spell.AreaImpact();
        area_impact.execute_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.radius = radius;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.35F, 0.35F
                ).color(Color.RED.toRGBA()),
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.15F, 0.15F
                )
        };

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        modifier.replacing_area_impact = area_impact;

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_a_modifier_2 = add(archer_spec_a_modifier_2());
    private static Entry archer_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_spec_a_modifier_2");
        var title = "Nettle Sprouts";
        var description = "Entangling Roots has {impact_chance} chance to apply stacking poison, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:entangling_roots";

        var impact = SpellBuilder.Impacts.effectAdd(StatusEffects.POISON.getIdAsString(), 5, 1, 1);
        impact.chance = 0.5F;
        impact.action.status_effect.amplifier_cap_power_multiplier = 0.2F;
        impact.particles = poisonImpactParticles();

        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_b_modifier_2 = add(archer_spec_b_modifier_2());
    private static Entry archer_spec_b_modifier_2() {
        var id = Identifier.of(NAMESPACE, "archer_spec_b_modifier_2");
        var title = "Nature's Grasp";
        var description = "Entangling Roots has {impact_chance} chance to immobilize the target for {effect_duration} sec.";
        var effect = SkillEffects.NATURES_GRASP;
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:entangling_roots";
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        impact.chance = 0.3F;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_a_modifier_3 = add(archer_spec_a_modifier_3());
    private static Entry archer_spec_a_modifier_3() {
        var id = Identifier.of(NAMESPACE, "archer_spec_a_modifier_3");
        var title = "Extensive Barrage";
        var description = "Barrage fires {extra_launch} extra arrow.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:barrage";
        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1; // TODO: Check if works for arrows
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_b_modifier_3 = add(archer_spec_b_modifier_3());
    private static Entry archer_spec_b_modifier_3() {
        var id = Identifier.of(NAMESPACE, "archer_spec_b_modifier_3");
        var title = "Blood Barrage";
        var description = "Barrage arrow hits heal you by {heal}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:barrage";
        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(SPARK_FLOAT.toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.02F, 0.1F)
                        .color(Color.BLOOD.toRGBA()),
                new ParticleBatch(SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.08F, 0.12F)
                        .invert()
                        .preSpawnTravel(5)
                        .followEntity(true)
                        .color(Color.BLOOD.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.ground_glow.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.GROUND,
                        1, 0.0F, 0.F)
                        .followEntity(true)
                        .scale(0.8F)
                        .color(Color.BLOOD.alpha(0.2F).toRGBA())
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_a_modifier_4 = add(archer_spec_a_modifier_4());
    private static Entry archer_spec_a_modifier_4() {
        var id = Identifier.of(NAMESPACE, "archer_spec_a_modifier_4");
        var title = "Conjured Arrow";
        var description = "Magic Arrow has {trigger_chance} chance to reset its own cooldown.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var trigger = SpellBuilder.Triggers.specificSpellCast("archers:magic_arrow");
        trigger.chance = 0.4F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive("archers:magic_arrow");
        impact.action.apply_to_caster = true;
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_b_modifier_4 = add(archer_spec_b_modifier_4());
    private static Entry archer_spec_b_modifier_4() {
        var id = Identifier.of(NAMESPACE, "archer_spec_b_modifier_4");
        var title = "Magic Punch";
        var description = "Magic Arrow deals extra {knockback_multiply_base} knockback.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "archers:magic_arrow";
        modifier.knockback_multiply_base = 1.5F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }

    private static SpellEntityPredicates.Entry HAS_HUNTERS_MARK = SpellEntityPredicates.hasEffectOptimized(Identifier.of("archers", "hunters_mark"));
    public static final Entry archer_spec_a_passive_1 = add(archer_spec_a_passive_1());
    private static Entry archer_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_spec_a_passive_1");
        var title = "Rhythm";
        var description = "Hitting Marked target increasing ranged attack speed by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var effect = SkillEffects.RHYTHM;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        var condition = new Spell.TargetCondition();
        condition.entity_predicate_id = HAS_HUNTERS_MARK.id().toString();
        trigger.target_conditions = List.of(condition);
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(SkillEffects.RHYTHM.id.toString(), 6, 1, 4);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.4F)
                        .color(Color.NATURE.toRGBA())
        };
        spell.impacts = List.of(impact);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ARCHER));
    }

    public static final Entry archer_spec_b_passive_1 = add(archer_spec_b_passive_1());
    private static Entry archer_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "archer_spec_b_passive_1");
        var title = "Concussive Shot";
        var description = "Arrows have {trigger_chance} chance, to stun the target for {effect_duration} sec.";
        var effect = SpellEngineEffects.STUN;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.chance = 0.2F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2F, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "crit",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.25F, 0.3F)
                        .color(Color.RED.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCHER));
    }
}
