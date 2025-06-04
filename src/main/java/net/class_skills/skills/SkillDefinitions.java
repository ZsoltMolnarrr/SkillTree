package net.class_skills.skills;

import net.class_skills.ClassSkillsMod;
import net.class_skills.utils.TextUtil;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.common.IconType;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_engine.mixin.client.ItemStackTooltipAccessor;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class SkillDefinitions {
    public static final Identifier CATEGORY_ID = Identifier.of(ClassSkillsMod.NAMESPACE, "class_skills");
    public record Icon(IconType type, String value) {
        public static Icon texture(String texture) {
            return new Icon(IconType.TEXTURE, texture);
        }
        public static Icon item(String item) {
            return new Icon(IconType.ITEM, item);
        }
        public static Icon effect(String effect) {
            return new Icon(IconType.EFFECT, effect);
        }
        public static Icon spell(Identifier spellId) {
            return texture(spellId.getNamespace() + ":textures/spell/" + spellId.getPath() + ".png");
        }
    }
    public record EntityAttributeReward(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        public static EntityAttributeReward of(RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
            return new EntityAttributeReward(attribute, new EntityAttributeModifier(Identifier.of(ClassSkillsMod.NAMESPACE + ":attribute_reward"), value, operation));
        }
    }
    public record Entry(String id, String title, String description, Icon icon, List<SpellContainer> spellReward, EntityAttributeReward attributeReward) {
        public static Entry spell(String id, String title, String description, Icon icon, List<SpellContainer> spellReward) {
            return new Entry(id, title, description, icon, spellReward, null);
        }
        public static Entry attribute(String id, String title, String description, Icon icon,
                                      RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
            return attribute(id, title, description, icon, EntityAttributeReward.of(attribute, value, operation));
        }
        public static Entry attribute(String id, String title, String description, Icon icon, EntityAttributeReward attributeReward) {
            if (description == null) {
                var lines = attributeModifierText(attributeReward);
                description = TextUtil.convert(lines);
            }
            return new Entry(id, title, description, icon, null, attributeReward);
        }
        public String titleTranslationKey() {
            return "skill." + ClassSkillsMod.NAMESPACE + "." + id + ".title";
        }
        public String descriptionTranslationKey() {
            return "skill." + ClassSkillsMod.NAMESPACE + "." + id + ".description";
        }
    }
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }


    public static final float ROOT_MULTIPLIER = 0.01f;
    public static final float BOOST_MULTIPLIER = 0.01f;

    private static List<Text> attributeModifierText(EntityAttributeReward reward) {
        List<Text> lines = new ArrayList<>();
        var tooltipUtil = (ItemStackTooltipAccessor) (Object) ItemStack.EMPTY;
        tooltipUtil.spellEngine_appendAttributeModifierTooltip(
                lines::add,
                null,
                reward.attribute(),
                reward.modifier()
        );
        return lines;
    }

    public static final Entry FIRE_ROOT = add(
            Entry.attribute("fire_root",
                    "Path of Fire",
                    null,
                    Icon.spell(Identifier.of("wizards", "fire_scorch")),
                    SpellSchools.FIRE.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry FIRE_BOOST = add(
            Entry.attribute("fire_boost",
                    "Fire Boost",
                    null,
                    FIRE_ROOT.icon(),
                    FIRE_ROOT.attributeReward())
    );

    public static final Entry FIREBALL = add(
            Entry.spell("fireball",
                    "Fireball",
                    "Unlock Fireball",
                    Icon.spell(Identifier.of("wizards", "fireball")),
                    List.of(SpellContainerHelper.createForSpellHost(Identifier.of("wizards:fireball")))
            )
    );
}
