package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

public class SkillsCommon {
    public static final float WIZARD_WARD_CHANCE = 0.25F;
    public static final float WIZARD_WARD_DURATION = 8F;
    public static final long ARCANE_COLOR = Color.from(SpellSchools.ARCANE.color).toRGBA();
    public static final long FROST_COLOR = Color.from(SpellSchools.FROST.color).toRGBA();
    public static final long HOLY_COLOR = Color.HOLY.toRGBA();
    public static final Color MIGHT_COLOR = Color.from(0xccffff);

    public static Spell createModifierAlikePassiveSpell() {
        var spell = SpellBuilder.createSpellPassive();
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        return spell;
    }

    public static Fx.Visuals poisonImpactParticles() {
        return Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST)
                        .color(Color.POISON_MID.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.5F, 0.8F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST)
                        .color(Color.POISON_DARK.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(10).speed(0.5F, 0.8F))
        );
    }

    public static Fx.Visuals leechImpactParticles() {
        return Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.FLOAT)
                        .color(Color.BLOOD.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(15).speed(0.02F, 0.1F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                        .attached()
                        .color(Color.BLOOD.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(25).speed(0.08F, 0.12F).invert(true).preTravel(5)),
                ParticleGroupBuilder.of(SpellEngineParticles.ground_glow)
                        .attachedToGround()
                        .scale(0.8F)
                        .color(Color.BLOOD.alpha(0.2F).toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.LINE_VERTICAL).count(1).anchor(ParticleGroup.Anchor.GROUND))
        );
    }

    public static void explosionImpact(Spell spell, float coefficient) {
        var impact = SpellBuilder.Impacts.damage(coefficient, 0.2F);
        spell.area_impact = SpellBuilder.Complex.fireExplosion(2.5F);
        spell.impacts = List.of(impact);
    }

    // MARK: Class-spell weak root nodes
    //
    // Every book spell's node cluster starts with a weak "root" modifier leading to the two
    // powerful mutex nodes. Roots draw from this shared palette of patterns; pick the one
    // matching what the spell already does (see each helper's fit note). Descriptions rely
    // on SpellTooltip auto-tokens resolved from the modifier at runtime, unless noted.

    /** Generic root builder for one-off patterns; prefer the named helpers below. */
    public static Skills.Entry spellRoot(Skills.Category category, SpellSchool school,
                                         String path, String spellPattern, String spellName,
                                         String description, Consumer<Spell.Modifier> configure) {
        var id = Identifier.of(Skills.NAMESPACE, path);
        var spell = SpellBuilder.createSpellModifier();
        spell.school = school;
        var modifier = new Spell.Modifier();
        modifier.spell_pattern = spellPattern;
        configure.accept(modifier);
        spell.modifiers = List.of(modifier);
        return new Skills.Entry(id, spell, "Improved " + spellName, description, null, EnumSet.of(category));
    }

    /** Burst damage spells. */
    public static Skills.Entry critRoot(Skills.Category category, SpellSchool school,
                                        String path, String spellPattern, String spellName, float chance) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " has {critical_chance_bonus} increased critical strike chance.",
                modifier -> {
                    modifier.power_modifier = new Spell.Impact.Modifier();
                    modifier.power_modifier.critical_chance_bonus = chance;
                });
    }

    /** Finisher/assassination spells, where landing a crit is the point. */
    public static Skills.Entry critDamageRoot(Skills.Category category, SpellSchool school,
                                              String path, String spellPattern, String spellName, float bonus) {
        return spellRoot(category, school, path, spellPattern, spellName,
                "Critical strikes of " + spellName + " deal {critical_damage_bonus} increased damage.",
                modifier -> {
                    modifier.power_modifier = new Spell.Impact.Modifier();
                    modifier.power_modifier.critical_damage_bonus = bonus;
                });
    }

    /** Long-cooldown utility, mobility and panic buttons. */
    public static Skills.Entry cooldownRoot(Skills.Category category, SpellSchool school,
                                            String path, String spellPattern, String spellName, float seconds) {
        return spellRoot(category, school, path, spellPattern, spellName,
                "Reduces the cooldown of " + spellName + " by {cooldown_duration_deduct} sec.",
                modifier -> modifier.cooldown_duration_deduct = seconds);
    }

    /** Heals and steady damage, where output matters more than spikes. */
    public static Skills.Entry powerRoot(Skills.Category category, SpellSchool school,
                                         String path, String spellPattern, String spellName, float multiplier) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " power increased by {power_multiplier}.",
                modifier -> {
                    modifier.power_modifier = new Spell.Impact.Modifier();
                    modifier.power_modifier.power_multiplier = multiplier;
                });
    }

    /** Projectiles, beams and dashes: longer reach. */
    public static Skills.Entry reachRoot(Skills.Category category, SpellSchool school,
                                         String path, String spellPattern, String spellName, float blocks) {
        return spellRoot(category, school, path, spellPattern, spellName,
                "Range of " + spellName + " increased by {range_add} blocks.",
                modifier -> modifier.range_add = blocks);
    }

    /** Blinks and dashes: teleports the caster further. Only affects TELEPORT impacts in FORWARD mode. */
    public static Skills.Entry teleportRoot(Skills.Category category, SpellSchool school,
                                            String path, String spellPattern, String spellName, float blocks) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " teleports {teleport_distance_add} blocks further.",
                modifier -> modifier.teleport_distance_add = blocks);
    }

    /** Area spells centered on the caster or target: wider area. */
    public static Skills.Entry radiusRoot(Skills.Category category, SpellSchool school,
                                          String path, String spellPattern, String spellName, float blocks) {
        return spellRoot(category, school, path, spellPattern, spellName,
                "Radius of " + spellName + " increased by {range_add} blocks.",
                modifier -> modifier.range_add = blocks);
    }

    /** Spells whose value is a status effect (buffs, slows, DoT debuffs). */
    public static Skills.Entry lingerRoot(Skills.Category category, SpellSchool school,
                                          String path, String spellPattern, String spellName, float seconds) {
        return spellRoot(category, school, path, spellPattern, spellName,
                "Effects applied by " + spellName + " last {effect_duration_add} sec longer.",
                modifier -> modifier.effect_duration_add = seconds);
    }

    /** Ground effects and clouds (walls, traps, banners). */
    public static Skills.Entry fieldRoot(Skills.Category category, SpellSchool school,
                                         String path, String spellPattern, String spellName, float seconds) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " persists {spawn_duration_add} sec longer.",
                modifier -> modifier.spawn_duration_add = seconds);
    }

    /** Channeled spells: more releases per cast. */
    public static Skills.Entry channelRoot(Skills.Category category, SpellSchool school,
                                           String path, String spellPattern, String spellName, int releases) {
        return spellRoot(category, school, path, spellPattern, spellName,
                "Channeling " + spellName + " releases {channel_ticks_add} additional times.",
                modifier -> modifier.channel_ticks_add = releases);
    }

    /** Summons: longer lifetime. No auto-token for lifespan, so the number is baked in. */
    public static Skills.Entry companionRoot(Skills.Category category, SpellSchool school,
                                             String path, String spellPattern, String spellName, int seconds) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " lasts " + seconds + " sec longer.",
                modifier -> modifier.summon_behaviour.lifespan.active_seconds_add = seconds);
    }

    /** Physical skillshots: bigger projectile, easier to land. No auto-token, number baked in. */
    public static Skills.Entry heftRoot(Skills.Category category, SpellSchool school,
                                        String path, String spellPattern, String spellName, float scale) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " projectile is " + Math.round(scale * 100) + "% larger.",
                modifier -> modifier.projectile_scale_multiply = scale);
    }

    /** MELEE delivery skills, whose damage comes from the swing rather than impacts. */
    public static Skills.Entry meleeRoot(Skills.Category category, SpellSchool school,
                                         String path, String spellPattern, String spellName, float multiplier) {
        return spellRoot(category, school, path, spellPattern, spellName,
                spellName + " deals {melee_damage_multiplier} increased damage.",
                modifier -> modifier.melee_damage_multiplier = multiplier);
    }
}
