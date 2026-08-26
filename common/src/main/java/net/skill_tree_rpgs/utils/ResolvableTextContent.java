package net.skill_tree_rpgs.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.skill_tree_rpgs.SkillTreeMod;

import java.util.Optional;

public record ResolvableTextContent(String id) implements ComponentContents {

	public static final MapCodec<ResolvableTextContent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("skill_definition_id").forGetter(ResolvableTextContent::id)
	).apply(instance, ResolvableTextContent::new));

	/** Dispatch id under the text component's {@code type} key; registered by {@code TextCodecsMixin}. */
	public static final String TYPE_ID = SkillTreeMod.NAMESPACE + ":resolvable";

	private Component getText() {
		return ComponentUtils.formatList(TranslationUtil.resolve(id), Component.literal("\n"));
	}

	@Override
	public <T> Optional<T> visit(FormattedText.ContentConsumer<T> visitor) {
		return getText().visit(visitor);
	}

	@Override
	public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> visitor, Style style) {
		return getText().visit(visitor, style);
	}

	@Override
	public MapCodec<ResolvableTextContent> codec() {
		return CODEC;
	}

}
