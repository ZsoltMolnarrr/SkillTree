package net.class_skills.client;

import net.class_skills.skills.SkillDefinitions;
import net.class_skills.skills.SkillEffects;
import net.class_skills.skills.Spells;
import net.class_skills.utils.TranslationUtil;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;

public class ClassSkillsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (var spell: Spells.all) {
            if (spell.mutator() != null) {
                SpellTooltip.addDescriptionMutator(spell.id(), spell.mutator());
            }
        }
        for (var entry: SkillDefinitions.ENTRIES) {
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
        }
        registerEffectRenderers();
    }

    private static void registerEffectRenderers() {
        final var arcaneSlowness = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPARK,
                        SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                2F, 0.15F, 0.15F)
                .preSpawnTravel(5)
                .invert()
                .color(Spells.ARCANE_COLOR);
        CustomParticleStatusEffect.register(
                SkillEffects.ARCANE_SLOWNESS.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ arcaneSlowness })
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
                .color(Spells.HOLY_COLOR);
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
                .color(Spells.HOLY_COLOR);
        CustomParticleStatusEffect.register(
                SkillEffects.INCANTER_CADENCE.effect,
                new BuffParticleSpawner(new ParticleBatch[]{ incanterParticles })
        );
    }
}
