package net.class_skills.data_gen;

import net.class_skills.node.SpellContainerReward;
import net.class_skills.skills.SkillDefinitions;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.puffish.skillsmod.reward.builtin.AttributeReward;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

public class ClassSkillsModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SkillDefinitionGen::new);
    }

    public static class SkillDefinitionGen extends SkillDefinitionGenerator {
        public SkillDefinitionGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate(Builder builder) {
            LinkedHashMap<String, Format> skillDefinitions = new LinkedHashMap<>();
            for (var skill : SkillDefinitions.ENTRIES) {
                var icon = Icon.texture(skill.texture());
                ArrayList<Reward> rewards = new ArrayList<>();
                if (skill.attributeReward() != null) {
                    var attribute = skill.attributeReward();
                    rewards.add(new Reward(AttributeReward.ID.toString(), RewardAttribute.from(attribute)));
                }
                if(skill.spellReward() != null) {
                    rewards.add(new Reward(SpellContainerReward.ID.toString(), new SpellContainerReward.DataStructure(skill.spellReward())));
                }
                var format = new Format(skill.title(), icon, rewards);
                skillDefinitions.put(skill.id(), format);
            }
            builder.entries.add(new Entry(SkillDefinitions.CATEGORY_ID, skillDefinitions));
        }
    }
}
