package net.skill_tree_rpgs.node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.reward.Reward;
import net.puffish.skillsmod.api.reward.RewardConfigContext;
import net.puffish.skillsmod.api.reward.RewardDisposeContext;
import net.puffish.skillsmod.api.reward.RewardUpdateContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.attributes.ConditionalAttributeHolder;
import net.skill_tree_rpgs.attributes.ConditionalAttributeModifier;
import net.skill_tree_rpgs.attributes.ModifierCondition;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ConditionalAttributeReward implements Reward {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "conditional_attribute");

    public static void register() {
        SkillsAPI.registerReward(ID, ConditionalAttributeReward::parse);
    }

    private static final Gson gson = new GsonBuilder().create();

    public record DataStructure(String attribute, String fallbackAttribute, double value, String operation, ConditionData condition) {
        public record ConditionData(String translationKey, EquipmentData equipment) {
            public record EquipmentData(String slot, String tag) {}
        }

        public @NotNull ConditionalAttributeModifier mapped() {
            var parsed = this;
            var effectiveEntry = BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(parsed.attribute()))
                    .orElseGet(() -> {
                        if (parsed.fallbackAttribute() == null) {
                            throw new IllegalArgumentException("Unknown attribute: " + parsed.attribute());
                        }
                        return BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(parsed.fallbackAttribute()))
                                .orElseThrow(() -> new IllegalArgumentException("Unknown fallback attribute: " + parsed.fallbackAttribute()));
                    });

            var operation = parseOperation(parsed.operation());

            var equipment = parsed.condition().equipment();
            var slot = parseEquipmentSlot(equipment.slot());
            var tag = TagKey.create(net.minecraft.core.registries.Registries.ITEM, Identifier.parse(equipment.tag()));

            var modifierId = Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, UUID.randomUUID().toString().replace("-", ""));
            var modifier = new AttributeModifier(modifierId, parsed.value(), operation);
            var condition = new ModifierCondition(new ModifierCondition.Equipment(slot, tag), parsed.condition().translationKey());

            return new ConditionalAttributeModifier(modifierId, effectiveEntry, modifier, condition);
        }
    }

    private ConditionalAttributeModifier conditionalModifier;

    private static Result<ConditionalAttributeReward, Problem> parse(RewardConfigContext context) {
        var dataResult = context.getData();
        if (dataResult.getFailure().isPresent()) {
            return Result.failure(dataResult.getFailure().get());
        }
        var data = dataResult.getSuccess();
        var reward = new ConditionalAttributeReward();
        try {
            var json = data.get().getJson();
            var parsed = gson.fromJson(json, DataStructure.class);
            reward.conditionalModifier = parsed.mapped();
        } catch (Exception e) {
            return Result.failure(Problem.message("Failed to parse conditional attribute reward: " + e.getMessage()));
        }
        return Result.success(reward);
    }

    private static AttributeModifier.Operation parseOperation(String op) {
        return switch (op) {
            case "addition" -> AttributeModifier.Operation.ADD_VALUE;
            case "multiply_base" -> AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            case "multiply_total" -> AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            default -> throw new IllegalArgumentException("Unknown operation: " + op);
        };
    }

    private static EquipmentSlot parseEquipmentSlot(String name) {
        for (var slot : EquipmentSlot.values()) {
            if (slot.getName().equals(name)) return slot;
        }
        throw new IllegalArgumentException("Unknown equipment slot: " + name);
    }

    @Override
    public void update(RewardUpdateContext context) {
        var player = context.getPlayer();
        var holder = (ConditionalAttributeHolder) player;
        holder.removeConditionalModifier(conditionalModifier.id());
        var instance = player.getAttribute(conditionalModifier.attribute());
        if (instance != null) {
            instance.removeModifier(conditionalModifier.modifier().id());
        }
        if (context.getCount() > 0) {
            holder.addConditionalModifier(conditionalModifier);
            holder.reapplyConditionalModifiers(player);
        }
    }

    @Override
    public void dispose(RewardDisposeContext context) {
        for (var player : context.getServer().getPlayerList().getPlayers()) {
            var holder = (ConditionalAttributeHolder) player;
            holder.removeConditionalModifier(conditionalModifier.id());
            var instance = player.getAttribute(conditionalModifier.attribute());
            if (instance != null) {
                instance.removeModifier(conditionalModifier.modifier().id());
            }
        }
    }
}
