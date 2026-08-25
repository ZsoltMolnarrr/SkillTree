package net.skill_tree_rpgs.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.spell_engine.api.effect.CustomStatusEffect;
import net.spell_engine.api.entity.LivingEntityImmunity;

/// Cheat Death briefly makes the bearer invulnerable after a would-be fatal blow. Like Phase Shift it
/// holds a {@link LivingEntityImmunity} (refreshed every tick) so it refuses all incoming damage and
/// any harmful status effect for its duration — but its purpose is reactive: the fatal-damage trigger
/// applies it mid-`damage()`, and SpellEngine re-checks `isInvulnerableTo` after that trigger fires
/// (LivingEntityHealthImpacting), so the immunity granted here also cancels the very hit that would
/// have killed the bearer, not merely the follow-up hits within the window.
public class CheatDeathStatusEffect extends CustomStatusEffect {
    /// Ticks of immunity granted per refresh — a small buffer re-applied every tick the effect runs, so
    /// it always outlives the gap to the next tick yet expires promptly once Cheat Death itself fades.
    private static final int IMMUNITY_TICKS = 5;

    public CheatDeathStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        // Grant immunity SYNCHRONOUSLY the instant the effect is applied. This is what makes reactive
        // death-cheating work: the fatal-damage trigger applies this effect part-way through the
        // victim's `damage()` call, and SpellEngine's `isInvulnerableTo` re-check runs later in that
        // SAME call. `applyUpdateEffect` only runs on the next status-effect tick — too late to cancel
        // the triggering blow — so the immunity has to be in place here, at application time.
        grantImmunity(entity);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // refresh the immunity every tick, so it tracks the effect's real (data-driven) duration
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        grantImmunity(entity);
        return true; // keep the normal lifecycle
    }

    private static void grantImmunity(LivingEntity entity) {
        // Server-authoritative; effect application and damage checks resolve server-side.
        if (!entity.getEntityWorld().isClient()) {
            LivingEntityImmunity.apply(entity, null, null, null, true, IMMUNITY_TICKS);
        }
    }
}
