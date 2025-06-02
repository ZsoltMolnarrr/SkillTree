package net.class_skills.skills;

import net.class_skills.node.SpellContainerReward;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.ArrayList;

public class SkillDefinitions {
    public record Entry(String id, String title, String texture, SpellContainerReward spellReward, EntityAttributeModifier attributeReward) {
        public static Entry spell(String id, String title, String texture, SpellContainerReward spellReward) {
            return new Entry(id, title, texture, spellReward, null);
        }
        public static Entry attribute(String id, String title, String texture, EntityAttributeModifier attributeReward) {
            return new Entry(id, title, texture, null, attributeReward);
        }
    }
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }


}
