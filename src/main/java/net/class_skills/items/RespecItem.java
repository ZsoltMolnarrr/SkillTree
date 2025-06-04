package net.class_skills.items;

import net.class_skills.utils.SkillHelper;
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

public class RespecItem extends Item {
    public RespecItem(Settings settings) {
        super(settings);
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if ((user instanceof ServerPlayerEntity serverUser)) {
            if (SkillHelper.respec(serverUser)) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                var equipmentSlot = user.getActiveHand() == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                itemStack.damage(1, ((ServerPlayerEntity) user).getServerWorld(), serverUser, item -> {
                    serverUser.sendEquipmentBreakStatus(item, equipmentSlot);
                });
                return TypedActionResult.success(itemStack, true);
            }
        }
        return TypedActionResult.fail(itemStack);
    }
}
