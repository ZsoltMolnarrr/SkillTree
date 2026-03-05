package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.ExternalSpellSchools;

import java.util.ArrayList;
import java.util.List;

public class WeaponSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Skills.Entry weapon_sword_root = add(weapon_sword_root());
    private static Skills.Entry weapon_sword_root() {
        var id = Identifier.of(NAMESPACE, "weapon_sword_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_claymore_root = add(weapon_claymore_root());
    private static Skills.Entry weapon_claymore_root() {
        var id = Identifier.of(NAMESPACE, "weapon_claymore_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_mace_root = add(weapon_mace_root());
    private static Skills.Entry weapon_mace_root() {
        var id = Identifier.of(NAMESPACE, "weapon_mace_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_hammer_root = add(weapon_hammer_root());
    private static Skills.Entry weapon_hammer_root() {
        var id = Identifier.of(NAMESPACE, "weapon_hammer_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_double_axe_root = add(weapon_double_axe_root());
    private static Skills.Entry weapon_double_axe_root() {
        var id = Identifier.of(NAMESPACE, "weapon_double_axe_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_spear_root = add(weapon_spear_root());
    private static Skills.Entry weapon_spear_root() {
        var id = Identifier.of(NAMESPACE, "weapon_spear_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_dagger_root = add(weapon_dagger_root());
    private static Skills.Entry weapon_dagger_root() {
        var id = Identifier.of(NAMESPACE, "weapon_dagger_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_sickle_root = add(weapon_sickle_root());
    private static Skills.Entry weapon_sickle_root() {
        var id = Identifier.of(NAMESPACE, "weapon_sickle_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_glaive_root = add(weapon_glaive_root());
    private static Skills.Entry weapon_glaive_root() {
        var id = Identifier.of(NAMESPACE, "weapon_glaive_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }

    public static final Skills.Entry weapon_axe_root = add(weapon_axe_root());
    private static Skills.Entry weapon_axe_root() {
        var id = Identifier.of(NAMESPACE, "weapon_axe_root");
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        return new Skills.Entry(id, spell, "", "", null, Skills.Category.WEAPON);
    }
}
