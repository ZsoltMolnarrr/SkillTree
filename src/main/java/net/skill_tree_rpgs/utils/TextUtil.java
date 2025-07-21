package net.skill_tree_rpgs.utils;

import net.minecraft.text.*;
import net.minecraft.util.Language;

import java.util.List;

public class TextUtil {
    public static String convert(List<Text> lines) {
        StringBuilder builder = new StringBuilder();
        for (Text text : lines) {
            // line.getString();

            System.out.println("attribute.name.spell_power.fire translation: " + Language.getInstance().hasTranslation("attribute.name.spell_power.fire")
            + " " + Language.getInstance().get("attribute.name.spell_power.fire"));

            var string = Text.literal("").append(text).getString();
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
