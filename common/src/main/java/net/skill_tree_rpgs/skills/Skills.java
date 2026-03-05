package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.SkillTreeMod;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.gui.SpellTooltip;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Skills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    public enum Category {
        ARCANE, FIRE, FROST, PRIEST, PALADIN, ROGUE, WARRIOR, ARCHER, WEAPON
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

    public static final List<Entry> ENTRIES = new ArrayList<>();
    static {
        ENTRIES.addAll(ArcherSkills.ENTRIES);
        ENTRIES.addAll(ArcaneSkills.ENTRIES);
        ENTRIES.addAll(FireSkills.ENTRIES);
        ENTRIES.addAll(FrostSkills.ENTRIES);
        ENTRIES.addAll(PriestSkills.ENTRIES);
        ENTRIES.addAll(PaladinSkills.ENTRIES);
        ENTRIES.addAll(RogueSkills.ENTRIES);
        ENTRIES.addAll(WarriorSkills.ENTRIES);
        ENTRIES.addAll(WeaponSkillModifiers.ENTRIES);
    }
}
