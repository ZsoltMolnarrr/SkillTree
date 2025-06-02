package net.class_skills.node;

import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.internals.container.SpellContainerSource;

import java.util.ArrayList;
import java.util.List;

public class SkillNodeSource {
    private static SpellContainerSource.Entry SKILLS =  new SpellContainerSource.Entry("skills", new SpellContainerSource.Source() {
        @Override
        public List<SpellContainerSource.SourcedContainer> getSpellContainers(PlayerEntity playerEntity, String s) {
            var skillNodeOwner = (SkillNodeOwner) playerEntity;
            var containers = new ArrayList<SpellContainerSource.SourcedContainer>();
            for(var skillEntry: skillNodeOwner.getSkillNodeSpellContainers().entrySet()) {
                var container = skillEntry.getValue();
                containers.add(new SpellContainerSource.SourcedContainer(skillEntry.getKey().toString(), null, container));
            }
            return containers;
        }
    }, null);

    public static void markDirtyForPlayer(PlayerEntity player) {
        SpellContainerSource.setDirty(player, SKILLS);
    }

    public static void register() {
        SpellContainerSource.addSource(SKILLS);
    }
}
