package net.skill_tree_rpgs.fabric.data_gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SkillDefinitionGenerator implements DataProvider {
    private final CompletableFuture<HolderLookup.Provider> registryLookup;
    protected final FabricDataOutput dataOutput;

    public SkillDefinitionGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
    }

    public record Format(
            Translatable title,
            Component description,
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
        /// The vanilla `minecraft:item_model` component names an **item-model definition**
        /// (`assets/<ns>/items/<path>.json`), and SpellEngine resolves a spell book/scroll pool
        /// to its pool id verbatim — so `modelId` is the pool id (`wizards:spell_book/arcane`).
        /// (`spell_engine:item_model` is deprecated and no longer read.)
        public static Icon itemWithModel(String item, String modelId) {
            return new Icon("item", new IconItem(item, Map.of("minecraft:item_model", modelId)));
        }
        public static Icon effect(String effect) {
            return new Icon("effect", new IconEffect(effect));
        }
    }
    public record IconTexture(String texture) {}
    public record IconItem(String item, Map<String, Object> components) {}
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
        public static RewardAttribute from(Holder<Attribute> attribute, AttributeModifier modifier) {
            String operation;
            switch (modifier.operation()) {
                case ADD_VALUE -> operation = "addition";
                case ADD_MULTIPLIED_BASE -> operation = "multiply_base";
                case ADD_MULTIPLIED_TOTAL -> operation = "multiply_total";
                default -> throw new IllegalArgumentException("Unknown operation: " + modifier.operation());
            }
            var attributeId = attribute.unwrapKey().get().identifier().toString();
            return new RewardAttribute(attributeId, modifier.amount(), operation);
        }
    }

    public record Entry(Identifier category, LinkedHashMap<String, Format> definitions) {  }
    public static class Builder {
        public final List<Entry> entries = new ArrayList<>();
    }

    public abstract void generate(Builder builder);

    /// 1.21.11: `Text.Serializer` is gone — encode through `TextCodecs.CODEC` instead.
    /// The dispatch shape is unchanged (the `type` key is omitted on encode), so the emitted
    /// JSON matches what the 1.21.1 serializer produced.
    private static final JsonSerializer<Component> TEXT_SERIALIZER = (src, type, context) ->
            ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, src)
                    .getOrThrow(message -> new JsonParseException("Failed to encode text: " + message));

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Component.class, TEXT_SERIALIZER)
            .setPrettyPrinting()
            .create();

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        var builder = new Builder();
        generate(builder);
        var entries = builder.entries;

        List<CompletableFuture> writes = new ArrayList<>();
        for (var entry: entries) {
            var content = entry.definitions();
            var json = gson.toJsonTree(content);
            var path = getFilePath(entry.category());
            writes.add(DataProvider.saveStable(writer, json, path));
        }

        return CompletableFuture.allOf(writes.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Skill Definition Generator";
    }

    private Path getFilePath(Identifier category) {
        return this.dataOutput.createPathProvider(PackOutput.Target.DATA_PACK, "puffish_skills/categories/" + category.getPath()).json(Identifier.fromNamespaceAndPath(category.getNamespace(), "definitions"));
    }
}
