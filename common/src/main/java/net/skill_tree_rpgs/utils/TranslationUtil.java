package net.skill_tree_rpgs.utils;

import net.skill_tree_rpgs.attributes.ConditionalAttributeModifier;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.skill_tree_rpgs.skills.NodeTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.mixin.client.ItemStackTooltipAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TranslationUtil {

    // Key skill Id, value spell Id
    public static final Map<String, Supplier<List<Text>>> resolvers = new HashMap<>();

    public static List<Text> resolve(String skillId) {
        var supplier = resolvers.get(skillId);
        if (supplier == null) {
            return List.of();
        }
        return supplier.get();
    }

    public static List<Text> resolveSpellDetails(Identifier spellId) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) {
            return List.of();
        }
        return SpellTooltip.spellDescriptionWithDetails(spellId, player, ItemStack.EMPTY, 0);
    }

    public static List<Text> resolveConditionalAttributeTooltip(ConditionalAttributeReward.DataStructure data) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) return List.of();
        var conditional = data.mapped();
        var tooltipUtil = (ItemStackTooltipAccessor) (Object) ItemStack.EMPTY;
        var lines = new ArrayList<Text>();
        lines.add(Text.translatable(conditional.condition().translationKey()));
        tooltipUtil.spellEngine_appendAttributeModifierTooltip(
                lines::add, player, conditional.attribute(), conditional.modifier());
        return lines;
    }

    public static List<Text> resolveAttributeModifierTooltip(NodeTypes.EntityAttributeReward attributeReward) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) {
            return List.of();
        }
        var tooltipUtil = (ItemStackTooltipAccessor) (Object) ItemStack.EMPTY;
        var bonusLines = new ArrayList<Text>();
        var modifier = attributeReward.modifier();
        tooltipUtil
                .spellEngine_appendAttributeModifierTooltip(
                        bonusLines::add,
                        player,
                        attributeReward.attribute(),
                        modifier
                );
        return bonusLines;
    }
}
