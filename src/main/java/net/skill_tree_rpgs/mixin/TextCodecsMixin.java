package net.skill_tree_rpgs.mixin;

import net.minecraft.text.TextCodecs;
import net.minecraft.util.StringIdentifiable;
import net.skill_tree_rpgs.utils.ResolvableTextContent;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TextCodecs.class)
public class TextCodecsMixin {

	@ModifyArg(
			method = "createCodec",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/TextCodecs;dispatchingCodec([Lnet/minecraft/util/StringIdentifiable;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;"
			),
			index = 0
	)
	private static StringIdentifiable[] injectTextContent(StringIdentifiable[] types) {
		return ArrayUtils.add(types, ResolvableTextContent.TYPE);
	}
}
