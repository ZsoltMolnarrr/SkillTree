package net.skill_tree_rpgs.forge.client;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.skill_tree_rpgs.client.SkillTreeClientMod;

/// Client-only wiring for Forge 47; only touched from {@link net.skill_tree_rpgs.forge.ForgeMod}
/// behind a `Dist.CLIENT` check. The mod-bus listener is registered explicitly (Forge 47's
/// `@EventBusSubscriber` scanning is avoided so the class is never loaded on a dedicated server).
public final class ForgeClientMod {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClientMod::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        SkillTreeClientMod.init();
    }
}
