package net.skill_tree_rpgs.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.Texts;
import net.skill_tree_rpgs.SkillTreeMod;

import java.util.Optional;

public record ResolvableTextContent(String id) implements TextContent {

	public static final MapCodec<ResolvableTextContent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("skill_definition_id").forGetter(ResolvableTextContent::id)
	).apply(instance, ResolvableTextContent::new));

	/** Dispatch id under the text component's {@code type} key; registered by {@code TextCodecsMixin}. */
	public static final String TYPE_ID = SkillTreeMod.NAMESPACE + ":resolvable";

	private Text getText() {
		return Texts.join(TranslationUtil.resolve(id), Text.literal("\n"));
	}

	@Override
	public <T> Optional<T> visit(StringVisitable.Visitor<T> visitor) {
		return getText().visit(visitor);
	}

	@Override
	public <T> Optional<T> visit(StringVisitable.StyledVisitor<T> visitor, Style style) {
		return getText().visit(visitor, style);
	}

	@Override
	public MapCodec<ResolvableTextContent> getCodec() {
		return CODEC;
	}

}
