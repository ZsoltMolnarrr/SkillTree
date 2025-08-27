package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.fx.SpellEngineSounds;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeSounds  {
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
    public static final SpellEngineSounds.Entry fire_trap_activate = add(entry("fire_trap_activate"));
    public static final SpellEngineSounds.Entry fire_ward_activate = add(entry("fire_ward_activate"));
    public static final SpellEngineSounds.Entry frost_trap_activate = add(entry("frost_trap_activate"));
    public static final SpellEngineSounds.Entry frost_ward_activate = add(entry("frost_ward_activate"));


    public static void register() {
        for (var entry: entries) {
            entry.register();
        }
    }
}
