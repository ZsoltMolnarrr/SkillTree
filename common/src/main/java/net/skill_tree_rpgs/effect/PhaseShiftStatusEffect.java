package net.skill_tree_rpgs.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.spell_engine.api.effect.CustomStatusEffect;
import net.spell_engine.api.entity.LivingEntityImmunity;

/// Phase Shift phases the caster briefly out of harm's way. On top of the damage reduction from its
/// attribute modifier, it holds a {@link LivingEntityImmunity} that also refuses any harmful status
/// effect while the shift lasts — so nothing can poison, slow or otherwise debuff the caster through it.
public class PhaseShiftStatusEffect extends CustomStatusEffect {
    /// Ticks of immunity granted per refresh — a small buffer re-applied every tick the effect runs, so
    /// it always outlives the gap to the next tick yet expires promptly once Phase Shift itself fades.
    private static final int IMMUNITY_TICKS = 5;

    public PhaseShiftStatusEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // refresh the immunity every tick, so it tracks the effect's real (data-driven) duration
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        // Server-authoritative; `applyUpdateEffect` only runs server-side now.
        LivingEntityImmunity.apply(entity, null, null, null, true, IMMUNITY_TICKS);
        return true; // keep the normal lifecycle
    }
}
