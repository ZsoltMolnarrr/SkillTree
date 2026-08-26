package net.skill_tree_rpgs.skills;

import net.minecraft.resources.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.tooltip.TooltipTokens;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Skills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    public enum Category {
        ARCANE, FIRE, FROST, PRIEST, PALADIN, ROGUE, WARRIOR, ARCHER, WEAPON
    }

    /// A percentage baked directly into a description literal. The description is a lang value and
    /// `I18n.translate` feeds it to `String.format`, so a literal `%` must be doubled (`%%` → `%`) or
    /// it renders as "Format error". Token percentages injected after translation don't need this.
    public static String bakedPercent(float value) {
        return TooltipTokens.percent(value).replace("%", "%%");
    }

    public record Entry(Identifier id, Spell spell, String title, String description,
                        EnumSet<Category> categories) {
        public Entry(Identifier id, Spell spell, String title, String description, Category category) {
            this(id, spell, title, description, EnumSet.of(category));
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
