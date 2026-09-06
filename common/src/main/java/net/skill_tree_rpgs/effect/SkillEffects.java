package net.skill_tree_rpgs.effect;

import net.skill_tree_rpgs.utils.RegistryIds;
import net.skill_tree_rpgs.SkillTreeMod;
import static net.skill_tree_rpgs.skills.NodeTypes.RANGED_WEAPON_HASTE_ID;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.skills.SkillSounds;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_engine.api.event.CombatEvents;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
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

/// 1.20.1 sacrifices (all three attributes were added in 1.21 and have no living-entity equivalent here):
/// - `minecraft:generic.jump_strength` — dropped from Nature's Grasp (the -10 movement-speed modifier
///   still roots the target horizontally) and from Arcane Speed / the other speed buffs.
/// - `minecraft:generic.max_absorption` — dropped from the three Ward effects. No behaviour is lost:
///   {@link WizardAbsorbEffect#onApplied} already sets the absorption amount itself (2 per stack),
///   which is exactly what the +2 ADDITION modifier contributed on 1.21.
/// - `minecraft:generic.scale` — dropped from Enrage and Juggernaut; the size change is cosmetic and
///   1.20.1 has no attribute-driven entity scaling.
public class SkillEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Effects.Entry DIVINE_STRENGTH = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "divine_strength"),
            "Divine Strength",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry RECKLESSNESS = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "recklessness"),
            "Recklessness",
            "Increases critical strike chance, but also damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcc0000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "critical_strike:chance",
                                    1.0F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    "spell_engine:damage_taken",
                                    1.0F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FLEET_FOOTED = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "fleet_footed"),
            "Fleet Footed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x33ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ARCANE_SLOWNESS = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "arcane_slowness"),
            "Arcane Slowness",
            "Decreased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff99ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static final float FIRE_VULNERABILITY_MULTIPLIER = 0.05F;
    public static Effects.Entry FIRE_VULNERABILITY = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "fire_vulnerability"),
            "Fire Vulnerability",
            "Increased damage taken from fire.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0xff6600)
                    .setVulnerability(SpellSchools.FIRE, new SpellPower.Vulnerability(FIRE_VULNERABILITY_MULTIPLIER, 0F, 0F)),
            new EffectConfig(
                    List.of()
            )
    ));
    public static final float FROST_VULNERABILITY_MULTIPLIER = 0.1F;
    public static Effects.Entry FROST_VULNERABILITY = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "frost_vulnerability"),
            "Winter's Chill",
            "Increased damage taken from frost critical strikes.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff)
                    .setVulnerability(SpellSchools.FROST, new SpellPower.Vulnerability(0, 0F, FROST_VULNERABILITY_MULTIPLIER)),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry HEALING_FOCUS = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "healing_focus"),
            "Healing Focus",
            "Increased healing received.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ff99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.HEALING_TAKEN.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry INCANTER_CADENCE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "incanter_cadence"),
            "Incanters' Cadence",
            "Increased spell haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id,
                                    0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry REDOUBT = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "redoubt"),
            "Redoubt",
            "Increased armor.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcccccc),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ARMOR),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry KILLING_SPREE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "killing_spree"),
            "Killing Spree",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc66),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FRACTURE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "fracture"),
            "Fracture",
            "Reduces armor.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff6666),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ARMOR),
                                    -0.3F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry RHYTHM = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "rhythm"),
            "Rhythm",
            "Increased ranged attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xccff99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RANGED_WEAPON_HASTE_ID,
                                    0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry PURSUIT_OF_JUSTICE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "pursuit_of_justice"),
            "Pursuit of Justice",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ffcc),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    0.3F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry BATTLE_SHOUT = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "battle_shout"),
            "Battle Shout",
            "Increased attack power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff9933),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry NATURES_GRASP = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "natures_grasp"),
            "Nature's Grasp",
            "Immobilized.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x66ff66),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -10,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static final float ARCANE_EXPOSURE_MULTIPLIER = 0.02F;
    public static Effects.Entry ARCANE_EXPOSURE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "arcane_exposure"),
            "Arcane Exposure",
            "Increased arcane damage taken.",
            new SpellVulnerabilityStatusEffect(StatusEffectCategory.HARMFUL, 0x9999ff)
                    .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(ARCANE_EXPOSURE_MULTIPLIER, 0F, 0F)),
            new EffectConfig(
                    List.of()
            )
    ));
    public static Effects.Entry ARCANE_SPEED = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "arcane_speed"),
            "Arcane Speed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FROST_SHIELD_SPEED = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "frost_shield_speed"),
            "Frost Shield Speed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    0.5F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry CONCUSSION_BLOW = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "concussion_blow"),
            "Concussing Blow",
            "Next attack stuns.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xffcc66),
            new EffectConfig(
                    List.of()
            )
    ));

    private static ParticleGroup CLOAK_OF_SHADOWS_POP = ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.DECELERATE)
            .color(Color.from(0xcc00cc).alpha(0.5F).toRGBA())
            .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(15).speed(0.25F, 0.25F));
    public static Effects.Entry CLOAK_OF_SHADOWS = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "cloak_of_shadows"),
            "Cloak of Shadows",
            "Protects you from an attack",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x333333),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry AMBUSH = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "ambush"),
            "Ambush",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99cc66),

            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.5F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry PRESENCE_OF_MIND = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "presence_of_mind"),
            "Presence of Mind",
            "Next spell cast is instant.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            // Marker effect only — the payload is `InstantCast.register` + the item glow below,
            // no attribute modifiers. (A default-constructed `AttributeModifier` here would carry
            // an empty attribute id, which never resolves.)
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry BLIZZARD_SLOW = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "blizzard_slow"),
            "Blizzard Slow",
            "Decreased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry BANNER_PROTECTION = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "banner_protection"),
            "Protective Banner",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.3F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry PHASE_SHIFT = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "phase_shift"),
            "Phase Shift",
            "Reduces damage taken.",
            new PhaseShiftStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    -1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry BLAZING_SPEED = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "blazing_speed"),
            "Blazing Speed",
            "Increased movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6600),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    0.5F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry ARCTIC_REFLEX = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "arctic_reflex"),
            "Arctic Reflex",
            "Increased dodge chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry ARCANE_WARD = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "arcane_ward"),
            "Arcane Ward",
            "Absorbs damage.",
            new WizardAbsorbEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry FIRE_WARD = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "fire_ward"),
            "Flame Ward",
            "Absorbs damage.",
            new WizardAbsorbEffect(StatusEffectCategory.BENEFICIAL, 0xff6600),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry FROST_WARD = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "frost_ward"),
            "Frost Ward",
            "Absorbs damage.",
            new WizardAbsorbEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));

    public static Effects.Entry DIVINE_FAVOR = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "divine_favor"),
            "Divine Favor",
            "Guaranteed spell critical strike.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id,
                                    1,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry PAIN_SUPPRESSION = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "pain_suppression"),
            "Pain Suppression",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.5F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    /** Levitate (Serenity node): per-stack damage reduction while floating; 4 stacks = 80%. */
    public static Effects.Entry SERENITY = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "serenity"),
            "Serenity",
            "Reduces damage taken.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    /** Penance (Hysteria node): per-stack bonus to melee attack speed, ranged haste and spell haste. */
    public static Effects.Entry HYSTERIA = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "hysteria"),
            "Hysteria",
            "Increased attack speed, ranged and spell haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_SPEED),
                                    0.08F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    RANGED_WEAPON_HASTE_ID,
                                    0.08F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id,
                                    0.08F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    /** Penance (Chastise node): per-stack increased damage taken. */
    public static Effects.Entry CHASTISE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "chastise"),
            "Chastise",
            "Increased damage taken.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xffcc66),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry CELESTIAL_ORB = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "celestial_orb"),
            "Celestial Orb",
            "Damages nearby enemies.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc99),
            new EffectConfig(
                    List.of()
            )
    ));

    /// Paladin: critical strikes stack this (Vengeance skill node)
    public static Effects.Entry VENGEANCE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "vengeance"),
            "Vengeance",
            "Increased attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6633),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry ARDENT_DEFENDER = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "ardent_defender"),
            "Ardent Defender",
            "Increases max health.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_MAX_HEALTH),
                                    1,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry VITALITY = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "vitality"),
            "Vitality",
            "Increased evasion chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));


    public static Effects.Entry ENRAGE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "enrage"),
            "Enrage",
            "Increased size and attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6600),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_SPEED),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id.toString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    /** Rogue roll passive (Opportunist node): guaranteed critical strike for the next melee attack. */
    public static Effects.Entry OPPORTUNIST = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "opportunist"),
            "Opportunist",
            "Guaranteed Critical Strike!",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc66),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "critical_strike:chance",
                                    1.0F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    /** Shock Powder (Smoke Screen node): evasion bonus while inside the smoke cloud. */
    public static Effects.Entry SMOKE_SCREEN = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "smoke_screen"),
            "Smoke Screen",
            "Increased evasion chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x999999),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id,
                                    0.5F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry SIDE_STEP = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "side_step"),
            "Sidestep",
            "Increased evasion chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry CHEAT_DEATH = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "cheat_death"),
            "Cheat Death",
            "Reduces damage taken.",
            // Immunity-based (like Phase Shift): the custom effect refreshes a LivingEntityImmunity each
            // tick, which cancels the triggering fatal hit via SpellEngine's post-trigger isInvulnerableTo
            // re-check. The DAMAGE_TAKEN -100% attribute is kept as a redundant fallback.
            new CheatDeathStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9999ff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                            )
                    )
            )
    ));

    public static Effects.Entry TACTICAL_MANEUVER = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "tactical_maneuver"),
            "Tactical Maneuver",
            "Increased roll recharge.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "combat_roll:recharge",
                                    2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));


    public static Effects.Entry SUPERCHARGE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "supercharge"),
            "Supercharge",
            "Powerful ranged shot.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RANGED_WEAPON_HASTE_ID,
                                    -0.25F,
                                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL // Total to fully half the ranged attack speed
                            )
                    )
            )
    ));

    /** Last Stand (Juggernaut node): per-stack size growth mirroring the Last Stand stacks. */
    public static Effects.Entry JUGGERNAUT = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "juggernaut"),
            "Juggernaut",
            "Increased size.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcc6600),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "spell_engine:damage_taken",
                                    -0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    /** Last Stand (Revenge node): stacking attack speed from blocking / soaking hits. */
    public static Effects.Entry REVENGE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "revenge"),
            "Revenge",
            "Increased attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff6633),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_SPEED),
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry DEFLECTION = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "deflection"),
            "Deflection",
            "Protects you from physical attacks.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff),
            new EffectConfig(
                    List.of()
            )
    ));

    // Weapon skill specific effects
    public static Effects.Entry FLURRY_TRANCE = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "flurry_trance"),
            "Flurry Trance",
            "Increased Attack Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff4400),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry SHATTER = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "shatter"),
            "Shatter",
            "Reduces armor.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xff6666),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    RegistryIds.attribute(EntityAttributes.GENERIC_ARMOR),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry PUNISHMENT = add(new Effects.Entry(new Identifier(SkillTreeMod.NAMESPACE, "punishment"),
            "Punishment",
            "Guaranteed Critical Strike!",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffcc00),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    "critical_strike:chance",
                                    1.0F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
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
                List.of(CLOAK_OF_SHADOWS_POP),
                SkillSounds.rogue_shadows_impact.soundEvent()
        ));
        Protection.register(DEFLECTION.entry, SpellEngineDamageTypeTags.EVADABLE, new Protection.Pop(
                List.of(ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .color(Color.WHITE.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.25F, 0.3F))),
                SkillSounds.archer_deflection_impact.soundEvent()
        ));
        CombatEvents.PLAYER_MELEE_ATTACK.register((event) -> {
            if (event.player().hasStatusEffect(AMBUSH.effect)) {
                event.player().removeStatusEffect(AMBUSH.effect);
            }
        });
        InstantCast.register(PRESENCE_OF_MIND.entry,
                TagKey.of(SpellRegistry.KEY, new Identifier("wizards:arcane")));
        // Light the wielded weapon in arcane light while primed — a visible tell that the next arcane
        // cast will fire instantly. Single application (1 stack), so full opacity for a bold glow.
        GlowingItemStatusEffect.register(PRESENCE_OF_MIND.effect, Color.ARCANE, 1F);
        EntityTints.register(PHASE_SHIFT.effect, 0x66ff66ff);
        InstantCast.register(ARCTIC_REFLEX.entry,
                TagKey.of(SpellRegistry.KEY, new Identifier("wizards:frost")));
    }
}
