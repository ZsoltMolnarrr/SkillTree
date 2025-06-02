package net.class_skills.node;

import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.internals.container.SpellContainerSource;

public class SkillNodeSource {
    public static void markDirtyForPlayer(PlayerEntity player) {
        SpellContainerSource.syncServerSideContainers(player);
    }

    public static void register() {
    }
}
