package net.skill_tree_rpgs.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.TextContent;
import net.minecraft.util.dynamic.Codecs;
import net.skill_tree_rpgs.utils.ResolvableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextCodecs.class)
public class TextCodecsMixin {

	@Inject(method = "registerTypes", at = @At("TAIL"))
	private static void injectTextContent(Codecs.IdMapper<String, MapCodec<? extends TextContent>> idMapper, CallbackInfo ci) {
		idMapper.put(ResolvableTextContent.TYPE_ID, ResolvableTextContent.CODEC);
	}
}
