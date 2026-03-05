package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.fx.SpellEngineSounds;

import java.util.ArrayList;
import java.util.List;

public class SkillSounds {
    public static final List<SpellEngineSounds.Entry> entries = new ArrayList<>();
    private static SpellEngineSounds.Entry add(SpellEngineSounds.Entry entry) {
        entries.add(entry);
        return entry;
    }
    private static SpellEngineSounds.Entry entry(String name) {
        return new SpellEngineSounds.Entry(Identifier.of(SkillTreeMod.NAMESPACE, name));
    }

    public static final SpellEngineSounds.Entry arcane_trap_activate = add(entry("arcane_trap_activate"));
    public static final SpellEngineSounds.Entry arcane_ward_activate = add(entry("arcane_ward_activate"));
    public static final SpellEngineSounds.Entry arcane_radiance = add(entry("arcane_radiance"));
    public static final SpellEngineSounds.Entry arcane_fissile_impact = add(entry("arcane_fissile_impact"));
    public static final SpellEngineSounds.Entry arcane_phase_shift = add(entry("arcane_phase_shift"));
    public static final SpellEngineSounds.Entry fire_trap_activate = add(entry("fire_trap_activate"));
    public static final SpellEngineSounds.Entry fire_ward_activate = add(entry("fire_ward_activate"));
    public static final SpellEngineSounds.Entry frost_trap_activate = add(entry("frost_trap_activate"));
    public static final SpellEngineSounds.Entry frost_ward_activate = add(entry("frost_ward_activate"));
    public static final SpellEngineSounds.Entry frost_winters_chill = add(entry("frost_winters_chill"));
    public static final SpellEngineSounds.Entry frost_cold_snap = add(entry("frost_cold_snap"));
    public static final SpellEngineSounds.Entry priest_holy_blast = add(entry("priest_holy_blast"));
    public static final SpellEngineSounds.Entry priest_healing_focus = add(entry("priest_healing_focus"));
    public static final SpellEngineSounds.Entry priest_incanter_cadence = add(entry("priest_incanter_cadence"));
    public static final SpellEngineSounds.Entry priest_fade = add(entry("priest_fade"));
    public static final SpellEngineSounds.Entry priest_pain_suppression = add(entry("priest_pain_suppression"));
    public static final SpellEngineSounds.Entry priest_consecration_impact = add(entry("priest_consecration_impact"));
    public static final SpellEngineSounds.Entry priest_orbs_activate = add(entry("priest_orbs_activate"));
    public static final SpellEngineSounds.Entry paladin_seal_impact = add(entry("paladin_seal_impact"));
    public static final SpellEngineSounds.Entry paladin_redoubt = add(entry("paladin_redoubt"));
    public static final SpellEngineSounds.Entry paladin_crusader_activate = add(entry("paladin_crusader_activate"));
    public static final SpellEngineSounds.Entry paladin_crusader_impact = add(entry("paladin_crusader_impact"));
    public static final SpellEngineSounds.Entry paladin_divine_hammer_impact = add(entry("paladin_divine_hammer_impact"));
    public static final SpellEngineSounds.Entry paladin_ardent_defender = add(entry("paladin_ardent_defender"));
    public static final SpellEngineSounds.Entry rogue_shadows_activate = add(entry("rogue_shadows_activate"));
    public static final SpellEngineSounds.Entry rogue_shadows_impact = add(entry("rogue_shadows_impact"));
    public static final SpellEngineSounds.Entry rogue_fracture_impact = add(entry("rogue_fracture_impact"));
    public static final SpellEngineSounds.Entry rogue_sidestep_activate = add(entry("rogue_sidestep_activate"));
    public static final SpellEngineSounds.Entry rogue_cheat_death = add(entry("rogue_cheat_death"));
    public static final SpellEngineSounds.Entry archer_rhythm_activate = add(entry("archer_rhythm_activate"));
    public static final SpellEngineSounds.Entry archer_maneuver_activate = add(entry("archer_maneuver_activate"));
    public static final SpellEngineSounds.Entry archer_supercharge_activate = add(entry("archer_supercharge_activate"));
    public static final SpellEngineSounds.Entry archer_supercharge_release = add(entry("archer_supercharge_release"));
    public static final SpellEngineSounds.Entry archer_deflection_activate = add(entry("archer_deflection_activate"));
    public static final SpellEngineSounds.Entry archer_deflection_impact = add(entry("archer_deflection_impact"));
    public static final SpellEngineSounds.Entry warrior_stomp = add(entry("warrior_stomp").variants(3));
    public static final SpellEngineSounds.Entry warrior_enrage = add(entry("warrior_enrage"));
    public static final SpellEngineSounds.Entry warrior_shockwave = add(entry("warrior_shockwave"));

    public static void register() {
        for (var entry: entries) {
            entry.register();
        }
    }
}
