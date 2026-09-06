package net.skill_tree_rpgs.utils;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

/// 1.20.1 registry objects carry no id of their own (1.21's `getIdAsString()` does not exist),
/// so every "attribute id" / "effect id" string has to go back through the registry.
public final class RegistryIds {
    private RegistryIds() { }

    public static String attribute(EntityAttribute attribute) {
        return Registries.ATTRIBUTE.getId(attribute).toString();
    }

    public static String attribute(RegistryEntry<EntityAttribute> attribute) {
        return attribute(attribute.value());
    }

    public static String effect(StatusEffect effect) {
        return Registries.STATUS_EFFECT.getId(effect).toString();
    }
}
