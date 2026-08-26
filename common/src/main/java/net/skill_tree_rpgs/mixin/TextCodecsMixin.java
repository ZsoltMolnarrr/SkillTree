package net.skill_tree_rpgs.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.skill_tree_rpgs.utils.ResolvableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComponentSerialization.class)
public class TextCodecsMixin {

	@Inject(method = "bootstrap", at = @At("TAIL"))
	private static void injectTextContent(ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ComponentContents>> idMapper, CallbackInfo ci) {
		idMapper.put(ResolvableTextContent.TYPE_ID, ResolvableTextContent.CODEC);
	}
}
