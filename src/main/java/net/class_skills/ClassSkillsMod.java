package net.class_skills;

import net.class_skills.items.SkillItems;
import net.class_skills.node.SpellContainerReward;
import net.fabricmc.api.ModInitializer;

public class ClassSkillsMod implements ModInitializer {
    public static final String NAMESPACE = "class_skills";

    @Override
    public void onInitialize() {
        SpellContainerReward.register();
        SkillItems.register();
    }
}
