package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;

import java.util.ArrayList;
import java.util.List;

public class WeaponSkillModifiers {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
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

    // ===== CLAYMORE (Flurry) =====

    public static final Skills.Entry weapon_claymore_root = add(weapon_claymore_root());
    private static Skills.Entry weapon_claymore_root() {
        var id = Identifier.of(NAMESPACE, "weapon_claymore_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "Claymore Specialisation", "", null, Skills.Category.WEAPON);
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
