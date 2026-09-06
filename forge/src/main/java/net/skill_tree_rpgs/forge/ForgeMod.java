package net.skill_tree_rpgs.forge;

import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.forge.client.ForgeClientMod;
import net.skill_tree_rpgs.items.SkillItems;

/// Forge 47 entrypoint (1.20.1 port of the NeoForge entrypoint).
///
/// Forge locks every vanilla registry outside its own `RegisterEvent` window, so each `registerX()`
/// call sits inside the window of the registry it writes to. Pufferfish's Skills reward types are
/// registered from `SkillTreeMod.init()` through the loader-neutral `SkillsAPI.registerReward`, which
/// is not a Minecraft registry and therefore needs no window.
@Mod(SkillTreeMod.NAMESPACE)
public final class ForgeMod {
    // FMLJavaModLoadingContext.get() is flagged for removal by late 47.x builds, but the
    // constructor-injected replacement doesn't exist on early 47.x; get() works on all of [47,).
    @SuppressWarnings("removal")
    public ForgeMod() {
        // Run our common setup.
        SkillTreeMod.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Explicit event classes: Forge 47's plain addListener(Consumer) infers the event type from the
        // lambda via TypeTools, which is fragile; the 4-arg overload takes it directly.
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        // Skill items into the vanilla Combat tab — Forge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class,
                ForgeMod::buildTabContents);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(ItemGroups.COMBAT)) {
            return;
        }
        for (var entry : SkillItems.ENTRIES) {
            event.add(entry.item());
        }
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> SkillTreeMod.registerSounds());
        event.register(RegistryKeys.ITEM, reg -> SkillTreeMod.registerItems());
        event.register(RegistryKeys.STATUS_EFFECT, reg -> SkillTreeMod.registerEffects());
    }
}
