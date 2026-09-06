package net.skill_tree_rpgs.fabric.data_gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.skills.NodeTypes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SkillDefinitionGenerator implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
    protected final FabricDataOutput dataOutput;

    public SkillDefinitionGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
    }

    public record Format(
            Translatable title,
            Text description,
            Icon icon,
            List<Reward> rewards,
            List<String> required_mods
    ) {}
    public record Translatable(String translate) { }
    public record Icon(
            String type,
            Object data
    ) {
        public static Icon texture(String texture) {
            return new Icon("texture", new IconTexture(texture));
        }
        public static Icon item(String item) {
            return new Icon("item", new IconItem(item, null));
        }
        /// Skills' `BuiltinJson.parseItemStack` reads `{"item": ..., "nbt": "<SNBT>"}` on 1.20.1
        /// (the 1.21 line used a `components` object). The SNBT is what SpellEngine's `SpellItemData`
        /// facade writes for a custom item model: `{spell_engine:{item_model:"<id>"}}`.
        public static Icon itemWithModel(String item, String modelId) {
            return new Icon("item", new IconItem(item,
                    "{spell_engine:{item_model:\"" + modelId + "\"}}"));
        }
        public static Icon effect(String effect) {
            return new Icon("effect", new IconEffect(effect));
        }
    }
    public record IconTexture(String texture) {}
    public record IconItem(String item, String nbt) {}
    public record IconEffect(String effect) {}

    public record Reward(
            String type,
            Object data
    ) {}

    public record RewardAttribute(
            String attribute,
            double value,
            String operation
    ) {
        /// The reward is keyed by attribute **id**, so the emitted JSON always names the attribute the
        /// node was authored against (`ranged_weapon:damage` for the archer root) even when that mod is
        /// absent from the datagen runtime. Skills drops a definition whose `required_mods` are unmet
        /// before it reports parse problems, so an unresolvable id is never logged in that case.
        public static RewardAttribute from(NodeTypes.EntityAttributeReward reward) {
            var modifier = reward.modifier();
            String operation;
            // 1.20.1 accessors: getOperation() / getValue() (1.21 used the record-style operation()/value()).
            switch (modifier.getOperation()) {
                case ADDITION -> operation = "addition";
                case MULTIPLY_BASE -> operation = "multiply_base";
                case MULTIPLY_TOTAL -> operation = "multiply_total";
                default -> throw new IllegalArgumentException("Unknown operation: " + modifier.getOperation());
            }
            return new RewardAttribute(reward.attributeId(), modifier.getValue(), operation);
        }
    }

    public record Entry(Identifier category, LinkedHashMap<String, Format> definitions) {  }
    public static class Builder {
        public final List<Entry> entries = new ArrayList<>();
    }

    public abstract void generate(Builder builder);

    private static final Gson gson = new GsonBuilder()
            // 1.20.1's Text.Serializer is registry-free; ResolvableTextContent is handled by
            // `net.skill_tree_rpgs.mixin.TextSerializerMixin`.
            .registerTypeHierarchyAdapter(Text.class, new Text.Serializer())
            .setPrettyPrinting()
            .create();

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        var builder = new Builder();
        generate(builder);
        var entries = builder.entries;

        List<CompletableFuture> writes = new ArrayList<>();
        for (var entry: entries) {
            var content = entry.definitions();
            var json = gson.toJsonTree(content);
            var path = getFilePath(entry.category());
            writes.add(DataProvider.writeToPath(writer, json, path));
        }

        return CompletableFuture.allOf(writes.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Skill Definition Generator";
    }

    private Path getFilePath(Identifier category) {
        return this.dataOutput.getResolver(DataOutput.OutputType.DATA_PACK, "puffish_skills/categories/" + category.getPath()).resolveJson(new Identifier(category.getNamespace(), "definitions"));
    }
}
