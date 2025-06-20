package net.class_skills.skills;

import net.class_skills.ClassSkillsMod;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Spells {
    public static final String NAMESPACE = ClassSkillsMod.NAMESPACE;
    public enum Category {
        ARCANE, FIRE, FROST, PRIEST, PALADIN, ROGUE, WARRIOR, ARCHER
    }
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator, EnumSet<Category> categories) {
        public Entry(Identifier id, Spell spell, String title, String description,
                     @Nullable SpellTooltip.DescriptionMutator mutator, Category category) {
            this(id, spell, title, description, mutator, EnumSet.of(category));
        }
        public String key() {
            return id.getPath();
        }
    }

    public static final List<Entry> all = new ArrayList<>();
    private static Entry add(Entry entry) {
        all.add(entry);
        return entry;
    }

    public static final Entry arcane_spec_a_modifier_1 = add(arcane_spec_a_modifier_1());
    private static Entry arcane_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_a_modifier_1");
        var title = "Conjured Arcane Charge";
        var description = "Increases the maximum number of Arcane Charges by {effect_amplifier_cap_add}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry arcane_spec_b_modifier_1 = add(arcane_spec_b_modifier_1());
    private static Entry arcane_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "arcane_spec_b_modifier_1");
        var title = "Arcane Endurance";
        var description = "Increases the duration of Arcane Charges by {effect_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.ARCANE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:arcane_blast";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.ARCANE));
    }

    public static final Entry fire_spec_a_modifier_1 = add(fire_spec_a_modifier_1());
    private static Entry fire_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_spec_a_modifier_1");
        var title = "Blast Radius";

        var bonus = 0.5F;

        var description = "Increases the area of effect of Pyroblast by {bonus}.";
        var mutator = new SpellTooltip.DescriptionMutator() {
            @Override
            public String mutate(Args args) {
                return args.description().replace("{bonus}", SpellTooltip.percent(bonus));
            }
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_blast";
        modifier.replacing_area_impact = new Spell.AreaImpact();
        modifier.replacing_area_impact.radius = 2.5F * (1F + bonus);
        modifier.replacing_area_impact.area = new Spell.Target.Area();
        modifier.replacing_area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;

        var particleBatch = new ParticleBatch();
        particleBatch.particle_id = SpellEngineParticles.fire_explosion.id().toString();
        particleBatch.shape = ParticleBatch.Shape.SPHERE;
        particleBatch.origin = ParticleBatch.Origin.CENTER;
        particleBatch.count = 1;
        particleBatch.min_speed = 0;
        particleBatch.max_speed = 0;
        particleBatch.scale = 1F + bonus * 0.6F; // Scale up by bonus
        modifier.replacing_area_impact.particles = new ParticleBatch[]{ particleBatch };

        modifier.replacing_area_impact.sound = new Sound("wizards:fireball_impact");

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, EnumSet.of(Category.FIRE));
    }

    public static final Entry fire_spec_b_modifier_1 = add(fire_spec_b_modifier_1());
    private static Entry fire_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "fire_spec_b_modifier_1");
        var title = "Blast Punch";
        var description = "Increases the knockback of Pyroblast by {knockback_multiply_base}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FIRE;

        var bonus = 0.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:fire_blast";
        modifier.knockback_multiply_base = bonus;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FIRE));
    }

    public static final Entry frost_spec_a_modifier_1 = add(frost_spec_a_modifier_1());
    private static Entry frost_spec_a_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_spec_a_modifier_1");
        var title = "Frost Bounce";
        var description = "Frostbolt ricochets to {ricochet} additional target.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.projectile_perks = Spell.ProjectileData.Perks.EMPTY();
        modifier.projectile_perks.ricochet = 1;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }

    public static final Entry frost_spec_b_modifier_1 = add(frost_spec_b_modifier_1());
    private static Entry frost_spec_b_modifier_1() {
        var id = Identifier.of(NAMESPACE, "frost_spec_b_modifier_1");
        var title = "Lingering Chill";
        var description = "Frostbolt slow effect lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.FROST;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "wizards:frostbolt";
        modifier.effect_duration_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null, EnumSet.of(Category.FROST));
    }
}
