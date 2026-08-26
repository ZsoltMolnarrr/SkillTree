package net.skill_tree_rpgs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.skill_tree_rpgs.attributes.ConditionalAttributeModifier;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.skill_tree_rpgs.skills.NodeTypes;
import net.spell_engine.client.gui.SpellTooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TranslationUtil {

    // Key skill Id, value spell Id
    public static final Map<String, Supplier<List<Component>>> resolvers = new HashMap<>();

    public static List<Component> resolve(String skillId) {
        var supplier = resolvers.get(skillId);
        if (supplier == null) {
            return List.of();
        }
        return supplier.get();
    }

    public static List<Component> resolveSpellDetails(Identifier spellId) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return List.of();
        }
        return SpellTooltip.spellDescriptionWithDetails(spellId, player, ItemStack.EMPTY, 0);
    }

    public static List<Component> resolveConditionalAttributeTooltip(ConditionalAttributeReward.DataStructure data) {
        var player = Minecraft.getInstance().player;
        if (player == null) return List.of();
        var conditional = data.mapped();
        var display = ItemAttributeModifiers.Display.attributeModifiers();
        var lines = new ArrayList<Component>();
        lines.add(Component.translatable(conditional.condition().translationKey()));
        display.apply(lines::add, player, conditional.attribute(), conditional.modifier());
        return lines;
    }

    public static List<Component> resolveAttributeModifierTooltip(NodeTypes.EntityAttributeReward attributeReward) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return List.of();
        }
        var display = ItemAttributeModifiers.Display.attributeModifiers();
        var bonusLines = new ArrayList<Component>();
        var modifier = attributeReward.modifier();
        display.apply(bonusLines::add, player, attributeReward.attribute(), modifier);
        return bonusLines;
    }
}
