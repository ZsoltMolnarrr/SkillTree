package net.class_skills.utils;

import net.class_skills.skills.SkillDefinitions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.puffish.skillsmod.api.SkillsAPI;

public class SkillHelper {
    public static boolean respec(ServerPlayerEntity player) {
        var category = SkillsAPI.getCategory(SkillDefinitions.CATEGORY_ID);
        if (category.isEmpty()) {
            return false; // Category not found
        }
        var skillCategory = category.get();
        if (skillCategory.getSpentPoints(player) == 0) {
            return false; // No points to respec
        }
        skillCategory.resetSkills(player);
        return true;
    }
}
