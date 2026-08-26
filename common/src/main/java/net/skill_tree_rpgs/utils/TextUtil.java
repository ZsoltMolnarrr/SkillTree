package net.skill_tree_rpgs.utils;

import java.util.List;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public class TextUtil {
    public static String convert(List<Component> lines) {
        StringBuilder builder = new StringBuilder();
        for (Component text : lines) {
            // line.getString();

            System.out.println("attribute.name.spell_power.fire translation: " + Language.getInstance().has("attribute.name.spell_power.fire")
            + " " + Language.getInstance().getOrDefault("attribute.name.spell_power.fire"));

            var string = Component.literal("").append(text).getString();
            if (!string.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }
                builder.append(string);
            }
        }
        return builder.toString();
    }
}
