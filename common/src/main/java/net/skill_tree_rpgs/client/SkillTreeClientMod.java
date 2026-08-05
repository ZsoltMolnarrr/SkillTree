package net.skill_tree_rpgs.client;

import net.skill_tree_rpgs.client.effect.DeflectionEffectRenderer;
import net.skill_tree_rpgs.client.effect.HolyChargeEffectRenderer;
import net.skill_tree_rpgs.skills.RogueSkills;
import net.skill_tree_rpgs.skills.SkillsCommon;
import net.skill_tree_rpgs.skills.NodeTypes;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.skill_tree_rpgs.skills.Skills;
import net.skill_tree_rpgs.utils.TranslationUtil;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

public class SkillTreeClientMod {
    public static void init() {
        for (var spell: Skills.ENTRIES) {
            if (spell.mutator() != null) {
                SpellTooltip.addDescriptionMutator(spell.id(), spell.mutator());
            }
        }
        for (var entry: NodeTypes.allNodes()) {
            var skillId = entry.id();
            if (entry.spellReward() != null) {
                var container = entry.spellReward().get(0);
                var id = Identifier.of(container.spell_ids().getFirst());
                TranslationUtil.resolvers.put(skillId, () -> TranslationUtil.resolveSpellDetails(id));
            }
            else if (entry.attributeReward() != null) {
                var attribute = entry.attributeReward();
                TranslationUtil.resolvers.put(skillId, () -> TranslationUtil.resolveAttributeModifierTooltip(attribute));
            }
            else if (entry.conditionalAttributeReward() != null) {
                var conditional = entry.conditionalAttributeReward();
                TranslationUtil.resolvers.put(skillId, () -> TranslationUtil.resolveConditionalAttributeTooltip(conditional));
            }
        }
        registerEffectRenderers();
    }

    private static void registerEffectRenderers() {
        // Arcing sparks in the warrior RAGE color, mirroring Wizards' Evocation look.
        CustomParticleStatusEffect.register(
                SkillEffects.RECKLESSNESS.effect,
                new BuffParticleSpawner(
                        ParticleGroupBuilder.of(SpellEngineParticles.lightning_arc_A)
                                .color(Color.RAGE.toRGBA())
                                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1F).speed(0.05F, 0.1F).extent(0.5F)),
                        ParticleGroupBuilder.of(SpellEngineParticles.lightning_arc_B)
                                .color(Color.RAGE.toRGBA())
                                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1F).speed(0.05F, 0.1F).extent(0.5F))
                ).withFrequency(4)
        );

        final var magicSnareParticles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(2F).speed(0.15F, 0.15F).verticalOrigin(Batches.FEET).preTravel(5).invert(true));
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_SLOWNESS.effect,
                new BuffParticleSpawner(magicSnareParticles
                        .copy()
.appearance(a -> a.color(SkillsCommon.ARCANE_COLOR)))
        );

        final var fireVulnerability = ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(0.1F).speed(0.1F, 0.15F).verticalOrigin(Batches.FEET));
        CustomParticleStatusEffect.register(
                SkillEffects.FIRE_VULNERABILITY.effect,
                new BuffParticleSpawner(fireVulnerability)
        );

        final var frostVulnerability = ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).count(0.1F).speed(0.1F, 0.15F));
        CustomParticleStatusEffect.register(
                SkillEffects.FROST_VULNERABILITY.effect,
                new BuffParticleSpawner(frostVulnerability)
        );

        final var healingFocus = ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                .color(SkillsCommon.HOLY_COLOR)
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(0.2F).speed(0.15F, 0.35F).verticalOrigin(Batches.FEET));
        CustomParticleStatusEffect.register(
                SkillEffects.HEALING_FOCUS.effect,
                new BuffParticleSpawner(healingFocus)
        );

        final var incanterParticles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                .color(SkillsCommon.HOLY_COLOR)
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(0.4F).speed(0.15F, 0.15F).preTravel(2));
        CustomParticleStatusEffect.register(
                SkillEffects.INCANTER_CADENCE.effect,
                new BuffParticleSpawner(incanterParticles)
        );

        final var ruptureParticles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST)
                .color(Color.BLOOD.toRGBA())
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(2F).speed(0.45F, 0.75F));
        CustomParticleStatusEffect.register(
                SkillEffects.FRACTURE.effect,
                new BuffParticleSpawner(ruptureParticles)
        );

        final var rhythmParticles = ParticleGroupBuilder.of(SpellEngineParticles.area_circle_1)
                .color(Color.NATURE.toRGBA())
                .scale(0.75F)
                .attached()
                .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL).count(1F).speed(0.05F, 0.05F).verticalOrigin(Batches.FEET));
        CustomParticleStatusEffect.register(
                SkillEffects.RHYTHM.effect,
                new BuffParticleSpawner(rhythmParticles)
                        .scaleWithAmplifier(false)
                        .withFrequency(40)
                        .invertFrequency()
        );

        final var speedParticles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.FLOAT)
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(0.3F).speed(0.05F, 0.15F).verticalOrigin(Batches.FEET).extent(-0.2F));
        CustomParticleStatusEffect.register(
                SkillEffects.PURSUIT_OF_JUSTICE.effect,
                new BuffParticleSpawner(speedParticles.copy()
.appearance(a -> a.color(SkillsCommon.HOLY_COLOR)))
        );
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_SPEED.effect,
                new BuffParticleSpawner(speedParticles.copy()
.appearance(a -> a.color(SkillsCommon.ARCANE_COLOR)))
        );

        final var blizzardSlowParticles = ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(1F).speed(0.15F, 0.15F).verticalOrigin(Batches.FEET));
        CustomParticleStatusEffect.register(
                SkillEffects.BLIZZARD_SLOW.effect,
                new BuffParticleSpawner(blizzardSlowParticles)
        );

        final var arcaneBarrierParticles = ParticleGroupBuilder.of(SpellEngineParticles.area_effect_622)
                .facing(ParticleGroup.Facing.CAMERA)
                .scale(1.4F)
                .attached()
                .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1));
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_WARD.effect,
                new BuffParticleSpawner(
                        arcaneBarrierParticles.copy()
.appearance(a -> a.color(Color.ARCANE.alpha(0.5F).toRGBA()))
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        final var fireBarrierParticles = ParticleGroupBuilder.of(SpellEngineParticles.area_effect_716)
                .facing(ParticleGroup.Facing.CAMERA)
                .scale(1.4F)
                .attached()
                .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1));
        var fireColor = Color.from(0xff9933);
        CustomParticleStatusEffect.register(
                SkillEffects.FIRE_WARD.effect,
                new BuffParticleSpawner(
                        fireBarrierParticles.copy()
.appearance(a -> a.color(fireColor.alpha(0.5F).toRGBA()))
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        final var frostBarrierParticles = ParticleGroupBuilder.of(SpellEngineParticles.area_effect_691)
                .facing(ParticleGroup.Facing.CAMERA)
                .scale(1.4F)
                .attached()
                .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1));
        CustomParticleStatusEffect.register(
                SkillEffects.FROST_WARD.effect,
                new BuffParticleSpawner(
                        frostBarrierParticles.copy()
.appearance(a -> a.color(Color.FROST.alpha(0.5F).toRGBA()))
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        final var phaseShiftParticles = ParticleGroupBuilder.of(SpellEngineParticles.area_effect_668)
                .facing(ParticleGroup.Facing.CAMERA)
                .scale(1.4F)
                .attached()
                .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1));
        CustomParticleStatusEffect.register(
                SkillEffects.PHASE_SHIFT.effect,
                new BuffParticleSpawner(
                        phaseShiftParticles.copy().appearance(a -> a.color(Color.ARCANE.toRGBA()))
                ).withFrequency(20).scaleWithAmplifier(false)
        );

        final var blazingSpeedParticles = ParticleGroupBuilder.of(SpellEngineParticles.flame_ground)
                .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(1F).verticalOrigin(Batches.FEET));
        CustomParticleStatusEffect.register(
                SkillEffects.BLAZING_SPEED.effect,
                new BuffParticleSpawner(blazingSpeedParticles)
        );
//        final var sprintParticles = ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
//                .batch(b -> b.shape(ParticleGroup.Shape.PILLAR).count(1F).verticalOrigin(Batches.FEET));
//        CustomParticleStatusEffect.register(
//                SkillEffects.SPRINT.effect,
//                new BuffParticleSpawner(sprintParticles)
//        );

        CustomParticleStatusEffect.register(
                SkillEffects.PAIN_SUPPRESSION.effect,
                new BuffParticleSpawner(
                        SpellBuilder.Particles.aura(SpellEngineParticles.area_effect_619.id())
                                .appearance(a -> a.scale(1.5F).color(Color.HOLY.blend(Color.WHITE, 0.5F).alpha(0.5F).toRGBA()))
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        CustomModelStatusEffect.register(SkillEffects.CELESTIAL_ORB.effect, new HolyChargeEffectRenderer());

        // Vengeance: no per-entity buff particles — just a ground decal pulsing under the
        // holder every second, in the effect's own ember color.
        CustomParticleStatusEffect.register(
                SkillEffects.VENGEANCE.effect,
                new BuffParticleSpawner()
                        .withGroundEffect(
                                SpellEngineParticles.area_effect_307.id().toString(),
                                SkillsCommon.MIGHT_COLOR,
                                20)
        );

        final var enrageParticles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.BURST)
                .color(Color.RAGE.toRGBA())
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(1F).speed(0.15F, 0.15F));
        CustomParticleStatusEffect.register(
                SkillEffects.ENRAGE.effect,
                new BuffParticleSpawner(enrageParticles)
        );

        final var cheatDeathParticles = ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.FLOAT)
                .color(RogueSkills.ROGUE_SHADOW_COLOR.toRGBA())
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(0.5F));
        CustomParticleStatusEffect.register(
                SkillEffects.CHEAT_DEATH.effect,
                new BuffParticleSpawner(cheatDeathParticles)
        );

        CustomModelStatusEffect.register(SkillEffects.DEFLECTION.effect, new DeflectionEffectRenderer());
    }
}
