package net.skill_tree_rpgs.attributes;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.skill_tree_rpgs.SkillTreeMod;

import java.util.Locale;

public record ModifierCondition(Equipment equipment, String translationKey) {
    public record Equipment(EquipmentSlot slot, TagKey<Item> tag) {
        public boolean test(LivingEntity entity) {
            var stack = entity.getItemBySlot(slot);
            return !stack.isEmpty() && stack.is(tag);
        }
    }
}
