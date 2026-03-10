package net.skill_tree_rpgs.effect;

import net.skill_tree_rpgs.SkillTreeMod;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.skills.SkillSounds;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_engine.api.event.CombatEvents;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.tags.SpellEngineDamageTypeTags;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
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

    public static Effects.Entry DIVINE_STRENGTH = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "divine_strength"),
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
    public static Effects.Entry FLEET_FOOTED = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "fleet_footed"),
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
    public static Effects.Entry ARCANE_SLOWNESS = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "arcane_slowness"),
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
    public static Effects.Entry FIRE_VULNERABILITY = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "fire_vulnerability"),
            "Fire Vulnerability",
            "Increased damage taken from fire.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0xff6600)
                    .setVulnerability(SpellSchools.FIRE, new SpellPower.Vulnerability(FIRE_VULNERABILITY_MULTIPLIER, 0F, 0F)),
            new EffectConfig(
                    List.of()
            )
    ));
    public static final float FROST_VULNERABILITY_MULTIPLIER = 0.1F;
    public static Effects.Entry FROST_VULNERABILITY = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "frost_vulnerability"),
            "Winter's Chill",
            "Increased damage taken from frost critical strikes.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff)
                    .setVulnerability(SpellSchools.FROST, new SpellPower.Vulnerability(0, 0F, FROST_VULNERABILITY_MULTIPLIER)),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry HEALING_FOCUS = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "healing_focus"),
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
    public static Effects.Entry INCANTER_CADENCE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "incanter_cadence"),
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
    public static Effects.Entry REDOUBT = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "redoubt"),
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
    public static Effects.Entry KILLING_SPREE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "killing_spree"),
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
    public static Effects.Entry FRACTURE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "fracture"),
            "Fracture",
            "Reduces armor.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff6666),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ARMOR.getIdAsString(),
                                    -0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry RHYTHM = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "rhythm"),
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
    public static Effects.Entry PURSUIT_OF_JUSTICE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "pursuit_of_justice"),
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
    public static Effects.Entry BATTLE_SHOUT = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "battle_shout"),
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
    public static Effects.Entry NATURES_GRASP = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "natures_grasp"),
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
    public static final float ARCANE_EXPOSURE_MULTIPLIER = 0.02F;
    public static Effects.Entry ARCANE_EXPOSURE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "arcane_exposure"),
            "Arcane Exposure",
            "Increased arcane damage taken.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff)
                    .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(ARCANE_EXPOSURE_MULTIPLIER, 0F, 0F)),
            new EffectConfig(
                    List.of()
            )
    ));
    public static Effects.Entry ARCANE_SPEED = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "arcane_speed"),
            "Arcane Speed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_JUMP_STRENGTH.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FROST_SHIELD_SPEED = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "frost_shield_speed"),
            "Frost Shield Speed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry CONCUSSION_BLOW = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "concussion_blow"),
            "Concussing Blow",
            "Next attack stuns.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xffcc66),
            new EffectConfig(
                    List.of()
            )
    ));

    private static ParticleBatch CLOAK_OF_SHADOWS_POP = new ParticleBatch(
            SpellEngineParticles.MagicParticles.get(
                    SpellEngineParticles.MagicParticles.Shape.SKULL,
                    SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
            15, 0.25F, 0.25F)
            .color(Color.from(0xcc00cc).alpha(0.5F).toRGBA());
    public static Effects.Entry CLOAK_OF_SHADOWS = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "cloak_of_shadows"),
            "Cloak of Shadows",
            "Protects you from an attack",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x333333),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry AMBUSH = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "ambush"),
            "Ambush",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99cc66),

            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry PRESENCE_OF_MIND = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "presence_of_mind"),
            "Presence of Mind",
            "Next spell cast is instant.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier()
                    )
            )
    ));

    public static Effects.Entry BLIZZARD_SLOW = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "blizzard_slow"),
            "Blizzard Slow",
            "Decreased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry BANNER_PROTECTION = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "banner_protection"),
            "Protective Banner",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry PHASE_SHIFT = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "phase_shift"),
            "Phase Shift",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    -1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry BLAZING_SPEED = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "blazing_speed"),
            "Blazing Speed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6600),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry ARCTIC_REFLEX = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "arctic_reflex"),
            "Arctic Reflex",
            "Increased dodge chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry ARCANE_WARD = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "arcane_ward"),
            "Arcane Ward",
            "Absorbs damage.",
            new WizardAbsorbEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                                    2,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));

    public static Effects.Entry FIRE_WARD = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "fire_ward"),
            "Flame Ward",
            "Absorbs damage.",
            new WizardAbsorbEffect(StatusEffectCategory.BENEFICIAL, 0xff6600),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                                    2,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));

    public static Effects.Entry FROST_WARD = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "frost_ward"),
            "Frost Ward",
            "Absorbs damage.",
            new WizardAbsorbEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                                    2,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));

    public static Effects.Entry DIVINE_FAVOR = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "divine_favor"),
            "Divine Favor",
            "Guaranteed spell critical strike.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id,
                                    1,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry PAIN_SUPPRESSION = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "pain_suppression"),
            "Pain Suppression",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry CELESTIAL_ORB = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "celestial_orb"),
            "Celestial Orb",
            "Damages nearby enemies.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                    )
            )
    ));

    public static Effects.Entry SEAL_OF_CRUSADER = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "seal_of_crusader"),
            "Seal of Crusader",
            "Increased attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier()
                    )
            )
    ));
    public static Effects.Entry CRUSADERS_MARK = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "crusaders_mark"),
            "Crusader's Mark",
            "Increased damage taken",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xffcc99),
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

    public static Effects.Entry ARDENT_DEFENDER = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "ardent_defender"),
            "Ardent Defender",
            "Increases max health.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(),
                                    1,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry VITALITY = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "vitality"),
            "Vitality",
            "Increased evasion chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));


    public static Effects.Entry ENRAGE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "enrage"),
            "Enrage",
            "Increased size and attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6600),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_SCALE.getIdAsString(),
                                    0.15F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry LEECHING_STRIKE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "leeching_strike"),
            "Leeching Strike",
            "Next attack heals you.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc66),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry SIDE_STEP = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "side_step"),
            "Sidestep",
            "Increased evasion chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry CHEAT_DEATH = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "cheat_death"),
            "Cheat Death",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry TACTICAL_MANEUVER = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "tactical_maneuver"),
            "Tactical Maneuver",
            "Increased roll recharge.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "combat_roll:recharge",
                                    2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));


    public static Effects.Entry SUPERCHARGE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "supercharge"),
            "Supercharge",
            "Powerful ranged shot.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id,
                                    -0.25F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL // Total to fully half the ranged attack speed
                            )
                    )
            )
    ));

    public static Effects.Entry DEFLECTION = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "deflection"),
            "Deflection",
            "Protects you from physical attacks.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));

    // Weapon skill specific effects
    public static Effects.Entry FLURRY_TRANCE = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "flurry_trance"),
            "Flurry Trance",
            "Increased Attack Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff4400),
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
    public static Effects.Entry SHATTER = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "shatter"),
            "Shatter",
            "Reduces armor.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff6666),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ARMOR.getIdAsString(),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry PUNISHMENT = add(new Effects.Entry(Identifier.of(SkillTreeMod.NAMESPACE, "punishment"),
            "Punishment",
            "Guaranteed Critical Strike!",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc00),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "critical_strike:chance",
                                    1.0F,
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

        Protection.register(CLOAK_OF_SHADOWS.entry, new Protection.Pop(
                new ParticleBatch[]{ CLOAK_OF_SHADOWS_POP },
                SkillSounds.rogue_shadows_impact.soundEvent()
        ));
        Protection.register(DEFLECTION.entry, SpellEngineDamageTypeTags.EVADABLE, new Protection.Pop(
                new ParticleBatch[]{ new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.25F, 0.3F)
                        .color(Color.WHITE.toRGBA())
                },
                SkillSounds.archer_deflection_impact.soundEvent()
        ));
        CombatEvents.PLAYER_MELEE_ATTACK.register((event) -> {
            if (event.player().hasStatusEffect(AMBUSH.entry)) {
                event.player().removeStatusEffect(AMBUSH.entry);
            }
        });
        InstantCast.register(PRESENCE_OF_MIND.entry,
                TagKey.of(SpellRegistry.KEY, Identifier.of("wizards:arcane")));
        InstantCast.register(ARCTIC_REFLEX.entry,
                TagKey.of(SpellRegistry.KEY, Identifier.of("wizards:frost")));
    }
}
