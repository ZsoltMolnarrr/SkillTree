package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.attributes.ModifierCondition;
import net.skill_tree_rpgs.attributes.ModifierConditions;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.rpg_foundation.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.puffish.skillsmod.common.IconType;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.datagen.WeaponSkills;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NodeTypes {
    public static final Identifier CATEGORY_ID = Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "class_skills");
    public static final Identifier WEAPON_CATEGORY_ID = Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "weapon_skills");
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
    public record EntityAttributeReward(Holder<Attribute> attribute, AttributeModifier modifier) {
        public static EntityAttributeReward of(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            return new EntityAttributeReward(attribute, new AttributeModifier(Identifier.parse(SkillTreeMod.NAMESPACE + ":attribute_reward"), value, operation));
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
                                      Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            return attribute(id, title, description, icon, EntityAttributeReward.of(attribute, value, operation));
        }
        public static Entry attribute(String id, String title, String description, Icon icon, EntityAttributeReward attributeReward) {
            return new Entry(id, title, description, icon, null, attributeReward, null, null);
        }
        public static Entry conditionalAttribute(String id, String title, String description, Icon icon,
                                                 Holder<Attribute> attribute, double value,
                                                 AttributeModifier.Operation operation,
                                                 ModifierCondition condition) {
            return conditionalAttribute(id, title, description, icon,
                    attribute.unwrapKey().orElseThrow().identifier().toString(), null, value, operation, condition);
        }
        public static Entry conditionalAttribute(String id, String title, String description, Icon icon,
                                                 Holder<Attribute> attribute,
                                                 Holder<Attribute> fallbackAttribute,
                                                 double value,
                                                 AttributeModifier.Operation operation,
                                                 ModifierCondition condition) {
            return conditionalAttribute(id, title, description, icon,
                    attribute.unwrapKey().orElseThrow().identifier().toString(),
                    fallbackAttribute.unwrapKey().orElseThrow().identifier().toString(),
                    value, operation, condition);
        }
        public static Entry conditionalAttribute(String id, String title, String description, Icon icon,
                                                 String attribute, String fallbackAttribute,
                                                 double value,
                                                 AttributeModifier.Operation operation,
                                                 ModifierCondition condition) {
            return new Entry(id, title, description, icon, null, null, toDataStructure(attribute, fallbackAttribute, value, operation, condition), null);
        }
        private static ConditionalAttributeReward.DataStructure toDataStructure(
                String attribute, String fallbackAttribute,
                double value, AttributeModifier.Operation operation,
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
                                    condition.equipment().slot().getName(), condition.equipment().tag().location().toString())));
        }
        public String titleTranslationKey() {
            return "skill." + SkillTreeMod.NAMESPACE + "." + id + ".title";
        }
        public String descriptionTranslationKey() {
            return "skill." + SkillTreeMod.NAMESPACE + "." + id + ".description";
        }
        public Entry require(String modId) {
            return new Entry(id, title, description, icon, spellReward, attributeReward, conditionalAttributeReward, List.of(modId));
        }
    }

    /// Hand-authored, irreducible nodes: attribute roots/boosts, conditional weapon-spec roots,
    /// and the Fireball unlock. Their reward data (attribute + value + operation, or equip condition)
    /// exists nowhere else, so it cannot be derived. Every *spell* node is generated instead — see
    /// {@link #spellNodesFromSkills()} and {@link #allNodes()}.
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

    // ===== CLASS ATTUNEMENT ROOTS / BOOSTS (attribute rewards) =====

    public static final Entry ARCANE_ROOT = add(
            Entry.attribute("arcane_root",
                    "Path of Arcane",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "wizards:spell_book/arcane"),
                    SpellSchools.ARCANE.attributeEntry,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(WIZARDS)
    );
    public static final Entry ARCANE_BOOST = add(
            Entry.attribute("arcane_boost",
                    "Arcane Attunement",
                    null,
                    Icon.item("wizards:wand_arcane"),
                    ARCANE_ROOT.attributeReward()).require(WIZARDS)
    );

    public static final Entry FIRE_ROOT = add(
            Entry.attribute("fire_root",
                    "Path of Fire",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "wizards:spell_book/fire"),
                    SpellSchools.FIRE.attributeEntry,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(WIZARDS)
    );
    public static final Entry FIRE_BOOST = add(
            Entry.attribute("fire_boost",
                    "Fire Attunement",
                    null,
                    Icon.item("wizards:wand_fire"),
                    FIRE_ROOT.attributeReward()).require(WIZARDS)
    );

    public static final Entry FROST_ROOT = add(
            Entry.attribute("frost_root",
                    "Path of Frost",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "wizards:spell_book/frost"),
                    SpellSchools.FROST.attributeEntry,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(WIZARDS)
    );
    public static final Entry FROST_BOOST = add(
            Entry.attribute("frost_boost",
                    "Frost Attunement",
                    null,
                    Icon.item("wizards:wand_frost"),
                    FROST_ROOT.attributeReward()).require(WIZARDS)
    );

    public static final Entry PRIEST_ROOT = add(
            Entry.attribute("priest_root",
                    "Path of the Light",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "paladins:spell_book/priest"),
                    SpellSchools.HEALING.attributeEntry,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(PALADINS)
    );
    public static final Entry PRIEST_BOOST = add(
            Entry.attribute("priest_boost",
                    "Holy Attunement",
                    null,
                    Icon.item("paladins:holy_wand"),
                    PRIEST_ROOT.attributeReward()).require(PALADINS)
    );

    public static final Entry PALADIN_ROOT = add(
            Entry.attribute("paladin_root",
                    "Path of the Paladin",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "paladins:spell_book/paladin"),
                    SpellSchools.HEALING.attributeEntry,
                    0.2,
                    AttributeModifier.Operation.ADD_VALUE
            ).require(PALADINS)
    );
    public static final Entry PALADIN_BOOST = add(
            Entry.attribute("paladin_boost",
                    "Paladin Empowerment",
                    null,
                    Icon.item("paladins:iron_mace"),
                    PALADIN_ROOT.attributeReward()).require(PALADINS)
    );

    public static final Entry ARCHER_ROOT = add(
            Entry.attribute("archer_root",
                    "Path of the Archer",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "archers:spell_book/archer"),
                    EntityAttributes_RangedWeapon.DAMAGE.entry,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(ARCHERS)
    );
    public static final Entry ARCHER_BOOST = add(
            Entry.attribute("archer_boost",
                    "Archer Empowerment",
                    null,
                    Icon.item("archers:composite_longbow"),
                    ARCHER_ROOT.attributeReward()).require(ARCHERS)
    );

    public static final Entry ROGUE_ROOT = add(
            Entry.attribute("rogue_root",
                    "Path of the Rogue",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "rogues:spell_book/rogue"),
                    Attributes.ATTACK_SPEED,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(ROGUES)
    );
    public static final Entry ROGUE_BOOST = add(
            Entry.attribute("rogue_boost",
                    "Rogue Empowerment",
                    null,
                    Icon.item("rogues:iron_sickle"),
                    ROGUE_ROOT.attributeReward()).require(ROGUES)
    );

    public static final Entry WARRIOR_ROOT = add(
            Entry.attribute("warrior_root",
                    "Path of the Warrior",
                    null,
                    Icon.itemWithModel("spell_engine:spell_book", "rogues:spell_book/warrior"),
                    Attributes.ATTACK_DAMAGE,
                    0.01,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ).require(ROGUES)
    );
    public static final Entry WARRIOR_BOOST = add(
            Entry.attribute("warrior_boost",
                    "Warrior Empowerment",
                    null,
                    Icon.item("rogues:iron_double_axe"),
                    WARRIOR_ROOT.attributeReward()).require(ROGUES)
    );

    // ===== PHYSICAL WEAPON SPECIALISATION ROOTS (conditional attribute rewards) =====

    public static final float WEAPON_ROOT_DAMAGE = 0.05f;
    public static final float WEAPON_ROOT_CRIT_CHANCE = 0.04f;
    public static final float WEAPON_ROOT_CRIT_DAMAGE = 0.08f;
    public static final float WEAPON_ROOT_HASTE = 0.05f;

    public static final String CRIT_CHANCE_ID = "critical_strike:chance";
    public static final String CRIT_DAMAGE_ID = "critical_strike:damage";
    public static final String ATTACK_DAMAGE_ID = Attributes.ATTACK_DAMAGE.getRegisteredName();

    public static final Entry WEAPON_SWORD_ROOT = add(
            Entry.conditionalAttribute("weapon_sword_root",
                    "Sword Specialisation",
                    null,
                    Icon.item("minecraft:iron_sword"),
                    Attributes.ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.SWORD
            )
    );
    public static final Entry WEAPON_CLAYMORE_ROOT = add(
            Entry.conditionalAttribute("weapon_claymore_root",
                    "Claymore Specialisation",
                    null,
                    Icon.item("paladins:iron_claymore"),
                    Attributes.ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.CLAYMORE
            ).require(PALADINS)
    );
    public static final Entry WEAPON_MACE_ROOT = add(
            Entry.conditionalAttribute("weapon_mace_root",
                    "Mace Specialisation",
                    null,
                    Icon.item("paladins:iron_mace"),
                    CRIT_DAMAGE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.MACE
            ).require(PALADINS)
    );
    public static final Entry WEAPON_HAMMER_ROOT = add(
            Entry.conditionalAttribute("weapon_hammer_root",
                    "Hammer Specialisation",
                    null,
                    Icon.item("paladins:iron_great_hammer"),
                    CRIT_DAMAGE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.HAMMER
            ).require(PALADINS)
    );
    public static final Entry WEAPON_DOUBLE_AXE_ROOT = add(
            Entry.conditionalAttribute("weapon_double_axe_root",
                    "Double Axe Specialisation",
                    null,
                    Icon.item("rogues:iron_double_axe"),
                    CRIT_CHANCE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_CHANCE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.DOUBLE_AXE
            ).require(ROGUES)
    );
    public static final Entry WEAPON_SPEAR_ROOT = add(
            Entry.conditionalAttribute("weapon_spear_root",
                    "Spear Specialisation",
                    null,
                    Icon.item("archers:iron_spear"),
                    Attributes.ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.SPEAR
            ).require(ARCHERS)
    );
    public static final Entry WEAPON_DAGGER_ROOT = add(
            Entry.conditionalAttribute("weapon_dagger_root",
                    "Dagger Specialisation",
                    null,
                    Icon.item("rogues:iron_dagger"),
                    Attributes.ATTACK_DAMAGE,
                    WEAPON_ROOT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.DAGGER
            ).require(ROGUES)
    );
    public static final Entry WEAPON_SICKLE_ROOT = add(
            Entry.conditionalAttribute("weapon_sickle_root",
                    "Sickle Specialisation",
                    null,
                    Icon.item("rogues:iron_sickle"),
                    CRIT_CHANCE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_CHANCE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.SICKLE
            ).require(ROGUES)
    );
    public static final Entry WEAPON_GLAIVE_ROOT = add(
            Entry.conditionalAttribute("weapon_glaive_root",
                    "Glaive Specialisation",
                    null,
                    Icon.item("rogues:iron_glaive"),
                    CRIT_DAMAGE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.GLAIVE
            ).require(ROGUES)
    );
    public static final Entry WEAPON_AXE_ROOT = add(
            Entry.conditionalAttribute("weapon_axe_root",
                    "Axe Specialisation",
                    null,
                    Icon.item("minecraft:iron_axe"),
                    CRIT_CHANCE_ID, ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_CRIT_CHANCE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.AXE
            )
    );
    public static final Entry WEAPON_BOW_ROOT = add(
            Entry.conditionalAttribute("weapon_bow_root",
                    "Bow Specialisation",
                    null,
                    Icon.item("minecraft:bow"),
                    "ranged_weapon:damage",
                    ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_DAMAGE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.BOW
            )
    );
    public static final Entry WEAPON_CROSSBOW_ROOT = add(
            Entry.conditionalAttribute("weapon_crossbow_root",
                    "Crossbow Specialisation",
                    null,
                    Icon.item("minecraft:crossbow"),
                    "ranged_weapon:haste",
                    ATTACK_DAMAGE_ID,
                    WEAPON_ROOT_HASTE,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    ModifierConditions.CROSSBOW
            )
    );

    public static final Entry FIREBALL = add(
            Entry.spell("fireball",
                    "Fireball",
                    "Unlock Fireball",
                    Icon.spell(Identifier.fromNamespaceAndPath("wizards", "fireball")),
                    List.of(SpellContainers.forModifier(Identifier.parse("wizards:fireball")))
            ).require(WIZARDS)
    );

    // ===== SPELL NODES (generated from Skills.ENTRIES) =====

    /// Skill entries that must NOT become their own node: helper spells spawned by other spells,
    /// and hidden companions folded into another node (see {@link #EXTRA_CONTAINERS}).
    private static final Set<String> EXCLUDED = Set.of(
            "lightwell_cleanse",                    // spawned by another spell, never a tree node
            "rogue_tier_2_spell_1_modifier_1_bonus" // folded into rogue_tier_2_spell_1_modifier_1
    );

    /// Nodes that grant additional containers beyond their own spell.
    private static final Map<String, List<Identifier>> EXTRA_CONTAINERS = Map.of(
            "rogue_tier_2_spell_1_modifier_1",
            List.of(Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "rogue_tier_2_spell_1_modifier_1_bonus"))
    );

    /// Nodes whose icon should point somewhere other than the mechanical default
    /// (MODIFIER → the modified spell, otherwise → the spell's own texture).
    private static final Map<String, Icon> ICON_OVERRIDES = Map.ofEntries(
            // Passives whose node icon points at the associated class spell:
            Map.entry("arcane_tier_2_spell_2_modifier_1",  Icon.spell(Identifier.fromNamespaceAndPath("wizards", "arcane_explosion"))),
            Map.entry("arcane_tier_2_spell_2_modifier_2",  Icon.spell(Identifier.fromNamespaceAndPath("wizards", "arcane_explosion"))),
            Map.entry("fire_tier_2_spell_1_modifier_1",    Icon.spell(Identifier.fromNamespaceAndPath("wizards", "fire_breath"))),
            Map.entry("fire_tier_3_spell_1_modifier_2",    Icon.spell(Identifier.fromNamespaceAndPath("wizards", "fire_meteor"))),
            Map.entry("fire_tier_2_spell_2_modifier_2",    Icon.spell(Identifier.fromNamespaceAndPath("wizards", "fire_slash"))),
            Map.entry("frost_tier_2_spell_1_modifier_1",   Icon.spell(Identifier.fromNamespaceAndPath("wizards", "frost_nova"))),
            Map.entry("archer_tier_4_spell_1_modifier_2",  Icon.spell(Identifier.fromNamespaceAndPath("archers", "rain_of_arrows"))),
            Map.entry("archer_tier_4_spell_2_modifier_1",  Icon.spell(Identifier.fromNamespaceAndPath("archers", "magic_arrow"))),
            Map.entry("priest_tier_4_spell_2_modifier_1",  Icon.spell(Identifier.fromNamespaceAndPath("paladins", "barrier"))),
            Map.entry("paladin_tier_4_spell_2_modifier_2", Icon.spell(Identifier.fromNamespaceAndPath("paladins", "immolation"))),
            Map.entry("rogue_tier_2_spell_1_modifier_1",   Icon.spell(Identifier.fromNamespaceAndPath("rogues", "shock_powder"))),
            Map.entry("rogue_tier_2_spell_1_modifier_2",   Icon.spell(Identifier.fromNamespaceAndPath("rogues", "shock_powder"))),
            Map.entry("warrior_tier_3_spell_1_root",       Icon.spell(Identifier.fromNamespaceAndPath("rogues", "charge"))),
            Map.entry("warrior_tier_3_spell_1_modifier_2", Icon.spell(Identifier.fromNamespaceAndPath("rogues", "charge"))),
            Map.entry("warrior_tier_4_spell_1_modifier_2", Icon.spell(Identifier.fromNamespaceAndPath("rogues", "mortal_strike"))),
            Map.entry("warrior_tier_3_spell_2_modifier_1", Icon.spell(Identifier.fromNamespaceAndPath("rogues", "shout"))),
            Map.entry("warrior_tier_4_spell_2_modifier_2", Icon.spell(Identifier.fromNamespaceAndPath("rogues", "last_stand"))),
            // Magic-staff specialisation roots: show the staff item, not the modified spell:
            Map.entry("weapon_arcane_root", Icon.item("wizards:staff_arcane")),
            Map.entry("weapon_fire_root",   Icon.item("wizards:staff_fire")),
            Map.entry("weapon_frost_root",  Icon.item("wizards:staff_frost")),
            Map.entry("weapon_holy_root",   Icon.item("paladins:holy_staff")),
            // Weapon passives whose node icon points at the weapon skill / item:
            Map.entry("weapon_smash_modifier_1",       Icon.spell(WeaponSkills.SMASH.id())),
            Map.entry("weapon_ground_slam_modifier_2", Icon.spell(WeaponSkills.GROUND_SLAM.id())),
            Map.entry("weapon_swipe_modifier_2",       Icon.spell(WeaponSkills.SWIPE.id())),
            Map.entry("weapon_bow_passive_1",      Icon.item("minecraft:bow")),
            Map.entry("weapon_bow_passive_2",      Icon.item("minecraft:bow")),
            Map.entry("weapon_crossbow_passive_1", Icon.item("minecraft:crossbow")),
            Map.entry("weapon_crossbow_passive_2", Icon.item("minecraft:crossbow"))
    );

    /// Mod requirement for WEAPON-category nodes, keyed by weapon-family id prefix. The class
    /// categories (ARCANE/FIRE/… → owning mod) are derived directly; only plain WEAPON nodes need
    /// this per-weapon table. Families not listed (sword/axe/bow/crossbow) are interchangeable.
    private static final LinkedHashMap<String, String> WEAPON_MODS = new LinkedHashMap<>();
    static {
        WEAPON_MODS.put("weapon_arcane", WIZARDS);
        WEAPON_MODS.put("weapon_fire", WIZARDS);
        WEAPON_MODS.put("weapon_frost", WIZARDS);
        WEAPON_MODS.put("weapon_holy", PALADINS);
        WEAPON_MODS.put("weapon_flurry", PALADINS);
        WEAPON_MODS.put("weapon_smash", PALADINS);
        WEAPON_MODS.put("weapon_ground_slam", PALADINS);
        WEAPON_MODS.put("weapon_whirlwind", ROGUES);
        WEAPON_MODS.put("weapon_fan_of_knives", ROGUES);
        WEAPON_MODS.put("weapon_swipe", ROGUES);
        WEAPON_MODS.put("weapon_thrust", ROGUES);
        WEAPON_MODS.put("weapon_impale", ARCHERS);
    }

    private static List<String> requiredMods(Skills.Entry skill) {
        var c = skill.categories();
        if (c.contains(Skills.Category.ARCANE) || c.contains(Skills.Category.FIRE) || c.contains(Skills.Category.FROST)) {
            return List.of(WIZARDS);
        }
        if (c.contains(Skills.Category.PRIEST) || c.contains(Skills.Category.PALADIN)) {
            return List.of(PALADINS);
        }
        if (c.contains(Skills.Category.ARCHER)) {
            return List.of(ARCHERS);
        }
        if (c.contains(Skills.Category.ROGUE) || c.contains(Skills.Category.WARRIOR)) {
            return List.of(ROGUES);
        }
        // WEAPON-only: requirement is per weapon family, not derivable from the category.
        for (var e : WEAPON_MODS.entrySet()) {
            if (skill.key().startsWith(e.getKey())) {
                return List.of(e.getValue());
            }
        }
        return List.of();
    }

    private static Entry spellNode(Skills.Entry skill) {
        var key = skill.key();
        Icon icon = ICON_OVERRIDES.get(key);
        if (icon == null) {
            // MODIFIER spells show the spell they modify; passives/actives show their own texture.
            icon = skill.spell().type == Spell.Type.MODIFIER
                    ? Icon.spell(Identifier.parse(skill.spell().modifiers.getFirst().spell_pattern))
                    : Icon.spell(skill.id());
        }
        var containers = new ArrayList<SpellContainer>();
        containers.add(SpellContainers.forModifier(skill.id()));
        for (var extra : EXTRA_CONTAINERS.getOrDefault(key, List.of())) {
            containers.add(SpellContainers.forModifier(extra));
        }
        var entry = Entry.spell(key, skill.title(), null, icon, containers);
        var mods = requiredMods(skill);
        return mods.isEmpty() ? entry : entry.require(mods.getFirst());
    }

    /// One spell node per entry in {@link Skills#ENTRIES}, minus entries that are already an
    /// authored node (e.g. the conditional weapon-spec roots) or explicitly {@link #EXCLUDED}.
    public static List<Entry> spellNodesFromSkills() {
        var authored = new HashSet<String>();
        for (var e : ENTRIES) {
            authored.add(e.id());
        }
        var result = new ArrayList<Entry>();
        for (var skill : Skills.ENTRIES) {
            var key = skill.key();
            if (authored.contains(key) || EXCLUDED.contains(key)) {
                continue;
            }
            result.add(spellNode(skill));
        }
        return result;
    }

    /// Every node type: the irreducible hand-authored {@link #ENTRIES} plus the generated spell nodes.
    /// Use this everywhere nodes are consumed (datagen definitions, lang, client tooltip resolvers).
    public static List<Entry> allNodes() {
        var all = new ArrayList<>(ENTRIES);
        all.addAll(spellNodesFromSkills());
        return all;
    }
}
