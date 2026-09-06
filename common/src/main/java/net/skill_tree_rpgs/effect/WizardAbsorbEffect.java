package net.skill_tree_rpgs.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

/// Absorption-granting ward effect.
///
/// On 1.20.1 there is no `minecraft:generic.max_absorption` attribute (1.21+), so `onApplied` setting
/// the absorption amount directly *is* the whole mechanic — the +2 ADDITION modifier the 1.21 line
/// carried alongside it is dropped in {@link SkillEffects}.
public class WizardAbsorbEffect extends StatusEffect {
    private final int healthPerStack;

    public WizardAbsorbEffect(StatusEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 2;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // 1.20.1's applyUpdateEffect is void; the 1.21 boolean return ("keep running") has no analogue.
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }
}
