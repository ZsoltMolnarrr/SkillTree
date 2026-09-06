package net.skill_tree_rpgs.items;

import net.skill_tree_rpgs.SkillTreeMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
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
            new Entry(new Identifier(SkillTreeMod.NAMESPACE, "orb_of_oblivion"),
                    "Orb of Oblivion",
                    List.of(
                            new LoreLine("Reset all skill points spend on the Class Skill Tree.", Formatting.GRAY)
                    ),
                    RespecItem::new,
                    new Item.Settings()
                            .rarity(Rarity.UNCOMMON)
                            .maxDamage(1)
            )
    );

    /// 1.20.1 has no `minecraft:lore` data component (the 1.21 line attached the lore to
    /// `Item.Settings`), so the lines are held here and appended by the item's own `appendTooltip`
    /// (see {@link RespecItem}). An entry registered with a factory that does not append them —
    /// e.g. a plain `Item::new` — would simply render without lore.
    private static final Map<Item, List<Text>> LORE = new IdentityHashMap<>();

    public static void appendLore(Item item, List<Text> tooltip) {
        tooltip.addAll(LORE.getOrDefault(item, List.of()));
    }

    public static void register() {
        for (Entry entry : ENTRIES) {
            List<Text> lore = entry.loreTranslation().stream()
                    .map(line -> {
                        return (Text) Text.translatable(line.translationKey())
                                .formatted(line.line().formatting());
                    })
                    .toList();
            Item item = entry.factory().apply(entry.settings());
            LORE.put(item, lore);
            entry.container.item = item;
            Registry.register(Registries.ITEM, entry.id(), item);
        }
        // Creative-tab placement (vanilla Combat tab) is wired per-platform from each loader's entrypoint
        // (Fabric ItemGroupEvents / NeoForge BuildCreativeModeTabContentsEvent), iterating ENTRIES.
    }
}
