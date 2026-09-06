package net.skill_tree_rpgs.utils;

import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.Texts;
import net.skill_tree_rpgs.SkillTreeMod;

import java.util.Optional;

/// A skill description that is resolved on the client, at render time, from the node's live spell /
/// attribute data (see {@link TranslationUtil}). Datagen writes it into the Skills definition JSON as
/// `{"skill_definition_id": "<node id>"}`.
///
/// **1.20.1 delta:** there is no `TextCodecs` and no `TextContent.Type`/`MapCodec` registry — text is
/// (de)serialised by the GSON-based `Text.Serializer`. So the type carries no codec of its own; the
/// JSON key is read and written by `net.skill_tree_rpgs.mixin.TextSerializerMixin` instead.
public record ResolvableTextContent(String id) implements TextContent {

	/// The single JSON key that identifies this content in a serialised `Text`.
	public static final String KEY = "skill_definition_id";

	/// Kept for parity with the 1.21 `TextContent.Type` id; not used by the 1.20.1 serializer.
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
	public String toString() {
		return "resolvable{" + id + "}";
	}
}
