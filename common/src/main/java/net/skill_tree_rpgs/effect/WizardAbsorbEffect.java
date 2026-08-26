package net.skill_tree_rpgs.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WizardAbsorbEffect extends MobEffect {
    private final int healthPerStack;

    public WizardAbsorbEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 2;
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        return entity.getAbsorptionAmount() > 0.0F;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }
}