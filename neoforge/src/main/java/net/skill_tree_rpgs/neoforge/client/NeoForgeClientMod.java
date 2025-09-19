package net.skill_tree_rpgs.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.client.SkillTreeClientMod;

@EventBusSubscriber(modid = SkillTreeMod.NAMESPACE, value = Dist.CLIENT)
public class NeoForgeClientMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SkillTreeClientMod.init();
    }
}