package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.SkillTreeMod;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.common.IconType;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class NodeTypes {
    public static final Identifier CATEGORY_ID = Identifier.of(SkillTreeMod.NAMESPACE, "skill_tree_rpgs");
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
    public record Entry(String id, String title, String description, Icon icon, List<SpellContainer> spellReward, EntityAttributeReward attributeReward, List<String> required_mods) {
        public static Entry spell(String id, String title, String description, Icon icon, List<SpellContainer> spellReward) {
            return new Entry(id, title, description, icon, spellReward, null, null);
        }
        public static Entry attribute(String id, String title, String description, Icon icon,
                                      RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation) {
            return attribute(id, title, description, icon, EntityAttributeReward.of(attribute, value, operation));
        }
        public static Entry attribute(String id, String title, String description, Icon icon, EntityAttributeReward attributeReward) {
            return new Entry(id, title, description, icon, null, attributeReward, null);
        }
        public String titleTranslationKey() {
            return "skill." + SkillTreeMod.NAMESPACE + "." + id + ".title";
        }
        public String descriptionTranslationKey() {
            return "skill." + SkillTreeMod.NAMESPACE + "." + id + ".description";
        }
        public Entry withIcon(Icon icon) {
            return new Entry(id, title, description, icon, spellReward, attributeReward, required_mods);
        }
        public Entry require(String modId) {
            return new Entry(id, title, description, icon, spellReward, attributeReward, List.of(modId));
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

    public static final float ROOT_MULTIPLIER = 0.01f;
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
    public static final Entry ARCANE_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(ArcaneSkills.arcane_tier_1_spell_1_modifier_1).require(WIZARDS));
    public static final Entry ARCANE_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcaneSkills.arcane_tier_1_spell_1_modifier_2).require(WIZARDS));
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
    public static final Entry FIRE_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(FireSkills.fire_tier_1_spell_1_modifier_1).require(WIZARDS));
    public static final Entry FIRE_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(FireSkills.fire_tier_1_spell_1_modifier_2).require(WIZARDS));
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
    public static final Entry FROST_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(FrostSkills.frost_tier_1_spell_1_modifier_1).require(WIZARDS));
    public static final Entry FROST_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(FrostSkills.frost_tier_1_spell_1_modifier_2).require(WIZARDS));
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
    public static final Entry PRIEST_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(PriestSkills.priest_tier_1_spell_1_modifier_1).require(PALADINS));
    public static final Entry PRIEST_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(PriestSkills.priest_tier_1_spell_1_modifier_2).require(PALADINS));
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
    public static final Entry PALADIN_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(PaladinSkills.paladin_tier_1_spell_1_modifier_1).require(PALADINS));
    public static final Entry PALADIN_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(PaladinSkills.paladin_tier_1_spell_1_modifier_2).require(PALADINS));
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
    
    public static final Entry ARCHER_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(ArcherSkills.archer_tier_1_spell_1_modifier_1).require(ARCHERS));
    public static final Entry ARCHER_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(ArcherSkills.archer_tier_1_spell_1_modifier_2).require(ARCHERS));
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
    public static final Entry ROGUE_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(RogueSkills.rogue_tier_1_spell_1_modifier_1).require(ROGUES));
    public static final Entry ROGUE_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(RogueSkills.rogue_tier_1_spell_1_modifier_2).require(ROGUES));
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
    public static final Entry WARRIOR_TIER_1_SPELL_1_MODIFIER_1 = add(modifierSpell(WarriorSkills.warrior_tier_1_spell_1_modifier_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_1_SPELL_1_MODIFIER_2 = add(modifierSpell(WarriorSkills.warrior_tier_1_spell_1_modifier_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_2_SPELL_1_MODIFIER_1 = add(passiveSpell(WarriorSkills.warrior_tier_2_spell_1_modifier_1)
            .withIcon(Icon.spell(Identifier.of("rogues", "shout")))
            .require(ROGUES)
    );
    public static final Entry WARRIOR_TIER_2_SPELL_1_MODIFIER_2 = add(modifierSpell(WarriorSkills.warrior_tier_2_spell_1_modifier_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_SPELL_1_MODIFIER_1 = add(modifierSpell(WarriorSkills.warrior_tier_3_spell_1_modifier_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_SPELL_1_MODIFIER_2 = add(passiveSpell(WarriorSkills.warrior_tier_3_spell_1_modifier_2)
            .withIcon(Icon.spell(Identifier.of("rogues", "charge")))
            .require(ROGUES)
    );
    public static final Entry WARRIOR_TIER_4_SPELL_1_MODIFIER_1 = add(modifierSpell(WarriorSkills.warrior_tier_4_spell_1_modifier_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_4_SPELL_1_MODIFIER_2 = add(modifierSpell(WarriorSkills.warrior_tier_4_spell_1_modifier_2).require(ROGUES));

    public static final Entry WARRIOR_TIER_1_PASSIVE_1 = add(passiveSpell(WarriorSkills.warrior_tier_1_passive_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_1_PASSIVE_2 = add(passiveSpell(WarriorSkills.warrior_tier_1_passive_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_2_PASSIVE_1 = add(passiveSpell(WarriorSkills.warrior_tier_2_passive_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_2_PASSIVE_2 = add(passiveSpell(WarriorSkills.warrior_tier_2_passive_2).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_PASSIVE_1 = add(passiveSpell(WarriorSkills.warrior_tier_3_passive_1).require(ROGUES));
    public static final Entry WARRIOR_TIER_3_PASSIVE_2 = add(passiveSpell(WarriorSkills.warrior_tier_3_passive_2).require(ROGUES));

    public static final Entry FIREBALL = add(
            Entry.spell("fireball",
                    "Fireball",
                    "Unlock Fireball",
                    Icon.spell(Identifier.of("wizards", "fireball")),
                    List.of(SpellContainers.forModifier(Identifier.of("wizards:fireball")))
            ).require(WIZARDS)
    );
}
