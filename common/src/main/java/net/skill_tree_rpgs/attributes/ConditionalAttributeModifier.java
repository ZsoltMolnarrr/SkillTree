package net.skill_tree_rpgs.attributes;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public record ConditionalAttributeModifier(
        Identifier id,
        RegistryEntry<EntityAttribute> attribute,
        EntityAttributeModifier modifier,
        ModifierCondition condition
) {}
