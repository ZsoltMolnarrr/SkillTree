package net.skill_tree_rpgs.attributes;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ConditionalAttributeModifier(
        Identifier id,
        Holder<Attribute> attribute,
        AttributeModifier modifier,
        ModifierCondition condition
) {}
