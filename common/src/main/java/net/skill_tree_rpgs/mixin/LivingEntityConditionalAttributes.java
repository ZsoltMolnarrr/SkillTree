package net.skill_tree_rpgs.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.skill_tree_rpgs.attributes.ConditionalAttributeHolder;
import net.skill_tree_rpgs.attributes.ConditionalAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityConditionalAttributes implements ConditionalAttributeHolder {
    @Unique
    private final List<ConditionalAttributeModifier> conditionalModifiers = new ArrayList<>();

    @Override
    public List<ConditionalAttributeModifier> getConditionalModifiers() {
        return conditionalModifiers;
    }

    @Inject(method = "getEquipmentChanges", at = @At("RETURN"))
    private void onEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if (cir.getReturnValue() != null) {
            var entity = (LivingEntity) (Object) this;
            reapplyConditionalModifiers(entity);
        }
    }
}
