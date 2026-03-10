package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.attributes.ModifierCondition;
import net.skill_tree_rpgs.attributes.ModifierConditions;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.common.IconType;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.datagen.WeaponSkills;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class NodeTypes {
    public static final Identifier CATEGORY_ID = Identifier.of(SkillTreeMod.NAMESPACE, "class_skills");
    public static final Identifier WEAPON_CATEGORY_ID = Identifier.of(SkillTreeMod.NAMESPACE, "weapon_skills");
    public record Icon(IconType type, String value, String modelId) {
        public static Icon texture(String texture) {
            return new Icon(IconType.TEXTURE, texture, null);
        }
        public static Icon item(String item) {
            return new Icon(IconType.ITEM, item, null);
        }
        public static Icon itemWithModel(String item, String modelId) {
            return new Icon(IconType.ITEM, item, modelId);
        }
        public static Icon effect(String effect) {
            return new Icon(IconType.EFFECT, effect, null);
        }
        public static Icon spell(Identifier spellId) {
            return texture(spellId.getNamespace() + ":textures/spell/" + spellId.getPath() + ".png");
        }
    }
    public record EntityAttributeReward(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        public static EntityAttributeReward of(RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
            return new EntityAttributeReward(attribute, new EntityAttributeModifier(Identifier.of(SkillTreeMod.NAMESPACE + ":attribute_reward"), value, operation));
        }
    }
    public record Entry(String id, String title, String description, Icon icon,
                        List<SpellContainer> spellReward,
                        EntityAttributeReward attributeReward,
                        ConditionalAttributeReward.DataStructure conditionalAttributeReward,
                        List<String> required_mods) {
        public static Entry spell(String id, String title, String description, Icon icon, List<SpellContainer> spellReward) {
            return new Entry(id, title, description, icon, spellReward, null, null, null);
        }
        public static Entry attribute(String id, String title, String description, Icon icon,
                                      RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
            return attribute(id, title, description, icon, EntityAttributeReward.of(attribute, value, operation));
        }
        public static Entry attribute(String id, String title, String description, Icon icon, EntityAttributeReward attributeReward) {
            return new Entry(id, title, description, icon, null, attributeReward, null, null);
        }
        public static Entry conditionalAttribute(String id, String title, String description, Icon icon,
                                                 RegistryEntry<EntityAttribute> attribute, double value,
                                                 EntityAttributeModifier.Operation operation,
                                                 ModifierCondition condition) {
            return conditionalAttribute(id, title, description, icon,
                    attribute.getKey().orElseThrow().getValue().toString(), null, value, operation, condition);
        }
        public static Entry conditionalAttribute(String id, String title, String description, Icon icon,
                                                 RegistryEntry<EntityAttribute> attribute,
                                                 RegistryEntry<EntityAttribute> fallbackAttribute,
                                                 double value,
                                                 EntityAttributeModifier.Operation operation,
                                                 ModifierCondition condition) {
            return conditionalAttribute(id, title, description, icon,
                    attribute.getKey().orElseThrow().getValue().toString(),
                    fallbackAttribute.getKey().orElseThrow().getValue().toString(),
                    value, operation, condition);
        }
        public static Entry conditionalAttribute(String id, String title, String description, Icon icon,
                                                 String attribute, String fallbackAttribute,
                                                 double value,
                                                 EntityAttributeModifier.Operation operation,
                                                 ModifierCondition condition) {
            return new Entry(id, title, description, icon, null, null, toDataStructure(attribute, fallbackAttribute, value, operation, condition), null);
        }
        private static ConditionalAttributeReward.DataStructure toDataStructure(
                String attribute, String fallbackAttribute,
                double value, EntityAttributeModifier.Operation operation,
                ModifierCondition condition) {
            var operationStr = switch (operation) {
                case ADD_VALUE -> "addition";
                case ADD_MULTIPLIED_BASE -> "multiply_base";
                case ADD_MULTIPLIED_TOTAL -> "multiply_total";
            };
            return new ConditionalAttributeReward.DataStructure(
                    attribute, fallbackAttribute, value, operationStr,
                    new ConditionalAttributeReward.DataStructure.ConditionData(
                            condition.translationKey(),
                            new ConditionalAttributeReward.DataStructure.ConditionData.EquipmentData(
                                    condition.equipment().slot().getName(), condition.equipment().tag().id().toString())));
        }
        public String titleTranslationKey() {
            return "skill." + SkillTreeMod.NAMESPACE + "." + id + ".title";
        }
        public String descriptionTranslationKey() {
            return "skill." + SkillTreeMod.NAMESPACE + "." + id + ".description";
        }
        public Entry withIcon(Icon icon) {
            return new Entry(id, title, description, icon, spellReward, attributeReward, conditionalAttributeReward, required_mods);
        }
        public Entry withItemIcon(String itemId) {
            return withIcon(Icon.item(itemId));
        }
        public Entry withTitle(String title) {
            return new Entry(id, title, description, icon, spellReward, attributeReward, conditionalAttributeReward, required_mods);
        }
        public Entry require(String modId) {
            return new Entry(id, title, description, icon, spellReward, attributeReward, conditionalAttributeReward, List.of(modId));
        }
    }
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final String WIZARDS = "wizards";
    public static final String PALADINS = "paladins";
    public static final String ARCHERS = "archers";
    public static final String ROGUES = "rogues";


    public static final float BOOST_MULTIPLIER = 0.01f;

//    private static List<Text> attributeModifierText(EntityAttributeReward reward) {
//        List<Text> lines = new ArrayList<>();
//        return lines;
//    }

    private static List<SpellContainer> dummyContainer() {
        return List.of(SpellContainers.forModifier(Identifier.of("wizards:fireball")));
    }

    private static Entry modifierSpell(Skills.Entry entry) {
        var modifiedSpellId = Identifier.of(entry.spell().modifiers.getFirst().spell_pattern);
        return Entry.spell(entry.id().getPath(),
                entry.title(),
                null,
                Icon.spell(modifiedSpellId),
                List.of(SpellContainers.forModifier(entry.id()))
        );
    }

    private static Entry passiveSpell(Skills.Entry entry) {
        return Entry.spell(entry.id().getPath(),
                entry.title(),
                null,
                Icon.spell(entry.id()),
                List.of(SpellContainers.forModifier(entry.id()))
        );
    }

    public static final Entry ARCANE_ROOT = add(
            Entry.attribute("arcane_root",
                    "Path of Arcane",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "wizards:item/spell_book/arcane"),
                    SpellSchools.ARCANE.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(WIZARDS)
    );
    public static final Entry ARCANE_BOOST = add(
            Entry.attribute("arcane_boost",
                    "Arcane Attunement",
                    null,
                    Icon.item("wizards:wand_arcane"),
                    ARCANE_ROOT.attributeReward()).require(WIZARDS)
    );
    public static final Entry ARCANE_TIER_2_SPELL_1_MODIFIER_1 = add(modifierSpell(ArcaneSkills.arcane_tier_2_spell_1_modifier_1).require(WIZARDS));
    public static final Entry ARCANE_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcaneSkills.arcane_tier_2_spell_1_modifier_2).require(WIZARDS));
    public static final Entry ARCANE_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(ArcaneSkills.arcane_tier_3_spell_1_modifier_1).require(WIZARDS));
    public static final Entry ARCANE_TIER_3_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcaneSkills.arcane_tier_3_spell_1_modifier_2).require(WIZARDS));
    public static final Entry ARCANE_TIER_4_SPELL_1_MODIFIER_1 = add(passiveSpell(ArcaneSkills.arcane_tier_4_spell_1_modifier_1)
            .withIcon(Icon.spell(Identifier.of("wizards", "arcane_blink")))
            .require(WIZARDS)
    );
    public static final Entry ARCANE_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcaneSkills.arcane_tier_4_spell_1_modifier_2).require(WIZARDS));
    public static final Entry ARCANE_TIER_1_PASSIVE_1 = add(passiveSpell(ArcaneSkills.arcane_tier_1_passive_1).require(WIZARDS));
    public static final Entry ARCANE_TIER_1_PASSIVE_2 = add(passiveSpell(ArcaneSkills.arcane_tier_1_passive_2).require(WIZARDS));
    public static final Entry ARCANE_TIER_2_PASSIVE_1 = add(passiveSpell(ArcaneSkills.arcane_tier_2_passive_1).require(WIZARDS));
    public static final Entry ARCANE_TIER_2_PASSIVE_2 = add(passiveSpell(ArcaneSkills.arcane_tier_2_passive_2).require(WIZARDS));
    public static final Entry ARCANE_TIER_3_PASSIVE_1 = add(passiveSpell(ArcaneSkills.arcane_tier_3_passive_1).require(WIZARDS));
    public static final Entry ARCANE_TIER_3_PASSIVE_2 = add(passiveSpell(ArcaneSkills.arcane_tier_3_passive_2).require(WIZARDS));

    public static final Entry FIRE_ROOT = add(
            Entry.attribute("fire_root",
                    "Path of Fire",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "wizards:item/spell_book/fire"),
                    SpellSchools.FIRE.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(WIZARDS)
    );
    public static final Entry FIRE_BOOST = add(
            Entry.attribute("fire_boost",
                    "Fire Attunement",
                    null,
                    Icon.item("wizards:wand_fire"),
                    FIRE_ROOT.attributeReward()).require(WIZARDS)
    );
    public static final Entry FIRE_TIER_2_SPELL_1_MODIFIER_1 = add(passiveSpell(FireSkills.fire_tier_2_spell_1_modifier_1)
            .withIcon(Icon.spell(Identifier.of("wizards", "fire_breath"))).require(WIZARDS));
    public static final Entry FIRE_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(FireSkills.fire_tier_2_spell_1_modifier_2).require(WIZARDS));
    public static final Entry FIRE_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(FireSkills.fire_tier_3_spell_1_modifier_1).require(WIZARDS));
    public static final Entry FIRE_TIER_3_SPELL_1_MODIFIER_2 = add(passiveSpell(FireSkills.fire_tier_3_spell_1_modifier_2)
            .withIcon(Icon.spell(Identifier.of("wizards", "fire_meteor"))).require(WIZARDS));
    public static final Entry FIRE_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(FireSkills.fire_tier_4_spell_1_modifier_1).require(WIZARDS));
    public static final Entry FIRE_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(FireSkills.fire_tier_4_spell_1_modifier_2).require(WIZARDS));

    public static final Entry FIRE_TIER_1_PASSIVE_1 = add(passiveSpell(FireSkills.fire_tier_1_passive_1).require(WIZARDS));
    public static final Entry FIRE_TIER_1_PASSIVE_2 = add(passiveSpell(FireSkills.fire_tier_1_passive_2).require(WIZARDS));
    public static final Entry FIRE_TIER_2_PASSIVE_1 = add(passiveSpell(FireSkills.fire_tier_2_passive_1).require(WIZARDS));
    public static final Entry FIRE_TIER_2_PASSIVE_2 = add(passiveSpell(FireSkills.fire_tier_2_passive_2).require(WIZARDS));
    public static final Entry FIRE_TIER_3_PASSIVE_1 = add(passiveSpell(FireSkills.fire_tier_3_passive_1).require(WIZARDS));
    public static final Entry FIRE_TIER_3_PASSIVE_2 = add(passiveSpell(FireSkills.fire_tier_3_passive_2).require(WIZARDS));

    public static final Entry FROST_ROOT = add(
            Entry.attribute("frost_root",
                    "Path of Frost",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "wizards:item/spell_book/frost"),
                    SpellSchools.FROST.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(WIZARDS)
    );
    public static final Entry FROST_BOOST = add(
            Entry.attribute("frost_boost",
                    "Frost Attunement",
                    null,
                    Icon.item("wizards:wand_frost"),
                    FROST_ROOT.attributeReward()).require(WIZARDS)
    );
    public static final Entry FROST_TIER_2_SPELL_1_MODIFIER_1 = add(passiveSpell(FrostSkills.frost_tier_2_spell_1_modifier_1)
            .withIcon(Icon.spell(Identifier.of("wizards", "frost_nova")))
            .require(WIZARDS)
    );
    public static final Entry FROST_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(FrostSkills.frost_tier_2_spell_1_modifier_2).require(WIZARDS));
    public static final Entry FROST_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(FrostSkills.frost_tier_3_spell_1_modifier_1).require(WIZARDS));
    public static final Entry FROST_TIER_3_SPELL_1_MODIFIER_2 = add(modifierSpell(FrostSkills.frost_tier_3_spell_1_modifier_2).require(WIZARDS));
    public static final Entry FROST_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(FrostSkills.frost_tier_4_spell_1_modifier_1).require(WIZARDS));
    public static final Entry FROST_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(FrostSkills.frost_tier_4_spell_1_modifier_2).require(WIZARDS));

    public static final Entry FROST_TIER_1_PASSIVE_1 = add(passiveSpell(FrostSkills.frost_tier_1_passive_1).require(WIZARDS));
    public static final Entry FROST_TIER_1_PASSIVE_2 = add(passiveSpell(FrostSkills.frost_tier_1_passive_2).require(WIZARDS));
    public static final Entry FROST_TIER_2_PASSIVE_1 = add(passiveSpell(FrostSkills.frost_tier_2_passive_1).require(WIZARDS));
    public static final Entry FROST_TIER_2_PASSIVE_2 = add(passiveSpell(FrostSkills.frost_tier_2_passive_2).require(WIZARDS));
    public static final Entry FROST_TIER_3_PASSIVE_1 = add(passiveSpell(FrostSkills.frost_tier_3_passive_1).require(WIZARDS));
    public static final Entry FROST_TIER_3_PASSIVE_2 = add(passiveSpell(FrostSkills.frost_tier_3_passive_2).require(WIZARDS));

    public static final Entry PRIEST_ROOT = add(
            Entry.attribute("priest_root",
                    "Path of the Light",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "paladins:item/spell_book/priest"),
                    SpellSchools.HEALING.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(PALADINS)
    );
    public static final Entry PRIEST_BOOST = add(
            Entry.attribute("priest_boost",
                    "Holy Attunement",
                    null,
                    Icon.item("paladins:holy_wand"),
                    PRIEST_ROOT.attributeReward()).require(PALADINS)
    );
    public static final Entry PRIEST_TIER_2_SPELL_1_MODIFIER_1 = add(modifierSpell(PriestSkills.priest_tier_2_spell_1_modifier_1).require(PALADINS));
    public static final Entry PRIEST_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(PriestSkills.priest_tier_2_spell_1_modifier_2).require(PALADINS));
    public static final Entry PRIEST_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(PriestSkills.priest_tier_3_spell_1_modifier_1).require(PALADINS));
    public static final Entry PRIEST_TIER_3_SPELL_1_MODIFIER_2 = add(passiveSpell(PriestSkills.priest_tier_3_spell_1_modifier_2)
            .withIcon(Icon.spell(Identifier.of("paladins", "circle_of_healing")))
            .require(PALADINS)
    );
    public static final Entry PRIEST_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(PriestSkills.priest_tier_4_spell_1_modifier_1).require(PALADINS));
    public static final Entry PRIEST_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(PriestSkills.priest_tier_4_spell_1_modifier_2).require(PALADINS));

    public static final Entry PRIEST_TIER_1_PASSIVE_1 = add(passiveSpell(PriestSkills.priest_tier_1_passive_1).require(PALADINS));
    public static final Entry PRIEST_TIER_1_PASSIVE_2 = add(passiveSpell(PriestSkills.priest_tier_1_passive_2).require(PALADINS));
    public static final Entry PRIEST_TIER_2_PASSIVE_1 = add(passiveSpell(PriestSkills.priest_tier_2_passive_1).require(PALADINS));
    public static final Entry PRIEST_TIER_2_PASSIVE_2 = add(passiveSpell(PriestSkills.priest_tier_2_passive_2).require(PALADINS));
    public static final Entry PRIEST_TIER_3_PASSIVE_1 = add(passiveSpell(PriestSkills.priest_tier_3_passive_1).require(PALADINS));
    public static final Entry PRIEST_TIER_3_PASSIVE_2 = add(passiveSpell(PriestSkills.priest_tier_3_passive_2).require(PALADINS));

    public static final Entry PALADIN_ROOT = add(
            Entry.attribute("paladin_root",
                    "Path of the Paladin",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "paladins:item/spell_book/paladin"),
                    SpellSchools.HEALING.attributeEntry,
                    0.2,
                    EntityAttributeModifier.Operation.ADD_VALUE
            ).require(PALADINS)
    );
    public static final Entry PALADIN_BOOST = add(
            Entry.attribute("paladin_boost",
                    "Paladin Empowerment",
                    null,
                    Icon.item("paladins:iron_mace"),
                    PALADIN_ROOT.attributeReward()).require(PALADINS)
    );
    public static final Entry PALADIN_TIER_2_SPELL_1_MODIFIER_1 = add(modifierSpell(PaladinSkills.paladin_tier_2_spell_1_modifier_1).require(PALADINS));
    public static final Entry PALADIN_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(PaladinSkills.paladin_tier_2_spell_1_modifier_2).require(PALADINS));
    public static final Entry PALADIN_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(PaladinSkills.paladin_tier_3_spell_1_modifier_1).require(PALADINS));
    public static final Entry PALADIN_TIER_3_SPELL_1_MODIFIER_2 = add(modifierSpell(PaladinSkills.paladin_tier_3_spell_1_modifier_2).require(PALADINS));
    public static final Entry PALADIN_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(PaladinSkills.paladin_tier_4_spell_1_modifier_1).require(PALADINS));
    public static final Entry PALADIN_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(PaladinSkills.paladin_tier_4_spell_1_modifier_2).require(PALADINS));

    public static final Entry PALADIN_TIER_1_PASSIVE_1 = add(passiveSpell(PaladinSkills.paladin_tier_1_passive_1).require(PALADINS));
    public static final Entry PALADIN_TIER_1_PASSIVE_2 = add(passiveSpell(PaladinSkills.paladin_tier_1_passive_2).require(PALADINS));
    public static final Entry PALADIN_TIER_2_PASSIVE_1 = add(passiveSpell(PaladinSkills.paladin_tier_2_passive_1).require(PALADINS));
    public static final Entry PALADIN_TIER_2_PASSIVE_2 = add(passiveSpell(PaladinSkills.paladin_tier_2_passive_2).require(PALADINS));
    public static final Entry PALADIN_TIER_3_PASSIVE_1 = add(passiveSpell(PaladinSkills.paladin_tier_3_passive_1).require(PALADINS));
    public static final Entry PALADIN_TIER_3_PASSIVE_2 = add(passiveSpell(PaladinSkills.paladin_tier_3_passive_2).require(PALADINS));

    public static final Entry ARCHER_ROOT = add(
            Entry.attribute("archer_root",
                    "Path of the Archer",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "archers:item/spell_book/archer"),
                    EntityAttributes_RangedWeapon.DAMAGE.entry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(ARCHERS)
    );
    public static final Entry ARCHER_BOOST = add(
            Entry.attribute("archer_boost",
                    "Archer Empowerment",
                    null,
                    Icon.item("archers:composite_longbow"),
                    ARCHER_ROOT.attributeReward()).require(ARCHERS)
    );
    
    public static final Entry ARCHER_TIER_2_SPELL_1_MODIFIER_1 = add(modifierSpell(ArcherSkills.archer_tier_2_spell_1_modifier_1).require(ARCHERS));
    public static final Entry ARCHER_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcherSkills.archer_tier_2_spell_1_modifier_2).require(ARCHERS));
    public static final Entry ARCHER_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(ArcherSkills.archer_tier_3_spell_1_modifier_1).require(ARCHERS));
    public static final Entry ARCHER_TIER_3_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcherSkills.archer_tier_3_spell_1_modifier_2).require(ARCHERS));
    public static final Entry ARCHER_TIER_4_SPELL_1_MODIFIER_1 = add(passiveSpell(ArcherSkills.archer_tier_4_spell_1_modifier_1)
            .withIcon(Icon.spell(Identifier.of("archers", "magic_arrow"))).require(ARCHERS));
    public static final Entry ARCHER_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcherSkills.archer_tier_4_spell_1_modifier_2).require(ARCHERS));

    public static final Entry ARCHER_TIER_1_PASSIVE_1 = add(passiveSpell(ArcherSkills.archer_tier_1_passive_1).require(ARCHERS));
    public static final Entry ARCHER_TIER_1_PASSIVE_2 = add(passiveSpell(ArcherSkills.archer_tier_1_passive_2).require(ARCHERS));
    public static final Entry ARCHER_TIER_2_PASSIVE_1 = add(passiveSpell(ArcherSkills.archer_tier_2_passive_1).require(ARCHERS));
    public static final Entry ARCHER_TIER_2_PASSIVE_2 = add(passiveSpell(ArcherSkills.archer_tier_2_passive_2).require(ARCHERS));
    public static final Entry ARCHER_TIER_3_PASSIVE_1 = add(passiveSpell(ArcherSkills.archer_tier_3_passive_1).require(ARCHERS));
    public static final Entry ARCHER_TIER_3_PASSIVE_2 = add(passiveSpell(ArcherSkills.archer_tier_3_passive_2).require(ARCHERS));

    public static final Entry ROGUE_ROOT = add(
            Entry.attribute("rogue_root",
                    "Path of the Rogue",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "rogues:item/spell_book/rogue"),
                    EntityAttributes.GENERIC_ATTACK_SPEED,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(ROGUES)
    );
    public static final Entry ROGUE_BOOST = add(
            Entry.attribute("rogue_boost",
                    "Rogue Empowerment",
                    null,
                    Icon.item("rogues:iron_sickle"),
                    ROGUE_ROOT.attributeReward()).require(ROGUES)
    );
    public static final Entry ROGUE_TIER_2_SPELL_1_MODIFIER_1 = add(modifierSpell(RogueSkills.rogue_tier_2_spell_1_modifier_1).require(ROGUES));
    public static final Entry ROGUE_TIER_2_SPELL_1_MODIFIER_2 = add(passiveSpell(RogueSkills.rogue_tier_2_spell_1_modifier_2)
            .withIcon(Icon.spell(Identifier.of("rogues:shock_powder")))
            .require(ROGUES)
    );
    public static final Entry ROGUE_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(RogueSkills.rogue_tier_3_spell_1_modifier_1).require(ROGUES));
    public static final Entry ROGUE_TIER_3_SPELL_1_MODIFIER_2 = add(modifierSpell(RogueSkills.rogue_tier_3_spell_1_modifier_2).require(ROGUES));
    public static final Entry ROGUE_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(RogueSkills.rogue_tier_4_spell_1_modifier_1).require(ROGUES));
    public static final Entry ROGUE_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(RogueSkills.rogue_tier_4_spell_1_modifier_2).require(ROGUES));

    public static final Entry ROGUE_TIER_1_PASSIVE_1 = add(passiveSpell(RogueSkills.rogue_tier_1_passive_1).require(ROGUES));
    public static final Entry ROGUE_TIER_1_PASSIVE_2 = add(passiveSpell(RogueSkills.rogue_tier_1_passive_2).require(ROGUES));
    public static final Entry ROGUE_TIER_2_PASSIVE_1 = add(passiveSpell(RogueSkills.rogue_tier_2_passive_1).require(ROGUES));
    public static final Entry ROGUE_TIER_2_PASSIVE_2 = add(passiveSpell(RogueSkills.rogue_tier_2_passive_2).require(ROGUES));
    public static final Entry ROGUE_TIER_3_PASSIVE_1 = add(passiveSpell(RogueSkills.rogue_tier_3_passive_1).require(ROGUES));
    public static final Entry ROGUE_TIER_3_PASSIVE_2 = add(passiveSpell(RogueSkills.rogue_tier_3_passive_2).require(ROGUES));

    public static final Entry WARRIOR_ROOT = add(
            Entry.attribute("warrior_root",
                    "Path of the Warrior",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "rogues:item/spell_book/warrior"),
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(ROGUES)
    );
    public static final Entry WARRIOR_BOOST = add(
            Entry.attribute("warrior_boost",
                    "Warrior Empowerment",
                    null,
                    Icon.item("rogues:iron_double_axe"),
                    WARRIOR_ROOT.attributeReward()).require(ROGUES)
    );
    public static final Entry WARRIOR_TIER_2_SPELL_1_MODIFIER_1 = add(modifierSpell(WarriorSkills.warrior_tier_2_spell_1_modifier_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(WarriorSkills.warrior_tier_2_spell_1_modifier_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_SPELL_1_MODIFIER_1 = add(passiveSpell(WarriorSkills.warrior_tier_3_spell_1_modifier_1)
            .withIcon(Icon.spell(Identifier.of("rogues", "shout")))
            .require(ROGUES)
    );
    public static final Entry WARRIOR_TIER_3_SPELL_1_MODIFIER_2 = add(modifierSpell(WarriorSkills.warrior_tier_3_spell_1_modifier_2)
            .require(ROGUES)
    );
    public static final Entry WARRIOR_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(WarriorSkills.warrior_tier_4_spell_1_modifier_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_4_SPELL_1_MODIFIER_2 = add(passiveSpell(WarriorSkills.warrior_tier_4_spell_1_modifier_2)
            .withIcon(Icon.spell(Identifier.of("rogues", "charge")))
            .require(ROGUES)
    );

    public static final Entry WARRIOR_TIER_1_PASSIVE_1 = add(passiveSpell(WarriorSkills.warrior_tier_1_passive_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_1_PASSIVE_2 = add(passiveSpell(WarriorSkills.warrior_tier_1_passive_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_2_PASSIVE_1 = add(passiveSpell(WarriorSkills.warrior_tier_2_passive_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_2_PASSIVE_2 = add(passiveSpell(WarriorSkills.warrior_tier_2_passive_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_PASSIVE_1 = add(passiveSpell(WarriorSkills.warrior_tier_3_passive_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_PASSIVE_2 = add(passiveSpell(WarriorSkills.warrior_tier_3_passive_2).require(ROGUES));

    // ===== WEAPON SKILLS =====

    public static final float WEAPON_ROOT_DAMAGE = 0.05f;
    public static final float WEAPON_ROOT_CRIT_CHANCE = 0.04f;
    public static final float WEAPON_ROOT_CRIT_DAMAGE = 0.08f;
    public static final float WEAPON_ROOT_HASTE = 0.05f;

    public static final String CRIT_CHANCE_ID = "critical_strike:chance";
    public static final String CRIT_DAMAGE_ID = "critical_strike:damage";
    public static final String ATTACK_DAMAGE_ID = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();


    // Arcane Staff
    public static final Entry WEAPON_ARCANE_ROOT = add(modifierSpell(WeaponSkillModifiers.weapon_arcane_root)
            .withTitle("Arcane Staff Specialisation")
            .withItemIcon("wizards:staff_arcane")
            .require(WIZARDS)
    );
    public static final Entry WEAPON_ARCANE_BLAST_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_arcane_blast_modifier_1).require(WIZARDS));
    public static final Entry WEAPON_ARCANE_BLAST_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_arcane_blast_modifier_2).require(WIZARDS));

    // Fire Staff
    public static final Entry WEAPON_FIRE_ROOT = add(modifierSpell(WeaponSkillModifiers.weapon_fire_root)
            .withTitle("Fire Staff Specialisation")
            .withItemIcon("wizards:staff_fire")
            .require(WIZARDS)
    );
    public static final Entry WEAPON_FIRE_BLAST_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_fire_blast_modifier_1).require(WIZARDS));
    public static final Entry WEAPON_FIRE_BLAST_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_fire_blast_modifier_2).require(WIZARDS));

    // Frost Staff
    public static final Entry WEAPON_FROST_ROOT = add(modifierSpell(WeaponSkillModifiers.weapon_frost_root)
            .withTitle("Frost Staff Specialisation")
            .withItemIcon("wizards:staff_frost")
            .require(WIZARDS)
    );
    public static final Entry WEAPON_FROSTBOLT_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_frostbolt_modifier_1).require(WIZARDS));
    public static final Entry WEAPON_FROSTBOLT_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_frostbolt_modifier_2).require(WIZARDS));

    // Holy Staff
    public static final Entry WEAPON_HOLY_ROOT = add(modifierSpell(WeaponSkillModifiers.weapon_holy_root)
            .withTitle("Holy Staff Specialisation")
            .withItemIcon("paladins:holy_staff")
            .require(PALADINS));
    public static final Entry WEAPON_HOLY_SHOCK_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_holy_shock_modifier_1).require(PALADINS));
    public static final Entry WEAPON_HOLY_SHOCK_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_holy_shock_modifier_2).require(PALADINS));

    // Sword (Swift Strikes)
    public static final Entry WEAPON_SWORD_ROOT = add(
            Entry.conditionalAttribute("weapon_sword_root",
                    "Sword Specialisation",
                    null,
                    Icon.item("minecraft:iron_sword"),
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.SWORD
            )
    );
    public static final Entry WEAPON_SWIFT_STRIKES_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_swift_strikes_modifier_1));
    public static final Entry WEAPON_SWIFT_STRIKES_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_swift_strikes_modifier_2));

    // Claymore
    public static final Entry WEAPON_CLAYMORE_ROOT = add(
            Entry.conditionalAttribute("weapon_claymore_root",
                    "Claymore Specialisation",
                    null,
                    Icon.item("paladins:iron_claymore"),
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.CLAYMORE
            ).require(PALADINS)
    );
    public static final Entry WEAPON_FLURRY_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_flurry_modifier_1).require(PALADINS));
    public static final Entry WEAPON_FLURRY_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_flurry_modifier_2).require(PALADINS));

    // Mace (Smash)
    public static final Entry WEAPON_MACE_ROOT = add(
            Entry.conditionalAttribute("weapon_mace_root",
                    "Mace Specialisation",
                    null,
                    Icon.item("paladins:iron_mace"),
                    CRIT_DAMAGE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.MACE
            ).require(PALADINS)
    );
    public static final Entry WEAPON_SMASH_MODIFIER_1 = add(passiveSpell(WeaponSkillModifiers.weapon_smash_modifier_1)
            .withIcon(Icon.spell(WeaponSkills.SMASH.id()))
            .require(PALADINS));
    public static final Entry WEAPON_SMASH_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_smash_modifier_2).require(PALADINS));

    // Hammer (Ground Slam)
    public static final Entry WEAPON_HAMMER_ROOT = add(
            Entry.conditionalAttribute("weapon_hammer_root",
                    "Hammer Specialisation",
                    null,
                    Icon.item("paladins:iron_great_hammer"),
                    CRIT_DAMAGE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.HAMMER
            ).require(PALADINS)
    );
    public static final Entry WEAPON_GROUND_SLAM_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_ground_slam_modifier_1).require(PALADINS));
    public static final Entry WEAPON_GROUND_SLAM_MODIFIER_2 = add(passiveSpell(WeaponSkillModifiers.weapon_ground_slam_modifier_2)
            .withIcon(Icon.spell(WeaponSkills.GROUND_SLAM.id()))
            .require(PALADINS));

    // Double Axe (Whirlwind)
    public static final Entry WEAPON_DOUBLE_AXE_ROOT = add(
            Entry.conditionalAttribute("weapon_double_axe_root",
                    "Double Axe Specialisation",
                    null,
                    Icon.item("rogues:iron_double_axe"),
                    CRIT_CHANCE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_CHANCE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.DOUBLE_AXE
            ).require(ROGUES)
    );
    public static final Entry WEAPON_WHIRLWIND_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_whirlwind_modifier_1).require(ROGUES));
    public static final Entry WEAPON_WHIRLWIND_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_whirlwind_modifier_2).require(ROGUES));

    // Spear (Impale)
    public static final Entry WEAPON_SPEAR_ROOT = add(
            Entry.conditionalAttribute("weapon_spear_root",
                    "Spear Specialisation",
                    null,
                    Icon.item("archers:iron_spear"),
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.SPEAR
            ).require(ARCHERS)
    );
    public static final Entry WEAPON_IMPALE_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_impale_modifier_1).require(ARCHERS));
    public static final Entry WEAPON_IMPALE_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_impale_modifier_2).require(ARCHERS));

    // Dagger (Fan of Knives)
    public static final Entry WEAPON_DAGGER_ROOT = add(
            Entry.conditionalAttribute("weapon_dagger_root",
                    "Dagger Specialisation",
                    null,
                    Icon.item("rogues:iron_dagger"),
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.DAGGER
            ).require(ROGUES)
    );
    public static final Entry WEAPON_FAN_OF_KNIVES_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_fan_of_knives_modifier_1).require(ROGUES));
    public static final Entry WEAPON_FAN_OF_KNIVES_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_fan_of_knives_modifier_2).require(ROGUES));

    // Sickle (Swipe)
    public static final Entry WEAPON_SICKLE_ROOT = add(
            Entry.conditionalAttribute("weapon_sickle_root",
                    "Sickle Specialisation",
                    null,
                    Icon.item("rogues:iron_sickle"),
                    CRIT_CHANCE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_CHANCE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.SICKLE
            ).require(ROGUES)
    );
    public static final Entry WEAPON_SWIPE_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_swipe_modifier_1).require(ROGUES));
    public static final Entry WEAPON_SWIPE_MODIFIER_2 = add(passiveSpell(WeaponSkillModifiers.weapon_swipe_modifier_2)
            .withIcon(Icon.spell(WeaponSkills.SWIPE.id()))
            .require(ROGUES));

    // Glaive (Thrust)
    public static final Entry WEAPON_GLAIVE_ROOT = add(
            Entry.conditionalAttribute("weapon_glaive_root",
                    "Glaive Specialisation",
                    null,
                    Icon.item("rogues:iron_glaive"),
                    CRIT_DAMAGE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.GLAIVE
            ).require(ROGUES)
    );
    public static final Entry WEAPON_THRUST_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_thrust_modifier_1).require(ROGUES));
    public static final Entry WEAPON_THRUST_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_thrust_modifier_2).require(ROGUES));

    // Axe (Cleave)
    public static final Entry WEAPON_AXE_ROOT = add(
            Entry.conditionalAttribute("weapon_axe_root",
                    "Axe Specialisation",
                    null,
                    Icon.item("minecraft:iron_axe"),
                    CRIT_CHANCE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_CHANCE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.AXE
            )
    );
    public static final Entry WEAPON_CLEAVE_MODIFIER_1 = add(modifierSpell(WeaponSkillModifiers.weapon_cleave_modifier_1));
    public static final Entry WEAPON_CLEAVE_MODIFIER_2 = add(modifierSpell(WeaponSkillModifiers.weapon_cleave_modifier_2));

    // Bow
    public static final Entry WEAPON_BOW_ROOT = add(
            Entry.conditionalAttribute("weapon_bow_root",
                    "Bow Specialisation",
                    null,
                    Icon.item("minecraft:bow"),
                    "ranged_weapon:damage",
                    "minecraft:generic.attack_damage",
                    WEAPON_ROOT_DAMAGE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.BOW
            )
    );
    public static final Entry WEAPON_BOW_PASSIVE_1 = add(passiveSpell(WeaponSkillModifiers.weapon_bow_passive_1)
            .withIcon(Icon.item("minecraft:bow")));
    public static final Entry WEAPON_BOW_PASSIVE_2 = add(passiveSpell(WeaponSkillModifiers.weapon_bow_passive_2)
            .withIcon(Icon.item("minecraft:bow")));

    // Crossbow
    public static final Entry WEAPON_CROSSBOW_ROOT = add(
            Entry.conditionalAttribute("weapon_crossbow_root",
                    "Crossbow Specialisation",
                    null,
                    Icon.item("minecraft:crossbow"),
                    "ranged_weapon:haste",
                    ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_HASTE,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.CROSSBOW
            )
    );
    public static final Entry WEAPON_CROSSBOW_PASSIVE_1 = add(passiveSpell(WeaponSkillModifiers.weapon_crossbow_passive_1)
            .withIcon(Icon.item("minecraft:crossbow")));
    public static final Entry WEAPON_CROSSBOW_PASSIVE_2 = add(passiveSpell(WeaponSkillModifiers.weapon_crossbow_passive_2)
            .withIcon(Icon.item("minecraft:crossbow")));

    public static final Entry FIREBALL = add(
            Entry.spell("fireball",
                    "Fireball",
                    "Unlock Fireball",
                    Icon.spell(Identifier.of("wizards", "fireball")),
                    List.of(SpellContainers.forModifier(Identifier.of("wizards:fireball")))
            ).require(WIZARDS)
    );
}
