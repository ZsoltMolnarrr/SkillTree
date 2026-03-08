package net.skill_tree_rpgs.skills;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class WeaponSkillModifiers {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    // ===== ARCANE =====

    public static final Skills.Entry weapon_arcane_root = add(weapon_arcane_root());
    private static Skills.Entry weapon_arcane_root() {
        var id = Identifier.of(NAMESPACE, "weapon_arcane_root");
        var title = "Arcane Mastery";
        var description = "Arcane Blast deals {power_multiplier} increased damage.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.1F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_arcane_blast_modifier_1 = add(weapon_arcane_blast_modifier_1());
    private static Skills.Entry weapon_arcane_blast_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_arcane_blast_modifier_1");
        var title = "Conjured Arcane Charge";
        var description = "Increases the maximum number of Arcane Charges by {effect_amplifier_cap_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    public static final Skills.Entry weapon_arcane_blast_modifier_2 = add(weapon_arcane_blast_modifier_2());
    private static Skills.Entry weapon_arcane_blast_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_arcane_blast_modifier_2");
        var title = "Arcane Endurance";
        var description = "Increases the duration of Arcane Charges by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.ARCANE));
    }

    // ===== FIRE =====

    public static final Skills.Entry weapon_fire_root = add(weapon_fire_root());
    private static Skills.Entry weapon_fire_root() {
        var id = Identifier.of(NAMESPACE, "weapon_fire_root");
        var title = "Fire Mastery";
        var critChance = 0.04F;
        var description = "Pyroblast has {bonus} increased critical strike chance.";
        SpellTooltip.DescriptionMutator mutator = (args) ->
                args.description().replace("{bonus}", SpellTooltip.percent(critChance));
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_blast";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_chance_bonus = critChance;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_fire_blast_modifier_1 = add(weapon_fire_blast_modifier_1());
    private static Skills.Entry weapon_fire_blast_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_fire_blast_modifier_1");
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

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WEAPON));
    }

    public static final Skills.Entry weapon_fire_blast_modifier_2 = add(weapon_fire_blast_modifier_2());
    private static Skills.Entry weapon_fire_blast_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_fire_blast_modifier_2");
        var title = "Blast Punch";
        var description = "Increases the knockback of Pyroblast by {knockback_multiply_base}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var bonus = 0.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_blast";
        modifier.knockback_multiply_base = bonus;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
    }

    // ===== FROST =====

    public static final Skills.Entry weapon_frost_root = add(weapon_frost_root());
    private static Skills.Entry weapon_frost_root() {
        var id = Identifier.of(NAMESPACE, "weapon_frost_root");
        var title = "Frost Mastery";
        var critDamage = 0.08F;
        var description = "Frostbolt deals {bonus} increased critical strike damage.";
        SpellTooltip.DescriptionMutator mutator = (args) ->
                args.description().replace("{bonus}", SpellTooltip.percent(critDamage));
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_damage_bonus = critDamage;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_frostbolt_modifier_1 = add(weapon_frostbolt_modifier_1());
    private static Skills.Entry weapon_frostbolt_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_frostbolt_modifier_1");
        var title = "Frost Bounce";
        var description = "Frostbolt ricochets to {ricochet} additional target.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.projectile_perks = Spell.ProjectileData.Perks.EMPTY();
        modifier.projectile_perks.ricochet = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
    }

    public static final Skills.Entry weapon_frostbolt_modifier_2 = add(weapon_frostbolt_modifier_2());
    private static Skills.Entry weapon_frostbolt_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_frostbolt_modifier_2");
        var title = "Lingering Chill";
        var description = "Frostbolt slow effect lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
    }

    // ===== HOLY =====

    public static final Skills.Entry weapon_holy_root = add(weapon_holy_root());
    private static Skills.Entry weapon_holy_root() {
        var id = Identifier.of(NAMESPACE, "weapon_holy_root");
        var title = "Holy Swiftness";
        var description = "Reduces the cooldown of Holy Shock by {cooldown_duration_deduct} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_shock";
        modifier.cooldown_duration_deduct = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
    }

    public static final Skills.Entry weapon_holy_shock_modifier_1 = add(weapon_holy_shock_modifier_1());
    private static Skills.Entry weapon_holy_shock_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_holy_shock_modifier_1");
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

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }

    public static final Skills.Entry weapon_holy_shock_modifier_2 = add(weapon_holy_shock_modifier_2());
    private static Skills.Entry weapon_holy_shock_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_holy_shock_modifier_2");
        var title = "Holy Blast";
        var description = "Damaging with Holy Shock causes small explosion, hitting enemies within {impact_range} blocks radius.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;
        var radius = 2.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_shock";
        var area_impact = new Spell.AreaImpact();
        area_impact.triggering_action_type = Spell.Impact.Action.Type.DAMAGE;
        area_impact.radius = radius;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SkillsCommon.HOLY_DECELERATE.toString(),
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
        area_impact.sound = new Sound(SkillSounds.priest_holy_blast.id());
        modifier.replacing_area_impact = area_impact;

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PRIEST));
    }


    // ===== SWORD (Swift Strikes) =====

    public static final Skills.Entry weapon_sword_root = add(weapon_sword_root());
    private static Skills.Entry weapon_sword_root() {
        var id = Identifier.of(NAMESPACE, "weapon_sword_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Sword Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_swift_strikes_modifier_1 = add(weapon_swift_strikes_modifier_1());
    private static Skills.Entry weapon_swift_strikes_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_swift_strikes_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:swift_strikes";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Swift Strikes I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_swift_strikes_modifier_2 = add(weapon_swift_strikes_modifier_2());
    private static Skills.Entry weapon_swift_strikes_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_swift_strikes_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:swift_strikes";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Swift Strikes II", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_flurry_modifier_1 = add(weapon_flurry_modifier_1());
    private static Skills.Entry weapon_flurry_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_flurry_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:flurry";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Flurry I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_flurry_modifier_2 = add(weapon_flurry_modifier_2());
    private static Skills.Entry weapon_flurry_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_flurry_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:flurry";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Flurry II", "", null, Skills.Category.WEAPON);
    }

    // ===== MACE (Smash) =====

    public static final Skills.Entry weapon_mace_root = add(weapon_mace_root());
    private static Skills.Entry weapon_mace_root() {
        var id = Identifier.of(NAMESPACE, "weapon_mace_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Mace Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_smash_modifier_1 = add(weapon_smash_modifier_1());
    private static Skills.Entry weapon_smash_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_smash_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:smash";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Smash I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_smash_modifier_2 = add(weapon_smash_modifier_2());
    private static Skills.Entry weapon_smash_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_smash_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:smash";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Smash II", "", null, Skills.Category.WEAPON);
    }

    // ===== HAMMER (Ground Slam) =====

    public static final Skills.Entry weapon_hammer_root = add(weapon_hammer_root());
    private static Skills.Entry weapon_hammer_root() {
        var id = Identifier.of(NAMESPACE, "weapon_hammer_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Hammer Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_ground_slam_modifier_1 = add(weapon_ground_slam_modifier_1());
    private static Skills.Entry weapon_ground_slam_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_ground_slam_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:ground_slam";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Ground Slam I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_ground_slam_modifier_2 = add(weapon_ground_slam_modifier_2());
    private static Skills.Entry weapon_ground_slam_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_ground_slam_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:ground_slam";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Ground Slam II", "", null, Skills.Category.WEAPON);
    }

    // ===== DOUBLE AXE (Whirlwind) =====

    public static final Skills.Entry weapon_double_axe_root = add(weapon_double_axe_root());
    private static Skills.Entry weapon_double_axe_root() {
        var id = Identifier.of(NAMESPACE, "weapon_double_axe_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Double Axe Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_whirlwind_modifier_1 = add(weapon_whirlwind_modifier_1());
    private static Skills.Entry weapon_whirlwind_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_whirlwind_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:whirlwind";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Whirlwind I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_whirlwind_modifier_2 = add(weapon_whirlwind_modifier_2());
    private static Skills.Entry weapon_whirlwind_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_whirlwind_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:whirlwind";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Whirlwind II", "", null, Skills.Category.WEAPON);
    }

    // ===== SPEAR (Impale) =====

    public static final Skills.Entry weapon_spear_root = add(weapon_spear_root());
    private static Skills.Entry weapon_spear_root() {
        var id = Identifier.of(NAMESPACE, "weapon_spear_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Spear Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_impale_modifier_1 = add(weapon_impale_modifier_1());
    private static Skills.Entry weapon_impale_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_impale_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:impale";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Impale I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_impale_modifier_2 = add(weapon_impale_modifier_2());
    private static Skills.Entry weapon_impale_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_impale_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:impale";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Impale II", "", null, Skills.Category.WEAPON);
    }

    // ===== DAGGER (Fan of Knives) =====

    public static final Skills.Entry weapon_dagger_root = add(weapon_dagger_root());
    private static Skills.Entry weapon_dagger_root() {
        var id = Identifier.of(NAMESPACE, "weapon_dagger_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Dagger Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_fan_of_knives_modifier_1 = add(weapon_fan_of_knives_modifier_1());
    private static Skills.Entry weapon_fan_of_knives_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_fan_of_knives_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:fan_of_knives";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Fan of Knives I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_fan_of_knives_modifier_2 = add(weapon_fan_of_knives_modifier_2());
    private static Skills.Entry weapon_fan_of_knives_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_fan_of_knives_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:fan_of_knives";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Fan of Knives II", "", null, Skills.Category.WEAPON);
    }

    // ===== SICKLE (Swipe) =====

    public static final Skills.Entry weapon_sickle_root = add(weapon_sickle_root());
    private static Skills.Entry weapon_sickle_root() {
        var id = Identifier.of(NAMESPACE, "weapon_sickle_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Sickle Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_swipe_modifier_1 = add(weapon_swipe_modifier_1());
    private static Skills.Entry weapon_swipe_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_swipe_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:swipe";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Swipe I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_swipe_modifier_2 = add(weapon_swipe_modifier_2());
    private static Skills.Entry weapon_swipe_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_swipe_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:swipe";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Swipe II", "", null, Skills.Category.WEAPON);
    }

    // ===== GLAIVE (Thrust) =====

    public static final Skills.Entry weapon_glaive_root = add(weapon_glaive_root());
    private static Skills.Entry weapon_glaive_root() {
        var id = Identifier.of(NAMESPACE, "weapon_glaive_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Glaive Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_thrust_modifier_1 = add(weapon_thrust_modifier_1());
    private static Skills.Entry weapon_thrust_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_thrust_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:thrust";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Thrust I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_thrust_modifier_2 = add(weapon_thrust_modifier_2());
    private static Skills.Entry weapon_thrust_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_thrust_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:thrust";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Thrust II", "", null, Skills.Category.WEAPON);
    }

    // ===== BOW =====

    public static final Skills.Entry weapon_bow_root = add(weapon_bow_root());
    private static Skills.Entry weapon_bow_root() {
        var id = Identifier.of(NAMESPACE, "weapon_bow_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        return new Skills.Entry(id, spell, "Bow Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_bow_passive_1 = add(weapon_bow_passive_1());
    private static Skills.Entry weapon_bow_passive_1() {
        var id = Identifier.of(NAMESPACE, "weapon_bow_passive_1");
        var title = "Leeching Shot";
        var description = "Arrow hits have {trigger_chance} chance to heal you.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.heal(0.15F);
        impact.particles = SkillsCommon.leechImpactParticles();
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_bow_passive_2 = add(weapon_bow_passive_2());
    private static Skills.Entry weapon_bow_passive_2() {
        var id = Identifier.of(NAMESPACE, "weapon_bow_passive_2");
        var title = "Poison Arrow";
        var description = "Arrow hits have {trigger_chance} chance to apply Poison for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(StatusEffects.POISON.getIdAsString(), 4F, 0, 1);
        impact.particles = SkillsCommon.poisonImpactParticles();
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 6F);

        return new Skills.Entry(id, spell, title, description, null, Skills.Category.WEAPON);
    }

    // ===== CROSSBOW =====

    public static final Skills.Entry weapon_crossbow_root = add(weapon_crossbow_root());
    private static Skills.Entry weapon_crossbow_root() {
        var id = Identifier.of(NAMESPACE, "weapon_crossbow_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        return new Skills.Entry(id, spell, "Crossbow Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_crossbow_passive_1 = add(weapon_crossbow_passive_1());
    private static Skills.Entry weapon_crossbow_passive_1() {
        var id = Identifier.of(NAMESPACE, "weapon_crossbow_passive_1");
        var title = "Impact Bolt";
        var description = "Crossbow bolts have {trigger_chance} chance to slow the target for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.chance = 0.3F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(StatusEffects.SLOWNESS.getIdAsString(), 3F, 1);
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 8F);

        return new Skills.Entry(id, spell, title, description, null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_crossbow_passive_2 = add(weapon_crossbow_passive_2());
    private static Skills.Entry weapon_crossbow_passive_2() {
        var id = Identifier.of(NAMESPACE, "weapon_crossbow_passive_2");
        var title = "Disarming Bolt";
        var description = "Crossbow bolts have {trigger_chance} chance to apply Weakness to the target for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.arrowHit();
        trigger.chance = 0.2F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(StatusEffects.WEAKNESS.getIdAsString(), 4F, 0);
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, Skills.Category.WEAPON);
    }

    // ===== AXE (Cleave) =====

    public static final Skills.Entry weapon_axe_root = add(weapon_axe_root());
    private static Skills.Entry weapon_axe_root() {
        var id = Identifier.of(NAMESPACE, "weapon_axe_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Axe Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_cleave_modifier_1 = add(weapon_cleave_modifier_1());
    private static Skills.Entry weapon_cleave_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_cleave_modifier_1");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:cleave";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Cleave I", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_cleave_modifier_2 = add(weapon_cleave_modifier_2());
    private static Skills.Entry weapon_cleave_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_cleave_modifier_2");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "rpg_series:cleave";
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Cleave II", "", null, Skills.Category.WEAPON);
    }
}
