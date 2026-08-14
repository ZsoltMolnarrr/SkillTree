package net.skill_tree_rpgs.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.items.SkillItems;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        SkillTreeMod.init();
        SkillTreeMod.registerSounds();
        SkillTreeMod.registerItems();
        SkillTreeMod.registerEffects();

        // Skill items into the vanilla Combat tab — Fabric API.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            for (var entry : SkillItems.ENTRIES) {
                content.add(entry.item());
            }
        });
    }
}
