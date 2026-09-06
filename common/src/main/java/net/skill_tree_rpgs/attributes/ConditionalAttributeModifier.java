package net.skill_tree_rpgs.attributes;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

/// 1.20.1 keeps `EntityAttribute` as a plain object (no `RegistryEntry` in the attribute-instance
/// APIs), so the resolved attribute is stored raw.
public record ConditionalAttributeModifier(
        Identifier id,
        EntityAttribute attribute,
        EntityAttributeModifier modifier,
        ModifierCondition condition
) {}
