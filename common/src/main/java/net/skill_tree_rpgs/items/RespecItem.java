package net.skill_tree_rpgs.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.skill_tree_rpgs.utils.SkillHelper;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

public class RespecItem extends Item {
    public RespecItem(Properties settings) {
        super(settings);
    }

    public static final List<ParticleGroup> RESET_PARTICLES = List.of(
        ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                .color(Color.from(0x8000ff).toRGBA())
                .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(30).speed(0.2F, 0.25F)),
            ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.DECELERATE)
                    .color(Color.from(0x8000ff).toRGBA())
                    .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F).count(30).speed(0.2F, 0.25F).invert(true))
    );

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if ((user instanceof ServerPlayer serverUser)) {
            if (SkillHelper.respec(serverUser)) {
                user.awardStat(Stats.ITEM_USED.get(this));
                var equipmentSlot = user.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                itemStack.hurtAndBreak(1, serverUser.level(), serverUser, item -> {
                    serverUser.onEquippedItemBroken(item, equipmentSlot);
                });
                ParticleHelper.sendBatches(user, RESET_PARTICLES);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
