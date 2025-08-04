package net.skill_tree_rpgs.skills;

import net.skill_tree_rpgs.ClassSkillsMod;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.common.IconType;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class SkillDefinitions {
    public static final Identifier CATEGORY_ID = Identifier.of(ClassSkillsMod.NAMESPACE, "skill_tree_rpgs");
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
            return new Entry(id, title, description, icon, null, attributeReward);
        }
        public String titleTranslationKey() {
            return "skill." + ClassSkillsMod.NAMESPACE + "." + id + ".title";
        }
        public String descriptionTranslationKey() {
            return "skill." + ClassSkillsMod.NAMESPACE + "." + id + ".description";
        }
        public Entry withIcon(Icon icon) {
            return new Entry(id, title, description, icon, spellReward, attributeReward);
        }
    }
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }


    public static final float ROOT_MULTIPLIER = 0.01f;
    public static final float BOOST_MULTIPLIER = 0.01f;

//    private static List<Text> attributeModifierText(EntityAttributeReward reward) {
//        List<Text> lines = new ArrayList<>();
//        return lines;
//    }

    private static List<SpellContainer> dummyContainer() {
        return List.of(SpellContainerHelper.createForSpellHost(Identifier.of("wizards:fireball")));
    }

    private static Entry modifierSpell(Spells.Entry entry) {
        var modifiedSpellId = Identifier.of(entry.spell().modifiers.getFirst().spell_pattern);
        return Entry.spell(entry.id().getPath(),
                entry.title(),
                null,
                Icon.spell(modifiedSpellId),
                List.of(SpellContainerHelper.createForModifier(entry.id()))
        );
    }

    private static Entry passiveSpell(Spells.Entry entry) {
        return Entry.spell(entry.id().getPath(),
                entry.title(),
                null,
                Icon.spell(entry.id()),
                List.of(SpellContainerHelper.createForSpellHost(entry.id()))
        );
    }

    public static final Entry ARCANE_ROOT = add(
            Entry.attribute("arcane_root",
                    "Path of Arcane",
                    null,
                    Icon.item("wizards:arcane_spell_book"),
                    SpellSchools.ARCANE.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry ARCANE_BOOST = add(
            Entry.attribute("arcane_boost",
                    "Arcane Attunement",
                    null,
                    Icon.item("wizards:wand_arcane"),
                    ARCANE_ROOT.attributeReward())
    );
    public static final Entry ARCANE_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.arcane_spec_a_modifier_1));
    public static final Entry ARCANE_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.arcane_spec_b_modifier_1));
    public static final Entry ARCANE_SPEC_A_MODIFIER_2 = add(modifierSpell(Spells.arcane_spec_a_modifier_2));
    public static final Entry ARCANE_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.arcane_spec_b_modifier_2));
    public static final Entry ARCANE_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.arcane_spec_a_modifier_3));
    public static final Entry ARCANE_SPEC_B_MODIFIER_3 = add(modifierSpell(Spells.arcane_spec_b_modifier_3));
    public static final Entry ARCANE_SPEC_A_MODIFIER_4 = add(passiveSpell(Spells.arcane_spec_a_modifier_4)
            .withIcon(Icon.spell(Identifier.of("wizards", "arcane_blink")))
    );
    public static final Entry ARCANE_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.arcane_spec_b_modifier_4));
    public static final Entry ARCANE_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.arcane_spec_a_passive_1));
    public static final Entry ARCANE_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.arcane_spec_b_passive_1));
    public static final Entry ARCANE_SPEC_A_PASSIVE_2 = add(passiveSpell(Spells.arcane_spec_a_passive_2));
    public static final Entry ARCANE_SPEC_B_PASSIVE_2 = add(passiveSpell(Spells.arcane_spec_b_passive_2));
    public static final Entry ARCANE_SPEC_A_PASSIVE_3 = add(passiveSpell(Spells.arcane_spec_a_passive_3));
    public static final Entry ARCANE_SPEC_B_PASSIVE_3 = add(passiveSpell(Spells.arcane_spec_b_passive_3));

    public static final Entry FIRE_ROOT = add(
            Entry.attribute("fire_root",
                    "Path of Fire",
                    null,
                    Icon.item("wizards:fire_spell_book"),
                    SpellSchools.FIRE.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry FIRE_BOOST = add(
            Entry.attribute("fire_boost",
                    "Fire Attunement",
                    null,
                    Icon.item("wizards:wand_fire"),
                    FIRE_ROOT.attributeReward())
    );
    public static final Entry FIRE_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.fire_spec_a_modifier_1));
    public static final Entry FIRE_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.fire_spec_b_modifier_1));
    public static final Entry FIRE_SPEC_A_MODIFIER_2 = add(passiveSpell(Spells.fire_spec_a_modifier_2)
            .withIcon(Icon.spell(Identifier.of("wizards", "fire_breath"))));
    public static final Entry FIRE_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.fire_spec_b_modifier_2));
    public static final Entry FIRE_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.fire_spec_a_modifier_3));
    public static final Entry FIRE_SPEC_B_MODIFIER_3 = add(passiveSpell(Spells.fire_spec_b_modifier_3)
            .withIcon(Icon.spell(Identifier.of("wizards", "fire_meteor"))));
    public static final Entry FIRE_SPEC_A_MODIFIER_4 = add(modifierSpell(Spells.fire_spec_a_modifier_4));
    public static final Entry FIRE_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.fire_spec_b_modifier_4));

    public static final Entry FIRE_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.fire_spec_a_passive_1));
    public static final Entry FIRE_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.fire_spec_b_passive_1));
    public static final Entry FIRE_SPEC_A_PASSIVE_2 = add(passiveSpell(Spells.fire_spec_a_passive_2));
    public static final Entry FIRE_SPEC_B_PASSIVE_2 = add(passiveSpell(Spells.fire_spec_b_passive_2));

    public static final Entry FIRE_SPEC_A_PASSIVE_3 = add(
            Entry.spell("fire_spec_a_passive_3",
                    "Fire Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("wizards", "fire_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry FIRE_SPEC_B_PASSIVE_3 = add(
            Entry.spell("fire_spec_b_passive_3",
                    "Fire Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("wizards", "fire_spec_b_passive_3")),
                    dummyContainer()
            )
    );

    public static final Entry FROST_ROOT = add(
            Entry.attribute("frost_root",
                    "Path of Frost",
                    null,
                    Icon.item("wizards:frost_spell_book"),
                    SpellSchools.FROST.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry FROST_BOOST = add(
            Entry.attribute("frost_boost",
                    "Frost Attunement",
                    null,
                    Icon.item("wizards:wand_frost"),
                    FROST_ROOT.attributeReward())
    );
    public static final Entry FROST_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.frost_spec_a_modifier_1));
    public static final Entry FROST_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.frost_spec_b_modifier_1));
    public static final Entry FROST_SPEC_A_MODIFIER_2 = add(passiveSpell(Spells.frost_spec_a_modifier_2)
            .withIcon(Icon.spell(Identifier.of("wizards", "frost_nova")))
    );
    public static final Entry FROST_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.frost_spec_b_modifier_2));
    public static final Entry FROST_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.frost_spec_a_modifier_3));
    public static final Entry FROST_SPEC_B_MODIFIER_3 = add(modifierSpell(Spells.frost_spec_b_modifier_3));
    public static final Entry FROST_SPEC_A_MODIFIER_4 = add(modifierSpell(Spells.frost_spec_a_modifier_4));
    public static final Entry FROST_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.frost_spec_b_modifier_4));

    public static final Entry FROST_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.frost_spec_a_passive_1));
    public static final Entry FROST_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.frost_spec_b_passive_1));

    public static final Entry FROST_SPEC_A_PASSIVE_2 = add(
            Entry.spell("frost_spec_a_passive_2",
                    "Frost Spec A Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("wizards", "frost_spec_a_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry FROST_SPEC_B_PASSIVE_2 = add(
            Entry.spell("frost_spec_b_passive_2",
                    "Frost Spec B Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("wizards", "frost_spec_b_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry FROST_SPEC_A_PASSIVE_3 = add(
            Entry.spell("frost_spec_a_passive_3",
                    "Frost Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("wizards", "frost_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry FROST_SPEC_B_PASSIVE_3 = add(
            Entry.spell("frost_spec_b_passive_3",
                    "Frost Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("wizards", "frost_spec_b_passive_3")),
                    dummyContainer()
            )
    );

    public static final Entry PRIEST_ROOT = add(
            Entry.attribute("priest_root",
                    "Path of the Light",
                    null,
                    Icon.item("paladins:priest_spell_book"),
                    SpellSchools.HEALING.attributeEntry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry PRIEST_BOOST = add(
            Entry.attribute("priest_boost",
                    "Holy Attunement",
                    null,
                    Icon.item("paladins:holy_wand"),
                    PRIEST_ROOT.attributeReward())
    );
    public static final Entry PRIEST_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.priest_spec_a_modifier_1));
    public static final Entry PRIEST_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.priest_spec_b_modifier_1));
    public static final Entry PRIEST_SPEC_A_MODIFIER_2 = add(modifierSpell(Spells.priest_spec_a_modifier_2));
    public static final Entry PRIEST_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.priest_spec_b_modifier_2));
    public static final Entry PRIEST_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.priest_spec_a_modifier_3));
    public static final Entry PRIEST_SPEC_B_MODIFIER_3 = add(passiveSpell(Spells.priest_spec_b_modifier_3)
            .withIcon(Icon.spell(Identifier.of("paladins", "circle_of_healing")))
    );
    public static final Entry PRIEST_SPEC_A_MODIFIER_4 = add(modifierSpell(Spells.priest_spec_a_modifier_4));
    public static final Entry PRIEST_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.priest_spec_b_modifier_4));

    public static final Entry PRIEST_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.priest_spec_a_passive_1));
    public static final Entry PRIEST_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.priest_spec_b_passive_1));

    public static final Entry PRIEST_SPEC_A_PASSIVE_2 = add(
            Entry.spell("priest_spec_a_passive_2",
                    "Priest Spec A Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "priest_spec_a_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry PRIEST_SPEC_B_PASSIVE_2 = add(
            Entry.spell("priest_spec_b_passive_2",
                    "Priest Spec B Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "priest_spec_b_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry PRIEST_SPEC_A_PASSIVE_3 = add(
            Entry.spell("priest_spec_a_passive_3",
                    "Priest Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "priest_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry PRIEST_SPEC_B_PASSIVE_3 = add(
            Entry.spell("priest_spec_b_passive_3",
                    "Priest Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "priest_spec_b_passive_3")),
                    dummyContainer()
            )
    );

    public static final Entry PALADIN_ROOT = add(
            Entry.attribute("paladin_root",
                    "Path of the Paladin",
                    null,
                    Icon.item("paladins:paladin_spell_book"),
                    SpellSchools.HEALING.attributeEntry,
                    0.2,
                    EntityAttributeModifier.Operation.ADD_VALUE
            )
    );
    public static final Entry PALADIN_BOOST = add(
            Entry.attribute("paladin_boost",
                    "Paladin Empowerment",
                    null,
                    Icon.item("paladins:iron_mace"),
                    PALADIN_ROOT.attributeReward())
    );
    public static final Entry PALADIN_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.paladin_spec_a_modifier_1));
    public static final Entry PALADIN_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.paladin_spec_b_modifier_1));
    public static final Entry PALADIN_SPEC_A_MODIFIER_2 = add(modifierSpell(Spells.paladin_spec_a_modifier_2));
    public static final Entry PALADIN_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.paladin_spec_b_modifier_2));
    public static final Entry PALADIN_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.paladin_spec_a_modifier_3));
    public static final Entry PALADIN_SPEC_B_MODIFIER_3 = add(modifierSpell(Spells.paladin_spec_b_modifier_3));
    public static final Entry PALADIN_SPEC_A_MODIFIER_4 = add(modifierSpell(Spells.paladin_spec_a_modifier_4));
    public static final Entry PALADIN_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.paladin_spec_b_modifier_4));

    public static final Entry PALADIN_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.paladin_spec_a_passive_1));
    public static final Entry PALADIN_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.paladin_spec_b_passive_1));

    public static final Entry PALADIN_SPEC_A_PASSIVE_2 = add(
            Entry.spell("paladin_spec_a_passive_2",
                    "Paladin Spec A Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "paladin_spec_a_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry PALADIN_SPEC_B_PASSIVE_2 = add(
            Entry.spell("paladin_spec_b_passive_2",
                    "Paladin Spec B Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "paladin_spec_b_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry PALADIN_SPEC_A_PASSIVE_3 = add(
            Entry.spell("paladin_spec_a_passive_3",
                    "Paladin Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "paladin_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry PALADIN_SPEC_B_PASSIVE_3 = add(
            Entry.spell("paladin_spec_b_passive_3",
                    "Paladin Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("paladins", "paladin_spec_b_passive_3")),
                    dummyContainer()
            )
    );

    public static final Entry ARCHER_ROOT = add(
            Entry.attribute("archer_root",
                    "Path of the Archer",
                    null,
                    Icon.item("archers:archer_spell_book"),
                    EntityAttributes_RangedWeapon.DAMAGE.entry,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry ARCHER_BOOST = add(
            Entry.attribute("archer_boost",
                    "Archer Empowerment",
                    null,
                    Icon.item("archers:composite_longbow"),
                    ARCHER_ROOT.attributeReward())
    );
    public static final Entry ARCHER_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.archer_spec_a_modifier_1));
    public static final Entry ARCHER_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.archer_spec_b_modifier_1));
    public static final Entry ARCHER_SPEC_A_MODIFIER_2 = add(modifierSpell(Spells.archer_spec_a_modifier_2));
    public static final Entry ARCHER_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.archer_spec_b_modifier_2));
    public static final Entry ARCHER_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.archer_spec_a_modifier_3));
    public static final Entry ARCHER_SPEC_B_MODIFIER_3 = add(modifierSpell(Spells.archer_spec_b_modifier_3));
    public static final Entry ARCHER_SPEC_A_MODIFIER_4 = add(passiveSpell(Spells.archer_spec_a_modifier_4)
            .withIcon(Icon.spell(Identifier.of("archers", "magic_arrow"))));
    public static final Entry ARCHER_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.archer_spec_b_modifier_4));

    public static final Entry ARCHER_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.archer_spec_a_passive_1));
    public static final Entry ARCHER_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.archer_spec_b_passive_1));

    public static final Entry ARCHER_SPEC_A_PASSIVE_2 = add(
            Entry.spell("archer_spec_a_passive_2",
                    "Archer Spec A Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("archers", "archer_spec_a_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry ARCHER_SPEC_B_PASSIVE_2 = add(
            Entry.spell("archer_spec_b_passive_2",
                    "Archer Spec B Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("archers", "archer_spec_b_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry ARCHER_SPEC_A_PASSIVE_3 = add(
            Entry.spell("archer_spec_a_passive_3",
                    "Archer Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("archers", "archer_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry ARCHER_SPEC_B_PASSIVE_3 = add(
            Entry.spell("archer_spec_b_passive_3",
                    "Archer Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("archers", "archer_spec_b_passive_3")),
                    dummyContainer()
            )
    );

    public static final Entry ROGUE_ROOT = add(
            Entry.attribute("rogue_root",
                    "Path of the Rogue",
                    null,
                    Icon.item("rogues:rogue_spell_book"),
                    EntityAttributes.GENERIC_ATTACK_SPEED,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry ROGUE_BOOST = add(
            Entry.attribute("rogue_boost",
                    "Rogue Empowerment",
                    null,
                    Icon.item("rogues:iron_sickle"),
                    ROGUE_ROOT.attributeReward())
    );
    public static final Entry ROGUE_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.rogue_spec_a_modifier_1));
    public static final Entry ROGUE_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.rogue_spec_b_modifier_1));
    public static final Entry ROGUE_SPEC_A_MODIFIER_2 = add(modifierSpell(Spells.rogue_spec_a_modifier_2));
    public static final Entry ROGUE_SPEC_B_MODIFIER_2 = add(passiveSpell(Spells.rogue_spec_b_modifier_2)
            .withIcon(Icon.spell(Identifier.of("rogues:shock_powder")))
    );
    public static final Entry ROGUE_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.rogue_spec_a_modifier_3));
    public static final Entry ROGUE_SPEC_B_MODIFIER_3 = add(modifierSpell(Spells.rogue_spec_b_modifier_3));
    public static final Entry ROGUE_SPEC_A_MODIFIER_4 = add(modifierSpell(Spells.rogue_spec_a_modifier_4));
    public static final Entry ROGUE_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.rogue_spec_b_modifier_4));

    public static final Entry ROGUE_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.rogue_spec_a_passive_1));
    public static final Entry ROGUE_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.rogue_spec_b_passive_1));

    public static final Entry ROGUE_SPEC_A_PASSIVE_2 = add(
            Entry.spell("rogue_spec_a_passive_2",
                    "Rogue Spec A Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "rogue_spec_a_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry ROGUE_SPEC_B_PASSIVE_2 = add(
            Entry.spell("rogue_spec_b_passive_2",
                    "Rogue Spec B Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "rogue_spec_b_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry ROGUE_SPEC_A_PASSIVE_3 = add(
            Entry.spell("rogue_spec_a_passive_3",
                    "Rogue Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "rogue_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry ROGUE_SPEC_B_PASSIVE_3 = add(
            Entry.spell("rogue_spec_b_passive_3",
                    "Rogue Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "rogue_spec_b_passive_3")),
                    dummyContainer()
            )
    );

    public static final Entry WARRIOR_ROOT = add(
            Entry.attribute("warrior_root",
                    "Path of the Warrior",
                    null,
                    Icon.item("rogues:warrior_spell_book"),
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    0.01,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
    );
    public static final Entry WARRIOR_BOOST = add(
            Entry.attribute("warrior_boost",
                    "Warrior Empowerment",
                    null,
                    Icon.item("rogues:iron_double_axe"),
                    WARRIOR_ROOT.attributeReward())
    );
    public static final Entry WARRIOR_SPEC_A_MODIFIER_1 = add(modifierSpell(Spells.warrior_spec_a_modifier_1));
    public static final Entry WARRIOR_SPEC_B_MODIFIER_1 = add(modifierSpell(Spells.warrior_spec_b_modifier_1));
    public static final Entry WARRIOR_SPEC_A_MODIFIER_2 = add(passiveSpell(Spells.warrior_spec_a_modifier_2)
            .withIcon(Icon.spell(Identifier.of("rogues", "shout")))
    );
    public static final Entry WARRIOR_SPEC_B_MODIFIER_2 = add(modifierSpell(Spells.warrior_spec_b_modifier_2));
    public static final Entry WARRIOR_SPEC_A_MODIFIER_3 = add(modifierSpell(Spells.warrior_spec_a_modifier_3));
    public static final Entry WARRIOR_SPEC_B_MODIFIER_3 = add(passiveSpell(Spells.warrior_spec_b_modifier_3)
            .withIcon(Icon.spell(Identifier.of("rogues", "charge")))
    );
    public static final Entry WARRIOR_SPEC_A_MODIFIER_4 = add(modifierSpell(Spells.warrior_spec_a_modifier_4));
    public static final Entry WARRIOR_SPEC_B_MODIFIER_4 = add(modifierSpell(Spells.warrior_spec_b_modifier_4));

    public static final Entry WARRIOR_SPEC_A_PASSIVE_1 = add(passiveSpell(Spells.warrior_spec_a_passive_1));
    public static final Entry WARRIOR_SPEC_B_PASSIVE_1 = add(passiveSpell(Spells.warrior_spec_b_passive_1));
    public static final Entry WARRIOR_SPEC_A_PASSIVE_2 = add(
            Entry.spell("warrior_spec_a_passive_2",
                    "Warrior Spec A Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "warrior_spec_a_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry WARRIOR_SPEC_B_PASSIVE_2 = add(
            Entry.spell("warrior_spec_b_passive_2",
                    "Warrior Spec B Passive 2",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "warrior_spec_b_passive_2")),
                    dummyContainer()
            )
    );
    public static final Entry WARRIOR_SPEC_A_PASSIVE_3 = add(
            Entry.spell("warrior_spec_a_passive_3",
                    "Warrior Spec A Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "warrior_spec_a_passive_3")),
                    dummyContainer()
            )
    );
    public static final Entry WARRIOR_SPEC_B_PASSIVE_3 = add(
            Entry.spell("warrior_spec_b_passive_3",
                    "Warrior Spec B Passive 3",
                    "Placeholder",
                    Icon.spell(Identifier.of("rogues", "warrior_spec_b_passive_3")),
                    dummyContainer()
            )
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
