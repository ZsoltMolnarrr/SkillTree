package net.class_skills.skills;

import net.class_skills.ClassSkillsMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;

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
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xff9900),
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
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ff00),
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
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x0000ff),
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

    public static void register(ConfigFile.Effects config) {
        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
        }

        Effects.register(entries, config.effects);
    }
}
