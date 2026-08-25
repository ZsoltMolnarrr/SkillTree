package net.skill_tree_rpgs.items;

import net.skill_tree_rpgs.SkillTreeMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SkillItems {
    public static class Container { Item item;}
    public record LoreLine(String text, Formatting formatting) {
        public record Translatable(String translationKey, LoreLine line) { }
    }
    public record Entry(Identifier id, String title, List<LoreLine> lore,
                        Function<Item.Settings, Item> factory, Item.Settings settings,
                        Container container) {
        public Entry(Identifier id, String title, List<LoreLine> lore, Item.Settings settings) {
            this(id, title, lore, Item::new, settings);
        }
        public Entry(Identifier id, String title, List<LoreLine> lore,
                     Function<Item.Settings, Item> factory, Item.Settings settings) {
            this(id, title, lore, factory, settings, new Container());
        }
        public Item item() {
            return container.item;
        }
        public List<LoreLine.Translatable> loreTranslation() {
            var keys = new ArrayList<LoreLine.Translatable>();
            int index = 0;
            for (var line : lore) {
                String key = "item." + id.getNamespace() + "." + id.getPath() + ".lore." + index++;
                keys.add(new LoreLine.Translatable(key, line));
            }
            return keys;
        }
    }
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final Entry ORB_OF_OBLIVION = add(
            new Entry(Identifier.of(SkillTreeMod.NAMESPACE, "orb_of_oblivion"),
                    "Orb of Oblivion",
                    List.of(
                            new LoreLine("Reset all skill points spend on the Class Skill Tree.", Formatting.GRAY)
                    ),
                    RespecItem::new,
                    new Item.Settings()
                            .rarity(Rarity.UNCOMMON)
                            .maxDamage(1)
                            // 1.21.2+: `Item#getBreakSound` is gone, the sound is a component
                            .component(DataComponentTypes.BREAK_SOUND, Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK))
            )
    );

    public static void register() {
        for (Entry entry : ENTRIES) {
            List<Text> lore = entry.loreTranslation().stream()
                    .map(line -> {
                        return (Text) Text.translatable(line.translationKey())
                                .formatted(line.line().formatting());
                    })
                    .toList();
            Item item = entry.factory().apply(entry.settings()
                    // 1.21.2+: item settings built in a factory must carry the registry key
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, entry.id()))
                    .component(DataComponentTypes.LORE, new LoreComponent(List.of(), lore) )
            );
            entry.container.item = item;
            Registry.register(Registries.ITEM, entry.id(), item);
        }
        // Creative-tab placement (vanilla Combat tab) is wired per-platform from each loader's entrypoint
        // (Fabric ItemGroupEvents / NeoForge BuildCreativeModeTabContentsEvent), iterating ENTRIES.
    }
}
