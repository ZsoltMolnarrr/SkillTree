package net.skill_tree_rpgs.fabric.data_gen;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.items.SkillItems;
import net.skill_tree_rpgs.attributes.ModifierConditions;
import net.skill_tree_rpgs.node.SpellContainerReward;
import net.skill_tree_rpgs.skills.NodeTypes;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.skill_tree_rpgs.skills.SkillSounds;
import net.skill_tree_rpgs.skills.Skills;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.puffish.skillsmod.reward.builtin.AttributeReward;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.skill_tree_rpgs.utils.ResolvableTextContent;
import net.spell_engine.api.datagen.SimpleSoundGeneratorV2;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.client.gui.SpellTooltip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;

public class SkillTreeModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(LangGenerator::new);
        pack.addProvider(SoundGen::new);
        pack.addProvider(ModelProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(SpellsGen::new);
        pack.addProvider(SkillDefinitionGen::new);
    }

    public static class LangGenerator extends FabricLanguageProvider {
        // Fabric API 0.92: FabricLanguageProvider(FabricDataOutput) — no registry future, and
        // generateTranslations takes only the builder.
        protected LangGenerator(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            for (var item: SkillItems.ENTRIES) {
                translationBuilder.add(item.item(), item.title());
                for (var lore : item.loreTranslation()) {
                    translationBuilder.add(lore.translationKey(), lore.line().text());
                }
            }
            for (var skill: NodeTypes.allNodes()) {
                if (skill.title() != null && !skill.title().isEmpty()) {
                    translationBuilder.add(skill.titleTranslationKey(), skill.title());
                }
                // Spell-reward nodes resolve their description from the granted spell at runtime, so a
                // node-level description lang key would be a dead, untranslatable orphan — skip it.
                if (skill.spellReward() == null
                        && skill.description() != null && !skill.description().isEmpty()) {
                    translationBuilder.add(skill.descriptionTranslationKey(), skill.description());
                }
            }
            for (var entry : ModifierConditions.TRANSLATIONS.entrySet()) {
                translationBuilder.add(entry.getKey().translationKey(), entry.getValue());
            }
            for (var entry: Skills.ENTRIES) {
                translationBuilder.add(SpellTooltip.spellTranslationKey(entry.id()), entry.title());
                translationBuilder.add(SpellTooltip.spellDescriptionTranslationKey(entry.id()), entry.description());
            }
            SkillEffects.entries.forEach(entry -> {
                translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
                translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            });
        }
    }

    public static class SoundGen extends SimpleSoundGeneratorV2 {
        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSounds(Builder builder) {
            builder.entries.add(new Entry(SkillTreeMod.NAMESPACE,
                        SkillSounds.entries.stream()
                                    .map(entry -> SoundEntry.withVariants(entry.id().getPath(), entry.variants()))
                                    .toList()
                    )
            );
        }
    }

    public static class ModelProvider extends FabricModelProvider {
        public ModelProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            SkillItems.ENTRIES.forEach(entry -> {
                itemModelGenerator.register(entry.item(), Models.GENERATED);
            });
        }
    }

    public static class RecipeProvider extends FabricRecipeProvider {
        // Fabric API 0.92: FabricRecipeProvider(FabricDataOutput), generate(Consumer<RecipeJsonProvider>).
        public RecipeProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> recipeExporter) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, SkillItems.ORB_OF_OBLIVION.item())
                    .pattern(" X ")
                    .pattern("XCX")
                    .pattern(" X ")
                    .input('X', Items.EXPERIENCE_BOTTLE)
                    .input('C', Items.DIAMOND)
                    .criterion(FabricRecipeProvider.hasItem(Items.EXPERIENCE_BOTTLE), FabricRecipeProvider.conditionsFromItem(Items.EXPERIENCE_BOTTLE))
                    .offerTo(recipeExporter);
        }
    }

    public static class SkillDefinitionGen extends SkillDefinitionGenerator {
        public SkillDefinitionGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate(Builder builder) {
            LinkedHashMap<String, Format> skillDefinitions = new LinkedHashMap<>();
            for (var skill : NodeTypes.allNodes()) {
                Translatable title = null;
                if (skill.title() != null && !skill.title().isEmpty()) {
                    title = new Translatable(skill.titleTranslationKey());
                }
                Text description;
                if (skill.spellReward() != null) {
                    // Spell-reward nodes always resolve the granted spell's fully tokenized description
                    // through the runtime adapter (ResolvableTextContent -> TranslationUtil -> SpellTooltip).
                    // A node's own `description` string would route to a static lang key with no token
                    // substitution, so it is intentionally ignored here for spell rewards.
                    description = MutableText.of(new ResolvableTextContent(skill.id()));
                } else if (skill.description() != null && !skill.description().isEmpty()) {
                    description = Text.translatable(skill.descriptionTranslationKey());
                } else {
                    description = MutableText.of(new ResolvableTextContent(skill.id()));
                }

                Icon icon = null;
                switch (skill.icon().type()) {
                    case TEXTURE -> icon = Icon.texture(skill.icon().value());
                    case ITEM -> icon = skill.icon().modelId() != null
                            ? Icon.itemWithModel(skill.icon().value(), skill.icon().modelId())
                            : Icon.item(skill.icon().value());
                    case EFFECT -> icon = Icon.effect(skill.icon().value());
                }
                ArrayList<Reward> rewards = new ArrayList<>();
                if (skill.attributeReward() != null) {
                    var attribute = skill.attributeReward();
                    rewards.add(new Reward(AttributeReward.ID.toString(), RewardAttribute.from(attribute)));
                }
                if (skill.conditionalAttributeReward() != null) {
                    rewards.add(new Reward(ConditionalAttributeReward.ID.toString(), skill.conditionalAttributeReward()));
                }
                if(skill.spellReward() != null) {
                    rewards.add(new Reward(SpellContainerReward.ID.toString(), new SpellContainerReward.DataStructure(skill.spellReward())));
                }
                var format = new Format(title, description, icon, rewards, skill.required_mods());
                skillDefinitions.put(skill.id(), format);
            }
            builder.entries.add(new Entry(NodeTypes.CATEGORY_ID, skillDefinitions));
            builder.entries.add(new Entry(NodeTypes.WEAPON_CATEGORY_ID, skillDefinitions));
        }
    }

    public static class SpellsGen extends SpellGenerator {
        public SpellsGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry : Skills.ENTRIES) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }
}
