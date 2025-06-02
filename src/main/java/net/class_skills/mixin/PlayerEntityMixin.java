package net.class_skills.mixin;

import net.class_skills.node.SkillNodeOwner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.container.SpellContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.LinkedHashMap;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements SkillNodeOwner {
    @Unique
    private final LinkedHashMap<Identifier, SpellContainer> skillNodeSpellContainers = new LinkedHashMap<>();
    @Override
    public LinkedHashMap<Identifier, SpellContainer> getSkillNodeSpellContainers() {
        return skillNodeSpellContainers;
    }
}
