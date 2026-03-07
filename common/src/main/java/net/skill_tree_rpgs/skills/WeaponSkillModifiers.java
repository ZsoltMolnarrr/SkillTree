package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
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
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;
        return new Skills.Entry(id, spell, "Arcane Staff Specialisation", "", null, Skills.Category.WEAPON);
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

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
    }

    public static final Skills.Entry weapon_arcane_missile_modifier_1 = add(weapon_arcane_missile_modifier_1());
    private static Skills.Entry weapon_arcane_missile_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_arcane_missile_modifier_1");
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

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
    }

    // ===== FIRE =====

    public static final Skills.Entry weapon_fire_root = add(weapon_fire_root());
    private static Skills.Entry weapon_fire_root() {
        var id = Identifier.of(NAMESPACE, "weapon_fire_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;
        return new Skills.Entry(id, spell, "Fire Staff Specialisation", "", null, Skills.Category.WEAPON);
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
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;
        return new Skills.Entry(id, spell, "Frost Staff Specialisation", "", null, Skills.Category.WEAPON);
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
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;
        return new Skills.Entry(id, spell, "Holy Staff Specialisation", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_holy_shock_modifier_1 = add(weapon_holy_shock_modifier_1());
    private static Skills.Entry weapon_holy_shock_modifier_1() {
        var id = Identifier.of(NAMESPACE, "weapon_holy_shock_modifier_1");
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

    public static final Skills.Entry weapon_holy_shock_modifier_2 = add(weapon_holy_shock_modifier_2());
    private static Skills.Entry weapon_holy_shock_modifier_2() {
        var id = Identifier.of(NAMESPACE, "weapon_holy_shock_modifier_2");
        var title = "Holy Empowerment";
        var description = "Holy Shock heals and damages for {power_multiplier} more.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var bonus = 0.2F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "paladins:holy_shock";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = bonus;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WEAPON));
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
