package net.skill_tree_rpgs.items;

import net.skill_tree_rpgs.utils.SkillHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RespecItem extends Item {
    public RespecItem(Settings settings) {
        super(settings);
    }

    /// 1.20.1 has no `minecraft:lore` data component, so the lore lines registered in
    /// {@link SkillItems} are appended from the item itself.
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        SkillItems.appendLore(this, tooltip);
        super.appendTooltip(stack, world, tooltip, context);
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK;
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if ((user instanceof ServerPlayerEntity serverUser)) {
            if (SkillHelper.respec(serverUser)) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                var equipmentSlot = user.getActiveHand() == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                // 1.20.1: damage(int, T extends LivingEntity, Consumer<T>) — no ServerWorld argument,
                // and `sendEquipmentBreakStatus` takes only the slot.
                itemStack.damage(1, serverUser, holder -> holder.sendEquipmentBreakStatus(equipmentSlot));
                ParticleHelper.sendBatches(user, RESET_PARTICLES);
                return TypedActionResult.success(itemStack, true);
            }
        }
        return TypedActionResult.fail(itemStack);
    }
}
