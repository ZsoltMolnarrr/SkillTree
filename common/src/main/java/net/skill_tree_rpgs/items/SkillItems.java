package net.skill_tree_rpgs.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.skill_tree_rpgs.SkillTreeMod;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SkillItems {
    public static class Container { Item item;}
    public record LoreLine(String text, ChatFormatting formatting) {
        public record Translatable(String translationKey, LoreLine line) { }
    }
    public record Entry(Identifier id, String title, List<LoreLine> lore,
                        Function<Item.Properties, Item> factory, Item.Properties settings,
                        Container container) {
        public Entry(Identifier id, String title, List<LoreLine> lore, Item.Properties settings) {
            this(id, title, lore, Item::new, settings);
        }
        public Entry(Identifier id, String title, List<LoreLine> lore,
                     Function<Item.Properties, Item> factory, Item.Properties settings) {
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
            new Entry(Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "orb_of_oblivion"),
                    "Orb of Oblivion",
                    List.of(
                            new LoreLine("Reset all skill points spend on the Class Skill Tree.", ChatFormatting.GRAY)
                    ),
                    RespecItem::new,
                    new Item.Properties()
                            .rarity(Rarity.UNCOMMON)
                            .durability(1)
                            // 1.21.2+: `Item#getBreakSound` is gone, the sound is a component
                            .component(DataComponents.BREAK_SOUND, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.AMETHYST_CLUSTER_BREAK))
            )
    );

    public static void register() {
        for (Entry entry : ENTRIES) {
            List<Component> lore = entry.loreTranslation().stream()
                    .map(line -> {
                        return (Component) Component.translatable(line.translationKey())
                                .withStyle(line.line().formatting());
                    })
                    .toList();
            Item item = entry.factory().apply(entry.settings()
                    // 1.21.2+: item settings built in a factory must carry the registry key
                    .setId(ResourceKey.create(Registries.ITEM, entry.id()))
                    .component(DataComponents.LORE, new ItemLore(List.of(), lore) )
            );
            entry.container.item = item;
            Registry.register(BuiltInRegistries.ITEM, entry.id(), item);
        }
        // Creative-tab placement (vanilla Combat tab) is wired per-platform from each loader's entrypoint
        // (Fabric ItemGroupEvents / NeoForge BuildCreativeModeTabContentsEvent), iterating ENTRIES.
    }
}
