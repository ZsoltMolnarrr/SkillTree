package net.skill_tree_rpgs.fabric;

import net.fabricmc.api.ModInitializer;
import net.skill_tree_rpgs.SkillTreeMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SkillTreeMod.init();
        SkillTreeMod.registerSounds();
        SkillTreeMod.registerItems();
        SkillTreeMod.registerEffects();
    }
}
