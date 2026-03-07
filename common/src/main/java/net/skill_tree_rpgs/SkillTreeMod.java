package net.skill_tree_rpgs;

import net.skill_tree_rpgs.items.SkillItems;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.skill_tree_rpgs.node.SpellContainerReward;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.skill_tree_rpgs.skills.SkillSounds;
import net.spell_engine.api.config.ConfigFile;
import net.tiny_config.ConfigManager;

public class SkillTreeMod {
    public static final String NAMESPACE = "skill_tree_rpgs";
    private static final String DIRECTORY = NAMESPACE;
    public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(DIRECTORY)
            .sanitize(true)
            .build();

    public static void init() {
        effectConfig.refresh();
        SpellContainerReward.register();
        ConditionalAttributeReward.register();
        effectConfig.save();
    }

    public static void registerSounds() {
        SkillSounds.register();
    }

    public static void registerItems() {
        SkillItems.register();
    }

    public static void registerEffects() {
        SkillEffects.register(effectConfig.value);
    }
}
