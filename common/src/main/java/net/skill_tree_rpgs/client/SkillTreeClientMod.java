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
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

public class SkillTreeClientMod {
    public static void init() {
        for (var spell: Skills.ENTRIES) {
            if (spell.mutator() != null) {
                SpellTooltip.addDescriptionMutator(spell.id(), spell.mutator());
            }
        }
        for (var entry: NodeTypes.ENTRIES) {
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
        final var magicSnareParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                2F, 0.15F, 0.15F)
                .preSpawnTravel(5)
                .invert();
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_SLOWNESS.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ magicSnareParticles
                        .copy().color(SkillsCommon.ARCANE_COLOR) })
        );

        final var fireVulnerability = new ParticleBatch(
                SpellEngineParticles.flame_medium_b.id().toString(),
                ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                0.1F, 0.1F, 0.15F);
        CustomParticleStatusEffect.register(
                SkillEffects.FIRE_VULNERABILITY.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ fireVulnerability })
        );

        final var frostVulnerability = new ParticleBatch(
                SpellEngineParticles.snowflake.id().toString(),
                ParticleBatch.Shape.PIPE, ParticleBatch.Origin.CENTER,
                0.1F, 0.1F, 0.15F);
        CustomParticleStatusEffect.register(
                SkillEffects.FROST_VULNERABILITY.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ frostVulnerability })
        );

        final var healingFocus = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                0.2F, 0.15F, 0.35F)
                .color(SkillsCommon.HOLY_COLOR);
        CustomParticleStatusEffect.register(
                SkillEffects.HEALING_FOCUS.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ healingFocus })
        );

        final var incanterParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                0.4F, 0.15F, 0.15F)
                .preSpawnTravel(2)
                .color(SkillsCommon.HOLY_COLOR);
        CustomParticleStatusEffect.register(
                SkillEffects.INCANTER_CADENCE.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ incanterParticles })
        );

        final var ruptureParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                2F, 0.45F, 0.75F)
                .color(Color.BLOOD.toRGBA());
        CustomParticleStatusEffect.register(
                SkillEffects.FRACTURE.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ ruptureParticles })
        );

        final var rhythmParticles = new ParticleBatch(
                SpellEngineParticles.area_circle_1.id().toString(),
                ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.FEET,
                1F, 0.05F, 0.05F)
                .color(Color.NATURE.toRGBA())
                .scale(0.75F)
                .followEntity(true);
        CustomParticleStatusEffect.register(
                SkillEffects.RHYTHM.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ rhythmParticles })
                        .scaleWithAmplifier(false)
                        .withFrequency(40)
                        .invertFrequency()
        );

        final var speedParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.STRIPE,
                        SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                0.3F, 0.05F, 0.15F)
                .extent(-0.2F);
        CustomParticleStatusEffect.register(
                SkillEffects.PURSUIT_OF_JUSTICE.effect,
                new BuffParticleSpawner(new ParticleBatch[]{
                        speedParticles.copy()
                                .color(SkillsCommon.HOLY_COLOR)
                })
        );
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_SPEED.effect,
                new BuffParticleSpawner(new ParticleBatch[]{
                        speedParticles.copy()
                                .color(SkillsCommon.ARCANE_COLOR)
                        })
        );

        final var blizzardSlowParticles = new ParticleBatch(
                SpellEngineParticles.snowflake.id().toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                1F, 0.15F, 0.15F);
        CustomParticleStatusEffect.register(
                SkillEffects.BLIZZARD_SLOW.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ blizzardSlowParticles })
        );

        final var arcaneBarrierParticles = new ParticleBatch(
                SpellEngineParticles.aura_effect_622.id().toString(),
                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                1, 0, 0)
                .scale(1.4F)
                .followEntity(true);
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_WARD.effect,
                new BuffParticleSpawner(
                        arcaneBarrierParticles.copy().color(Color.ARCANE.alpha(0.5F).toRGBA())
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        final var fireBarrierParticles = new ParticleBatch(
                SpellEngineParticles.aura_effect_716.id().toString(),
                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                1, 0, 0)
                .scale(1.4F)
                .followEntity(true);
        var fireColor = Color.from(0xff9933);
        CustomParticleStatusEffect.register(
                SkillEffects.FIRE_WARD.effect,
                new BuffParticleSpawner(
                        fireBarrierParticles.copy().color(fireColor.alpha(0.5F).toRGBA())
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        final var frostBarrierParticles = new ParticleBatch(
                SpellEngineParticles.aura_effect_691.id().toString(),
                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                1, 0, 0)
                .scale(1.4F)
                .followEntity(true);
        CustomParticleStatusEffect.register(
                SkillEffects.FROST_WARD.effect,
                new BuffParticleSpawner(
                        frostBarrierParticles.copy().color(Color.FROST.alpha(0.5F).toRGBA())
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        final var phaseShiftParticles = new ParticleBatch(
                SpellEngineParticles.aura_effect_668.id().toString(),
                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                1, 0, 0)
                .scale(1.4F)
                .followEntity(true);
        CustomParticleStatusEffect.register(
                SkillEffects.PHASE_SHIFT.effect,
                new BuffParticleSpawner(
                        phaseShiftParticles.color(Color.ARCANE.toRGBA())
                ).withFrequency(20).scaleWithAmplifier(false)
        );

        final var blazingSpeedParticles = new ParticleBatch(
                SpellEngineParticles.flame_ground.id().toString(),
                ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                1F, 0F, 0F);
        CustomParticleStatusEffect.register(
                SkillEffects.BLAZING_SPEED.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ blazingSpeedParticles })
        );
//        final var sprintParticles = new ParticleBatch(
//                SpellEngineParticles.smoke_medium.id().toString(),
//                ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
//                1F, 0F, 0F);
//        CustomParticleStatusEffect.register(
//                SkillEffects.SPRINT.effect,
//                new BuffParticleSpawner(new ParticleBatch[]{ sprintParticles })
//        );

        CustomParticleStatusEffect.register(
                SkillEffects.PAIN_SUPPRESSION.effect,
                new BuffParticleSpawner(
                        SpellBuilder.Particles.aura(SpellEngineParticles.aura_effect_619.id())
                                .scale(1.5F)
                                .color(Color.HOLY.blend(Color.WHITE, 0.5F).alpha(0.5F).toRGBA())
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        CustomModelStatusEffect.register(SkillEffects.CELESTIAL_ORB.effect, new HolyChargeEffectRenderer());

        final var sealOfCrusaderParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                1F, 0.05F, 0.1F)
                .color(Color.HOLY.toRGBA());
        CustomParticleStatusEffect.register(
                SkillEffects.SEAL_OF_CRUSADER.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ sealOfCrusaderParticles })
        );

        final var enrageParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SKULL,
                        SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                1F, 0.15F, 0.15F)
                .color(Color.RAGE.toRGBA());
        CustomParticleStatusEffect.register(
                SkillEffects.ENRAGE.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ enrageParticles })
        );

        final var cheatDeathParticles = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SKULL,
                        SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                0.5F, 0F, 0F)
                .color(RogueSkills.ROGUE_SHADOW_COLOR.toRGBA());
        CustomParticleStatusEffect.register(
                SkillEffects.CHEAT_DEATH.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ cheatDeathParticles })
        );

        CustomModelStatusEffect.register(SkillEffects.DEFLECTION.effect, new DeflectionEffectRenderer());
    }
}
