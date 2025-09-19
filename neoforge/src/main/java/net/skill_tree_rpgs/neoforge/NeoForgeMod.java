package net.skill_tree_rpgs.neoforge;

import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.skill_tree_rpgs.SkillTreeMod;

@Mod(SkillTreeMod.NAMESPACE)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        SkillTreeMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
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
