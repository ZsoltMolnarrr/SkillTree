package net.class_skills.skills;

import net.class_skills.ClassSkillsMod;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.statuseffects.SpellVulnerabilityStatusEffect;

import java.util.ArrayList;
import java.util.List;

public class SkillEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Effects.Entry DIVINE_STRENGTH = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "divine_strength"),
            "Divine Strength",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FLEET_FOOTED = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "fleet_footed"),
            "Fleet Footed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x33ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ARCANE_SLOWNESS = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "arcane_slowness"),
            "Arcane Slowness",
            "Decreased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff99ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static final float FIRE_VULNERABILITY_MULTIPLIER = 0.05F;
    public static Effects.Entry FIRE_VULNERABILITY = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "fire_vulnerability"),
            "Fire Vulnerability",
            "Increased damage taken from fire.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0xff6600)
                    .setVulnerability(SpellSchools.FIRE, new SpellPower.Vulnerability(FIRE_VULNERABILITY_MULTIPLIER, 0F, 0F)),
            new EffectConfig(
                    List.of()
            )
    ));
    public static final float FROST_VULNERABILITY_MULTIPLIER = 0.1F;
    public static Effects.Entry FROST_VULNERABILITY = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "frost_vulnerability"),
            "Winter's Chill",
            "Increased damage taken from frost critical strikes.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff)
                    .setVulnerability(SpellSchools.FROST, new SpellPower.Vulnerability(0, 0F, FROST_VULNERABILITY_MULTIPLIER)),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry HEALING_FOCUS = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "healing_focus"),
            "Healing Focus",
            "Increased healing received.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ff99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.HEALING_TAKEN.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry INCANTER_CADENCE = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "incanter_cadence"),
            "Incanters' Cadence",
            "Increased spell haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry REDOUBT = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "redoubt"),
            "Redoubt",
            "Increased armor.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcccccc),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ARMOR.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry KILLING_SPREE = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "killing_spree"),
            "Killing Spree",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc66),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry RUPTURE = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "rupture"),
            "Rupture",
            "Increased damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6666),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry RHYTHM = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "rhythm"),
            "Rhythm",
            "Increased ranged attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xccff99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry PURSUIT_OF_JUSTICE = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "pursuit_of_justice"),
            "Pursuit of Justice",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ffcc),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry BATTLE_SHOUT = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "battle_shout"),
            "Battle Shout",
            "Increased attack power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff9933),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry NATURES_GRASP = add(new Effects.Entry(Identifier.of(ClassSkillsMod.NAMESPACE, "natures_grasp"),
            "Nature's Grasp",
            "Immobilized.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x66ff66),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -10,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_JUMP_STRENGTH.getIdAsString(),
                                    -10,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static void register(ConfigFile.Effects config) {
        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
        }
        Effects.register(entries, config.effects);
    }
}
