package net.skill_tree_rpgs.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.skill_tree_rpgs.client.SkillTreeClientMod;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkillTreeClientMod.init();
    }
}