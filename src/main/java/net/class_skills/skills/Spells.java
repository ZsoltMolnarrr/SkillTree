package net.class_skills.skills;

import net.class_skills.ClassSkillsMod;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
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

    private static final Identifier HOLY_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HOLY,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();
    private static final Identifier SPARK_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();
    private static final Identifier HEAL_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HEAL,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE
    ).id();


    private static final long ARCANE_COLOR = Color.from(SpellSchools.ARCANE.color).toRGBA();

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

    public static final Entry arcane_spec_a_passive_1 = add(arcane_spec_a_passive_1());
    private static Entry arcane_spec_a_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_passive_1");
        var title = "Volatile Magic";
        var description = "Arcane spell impacts have {trigger_chance}, to cause a small explosion, dealing {damage} damage.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = new Spell.Trigger();
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.school = "arcane";
        trigger.spell.type = Spell.Type.ACTIVE;
        trigger.chance = 0.2F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.impactDamage(0.4F, 0.2F);
        spell.impacts = List.of(impact);
        var area_impact = new Spell.AreaImpact();
        area_impact.skip_center_target = true;
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
                        "firework",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.25F, 0.25F)
                        .color(ARCANE_COLOR)
        };
        spell.area_impact = area_impact;

        SpellBuilder.configureCooldown(spell, 1F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_passive_1 = add(arcane_spec_b_passive_1());
    private static Entry arcane_spec_b_passive_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_passive_1");
        var title = "Evocation Radiance";
        var description = "Arcane spell impacts have {trigger_chance}, to heal the you for {heal}.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = new Spell.Trigger();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.school = "arcane";
        trigger.spell.type = Spell.Type.ACTIVE;
        trigger.chance = 0.1F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.impactHeal(0.1F);
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

        SpellBuilder.configureCooldown(spell, 1F);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_a_modifier_2 = add(arcane_spec_a_modifier_2());
    private static Entry arcane_spec_a_modifier_2() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_modifier_2");
        var title = "Conjured Missile";
        var description = "Increases the projectiles shot by Arcane Missile by {extra_launch_count}.";

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_missile";

        modifier.projectile_launch = Spell.LaunchProperties.EMPTY();
        modifier.projectile_launch.extra_launch_count = 1;
        modifier.projectile_launch.extra_launch_delay = 2;

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

        var impact = SpellBuilder.impactEffectAdd(effect.id.toString(), 4, 0, 2);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.ARCANE));
    }

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
        modifier.replacing_area_impact = new Spell.AreaImpact();
        modifier.replacing_area_impact.radius = 2.5F * (1F + bonus);
        modifier.replacing_area_impact.area = new Spell.Target.Area();
        modifier.replacing_area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;

        var particleBatch = new ParticleBatch();
        particleBatch.particle_id = SpellEngineParticles.fire_explosion.id().toString();
        particleBatch.shape = ParticleBatch.Shape.SPHERE;
        particleBatch.origin = ParticleBatch.Origin.CENTER;
        particleBatch.count = 1;
        particleBatch.min_speed = 0;
        particleBatch.max_speed = 0;
        particleBatch.scale = 1F + bonus * 0.6F; // Scale up by bonus
        modifier.replacing_area_impact.particles = new ParticleBatch[]{ particleBatch };

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

    public static final Entry priest_spec_a_modifier_1 = add(priest_spec_a_modifier_1());
    private static Entry priest_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "priest_spec_a_modifier_1");
        var title = "Healing Focus";
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
        var title = "Shock Blast";
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

        var particleBatch = new ParticleBatch(
                HOLY_DECELERATE.toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                40, 0.5F, 0.5F
        ).color(Color.HOLY.toRGBA());

        modifier.replacing_area_impact.particles = new ParticleBatch[]{ particleBatch };

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.PRIEST));
    }


    public static final Color MIGHT_COLOR = Color.from(0xccffff);

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

        var impact = SpellBuilder.impactEffectSet(SkillEffects.DIVINE_STRENGTH.id.toString(), 8, 0);
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
        var impact = SpellBuilder.impactEffectCleanse();
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

    public static final Entry rogue_spec_a_modifier_1 = add(rogue_spec_a_modifier_1());
    private static Entry rogue_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "rogue_spec_a_modifier_1");
        var title = "Fleet Footed";
        var effect = SkillEffects.FLEET_FOOTED;
        var description = "Slice and Dice attacks increases Move Speed by {bonus}, stacking up to {effect_amplifier_cap}, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rogues:slice_and_dice";

        var impact = SpellBuilder.impactEffectAdd(effect.id.toString(), 5, 1, 4);
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

        var impact = SpellBuilder.impactDamage(0.5F, 0);

        var area_impact = new Spell.AreaImpact();
        area_impact.execute_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.skip_center_target = true;
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
}
