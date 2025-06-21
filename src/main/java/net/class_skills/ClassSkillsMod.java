package net.class_skills;

import net.class_skills.items.SkillItems;
import net.class_skills.node.SpellContainerReward;
import net.class_skills.skills.SkillEffects;
import net.fabricmc.api.ModInitializer;
import net.spell_engine.api.config.ConfigFile;
import net.tinyconfig.ConfigManager;

public class ClassSkillsMod implements ModInitializer {
    public static final String NAMESPACE = "class_skills";
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
