package net.skill_tree_rpgs.neoforge;

import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.items.SkillItems;

@Mod(SkillTreeMod.NAMESPACE)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        SkillTreeMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Skill items into the vanilla Combat tab — NeoForge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
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
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            SkillTreeMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            SkillTreeMod.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            SkillTreeMod.registerEffects();
        });
    }
}
