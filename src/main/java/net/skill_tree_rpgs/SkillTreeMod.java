package net.skill_tree_rpgs;

import net.skill_tree_rpgs.items.SkillItems;
import net.skill_tree_rpgs.node.SpellContainerReward;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.fabricmc.api.ModInitializer;
import net.spell_engine.api.config.ConfigFile;
import net.tinyconfig.ConfigManager;

public class SkillTreeMod implements ModInitializer {
    public static final String NAMESPACE = "skill_tree_rpgs";
    private static final String DIRECTORY = NAMESPACE;
    public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(DIRECTORY)
            .sanitize(true)
            .build();

    @Override
    public void onInitialize() {
        effectConfig.refresh();

        SpellContainerReward.register();
        SkillItems.register();
        SkillEffects.register(effectConfig.value);
    }
}
