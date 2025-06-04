package net.class_skills.skills;

import net.class_skills.ClassSkillsMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class SkillDefinitions {
    public static final Identifier CATEGORY_ID = Identifier.of(ClassSkillsMod.NAMESPACE, "class_skills");
    public record Entry(String id, String title, String texture, List<SpellContainer> spellReward, EntityAttributeModifier attributeReward) {
        public static Entry spell(String id, String title, String texture, List<SpellContainer> spellReward) {
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

    public static final Entry FIRE_BOOST = add(
            Entry.attribute("fire_boost", "Fire Boost", "wizards:textures/spell/fire_scorch.png",
                    new EntityAttributeModifier(SpellSchools.FIRE.id,
                            1,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )
    );

    public static final Entry FIREBALL = add(
            Entry.spell("fireball", "Fireball", "wizards:textures/spell/fireball.png",
                    List.of(SpellContainerHelper.createForSpellHost(Identifier.of("wizards:fireball")))
            )
    );
}
