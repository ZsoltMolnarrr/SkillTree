package net.class_skills;

import net.class_skills.node.SkillNodeSource;
import net.class_skills.node.SpellContainerReward;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.SkillsAPI;
import net.spell_engine.internals.container.SpellContainerSource;

import java.util.ArrayList;
import java.util.List;

public class ClassSkillsMod implements ModInitializer {

    public static final String NAMESPACE = "class_skills";


    @Override
    public void onInitialize() {
        SpellContainerReward.register();
        SkillNodeSource.register();
    }
}
