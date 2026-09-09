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
import net.skill_tree_rpgs.effect.SkillEffects;
import net.skill_tree_rpgs.forge.client.ForgeClientMod;
import net.skill_tree_rpgs.items.SkillItems;
import net.skill_tree_rpgs.skills.SkillSounds;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.fx.SpellEngineSounds;

/// Forge 47 entrypoint (1.20.1 port of the NeoForge entrypoint).
///
/// Forge locks every vanilla registry outside its own `RegisterEvent` window, so each block below sits
/// inside the window of the registry it writes to. Pufferfish's Skills reward types are registered from
/// `SkillTreeMod.init()` through the loader-neutral `SkillsAPI.registerReward`, which is not a Minecraft
/// registry and therefore needs no window.
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

    /// One listener for every registry; `RegisterEvent#register(key, consumer)` only runs the consumer when
    /// the event is for that key, so each block below executes inside exactly its own registry's window.
    ///
    /// The loops are **duplicated here on purpose** rather than delegated to `common`'s `registerX()`
    /// methods: a plain `Registry.register` is not usable on this loader, because Forge only clears the
    /// vanilla registry's own lock from 47.4.0 onwards — on 47.0–47.3 and NeoForge 1.20.1 it throws
    /// "Can not register to a locked registry" even inside the correct `RegisterEvent` window, and our
    /// `mods.toml` declares `loaderVersion = "[47,)"`. The helper this event hands out is the API every
    /// build of `[47,)` sanctions, so Forge iterates the same content `common` exposes and registers it
    /// itself. `common` keeps its vanilla-shaped `registerX()` for Fabric, which is untouched.
    ///
    /// `event.register` has no `else` and no throw, so content filed under a key that does not match the
    /// event vanishes silently — the grouping below is deliberate. Skill Tree adds no item group of its
    /// own (its items go into the vanilla Combat tab via `BuildCreativeModeTabContentsEvent`), so there is
    /// no `creative_mode_tab` block.
    public static void register(RegisterEvent event) {
        // Sounds are registry event 1, so they are in place before the status-effect window (event 5)
        // installs the behaviours that read them.
        event.register(RegistryKeys.SOUND_EVENT, helper -> {
            SpellEngineSounds.soundsToRegister(SkillSounds.entries).forEach(helper::register);
            // The helper returns void where `Registry.registerReference` returned the entry.
            SpellEngineSounds.linkEntries(SkillSounds.entries);
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            SkillEffects.configureEffects();
            Effects.effectsToRegister(SkillEffects.entries, SkillTreeMod.effectConfig.value.effects)
                    .forEach(helper::register);
            // `Effects.Entry#entry` is null until this runs, and `installBehaviours` reads four of them
            // (Protection.register ×2, InstantCast.register ×2) — link first, wire second.
            Effects.linkEntries(SkillEffects.entries);
            SkillEffects.installBehaviours();
        });

        // `Item`'s constructor takes an intrusive registry holder on 1.20.1, so the items are built here,
        // inside the ITEM window, not earlier.
        event.register(RegistryKeys.ITEM, helper ->
                SkillItems.itemsToRegister().forEach(helper::register));
    }
}
