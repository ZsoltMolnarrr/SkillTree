package net.skill_tree_rpgs.attributes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public record ModifierCondition(Equipment equipment) {
    public record Equipment(EquipmentSlot slot, TagKey<Item> tag) {
        public boolean test(LivingEntity entity) {
            var stack = entity.getEquippedStack(slot);
            return !stack.isEmpty() && stack.isIn(tag);
        }
    }
}
