package net.skill_tree_rpgs.attributes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.LinkedHashMap;
import java.util.Locale;

public class ModifierConditions {

    public static final LinkedHashMap<ModifierCondition, String> TRANSLATIONS = new LinkedHashMap<>();

    private static ModifierCondition mainhand(Equipment.WeaponType type, String displayText) {
        return weapon(type, EquipmentSlot.MAINHAND, displayText);
    }

    private static ModifierCondition weapon(Equipment.WeaponType type, EquipmentSlot slot, String displayText) {
        TagKey<Item> tag = RPGSeriesItemTags.WeaponType.get(type);
        String translationKey = type.toString().toLowerCase(Locale.ROOT);
        return create(tag, slot, translationKey, displayText);
    }

    private static ModifierCondition create(TagKey<Item> tag, EquipmentSlot slot, String translationKey, String displayText) {
        var condition = new ModifierCondition(new ModifierCondition.Equipment(slot, tag), translationKey);
        TRANSLATIONS.put(condition, displayText);
        return condition;
    }

    // Magic weapons
    public static final ModifierCondition DAMAGE_STAFF   = mainhand(Equipment.WeaponType.DAMAGE_STAFF,   "While holding a Damage Staff:");
    public static final ModifierCondition DAMAGE_WAND    = mainhand(Equipment.WeaponType.DAMAGE_WAND,    "While holding a Damage Wand:");
    public static final ModifierCondition HEALING_STAFF  = mainhand(Equipment.WeaponType.HEALING_STAFF,  "While holding a Healing Staff:");
    public static final ModifierCondition HEALING_WAND   = mainhand(Equipment.WeaponType.HEALING_WAND,   "While holding a Healing Wand:");
    public static final ModifierCondition SPELL_BLADE    = mainhand(Equipment.WeaponType.SPELL_BLADE,    "While holding a Spell Blade:");
    public static final ModifierCondition SPELL_SCYTHE   = mainhand(Equipment.WeaponType.SPELL_SCYTHE,   "While holding a Spell Scythe:");

    // Ranged weapons
    public static final ModifierCondition SHORT_BOW      = mainhand(Equipment.WeaponType.SHORT_BOW,      "While holding a Short Bow:");
    public static final ModifierCondition LONG_BOW       = mainhand(Equipment.WeaponType.LONG_BOW,       "While holding a Long Bow:");
    public static final ModifierCondition RAPID_CROSSBOW = mainhand(Equipment.WeaponType.RAPID_CROSSBOW, "While holding a Rapid Crossbow:");
    public static final ModifierCondition HEAVY_CROSSBOW = mainhand(Equipment.WeaponType.HEAVY_CROSSBOW, "While holding a Heavy Crossbow:");

    // Melee weapons
    public static final ModifierCondition SWORD          = mainhand(Equipment.WeaponType.SWORD,          "While holding a Sword:");
    public static final ModifierCondition CLAYMORE       = mainhand(Equipment.WeaponType.CLAYMORE,       "While holding a Claymore:");
    public static final ModifierCondition MACE           = mainhand(Equipment.WeaponType.MACE,           "While holding a Mace:");
    public static final ModifierCondition HAMMER         = mainhand(Equipment.WeaponType.HAMMER,         "While holding a Hammer:");
    public static final ModifierCondition SPEAR          = mainhand(Equipment.WeaponType.SPEAR,          "While holding a Spear:");
    public static final ModifierCondition DAGGER         = mainhand(Equipment.WeaponType.DAGGER,         "While holding a Dagger:");
    public static final ModifierCondition SICKLE         = mainhand(Equipment.WeaponType.SICKLE,         "While holding a Sickle:");
    public static final ModifierCondition DOUBLE_AXE     = mainhand(Equipment.WeaponType.DOUBLE_AXE,     "While holding a Double Axe:");
    public static final ModifierCondition GLAIVE         = mainhand(Equipment.WeaponType.GLAIVE,         "While holding a Glaive:");

    // Defense
    public static final ModifierCondition SHIELD         = weapon(Equipment.WeaponType.SHIELD, EquipmentSlot.OFFHAND, "While holding a Shield");

    // Vanilla meta types
    public static final ModifierCondition BOW      = create(ItemTags.BOW_ENCHANTABLE,      EquipmentSlot.MAINHAND, "bow",      "While holding a Bow:");
    public static final ModifierCondition CROSSBOW = create(ItemTags.CROSSBOW_ENCHANTABLE, EquipmentSlot.MAINHAND, "crossbow", "While holding a Crossbow:");
    public static final ModifierCondition AXE      = create(ItemTags.AXES,                 EquipmentSlot.MAINHAND, "axe",      "While holding an Axe:");
}
