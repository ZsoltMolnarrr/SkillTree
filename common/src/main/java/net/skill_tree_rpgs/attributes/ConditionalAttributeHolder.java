package net.skill_tree_rpgs.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public interface ConditionalAttributeHolder {
    List<ConditionalAttributeModifier> getConditionalModifiers();

    default void addConditionalModifier(ConditionalAttributeModifier modifier) {
        getConditionalModifiers().add(modifier);
    }

    default void removeConditionalModifier(Identifier id) {
        getConditionalModifiers().removeIf(m -> m.id().equals(id));
    }

    default void reapplyConditionalModifiers(LivingEntity entity) {
        for (var conditional : getConditionalModifiers()) {
            var instance = entity.getAttributeInstance(conditional.attribute());
            if (instance == null) continue;
            boolean shouldApply = conditional.condition().equipment().test(entity);
            boolean isApplied = instance.getModifier(conditional.modifier().id()) != null;
            if (shouldApply && !isApplied) {
                instance.addTemporaryModifier(conditional.modifier());
            } else if (!shouldApply && isApplied) {
                instance.removeModifier(conditional.modifier().id());
            }
        }
    }
}
