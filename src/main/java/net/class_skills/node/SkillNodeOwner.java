package net.class_skills.node;

import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.container.SpellContainer;

import java.util.LinkedHashMap;

public interface SkillNodeOwner {
    LinkedHashMap<Identifier, SpellContainer> getSkillNodeSpellContainers();
}
