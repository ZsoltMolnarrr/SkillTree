package net.skill_tree_rpgs.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.skill_tree_rpgs.utils.ResolvableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

/// Teaches vanilla's GSON text (de)serialiser about {@link ResolvableTextContent}.
///
/// On 1.21 this was a `TextCodecs` `@ModifyArg` that appended a `TextContent.Type` to the dispatching
/// codec's type array. 1.20.1 has no codec-based text serialisation at all — `Text.Serializer` is a
/// hand-written `JsonSerializer`/`JsonDeserializer` pair with an if-else chain over the known content
/// classes, which throws `Don't know how to serialize … as a Component` for anything else. So both
/// directions are short-circuited here instead:
///
/// - **deserialize**: a JSON object carrying only `skill_definition_id` becomes a resolvable text.
/// - **serialize**: a resolvable text becomes `{"skill_definition_id": "<id>"}`.
///
/// This is what lets Pufferfish's Skills read the generated `definitions.json` descriptions and send
/// them to the client, where the content resolves against the live spell/attribute data.
@Mixin(Text.Serializer.class)
public class TextSerializerMixin {

    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;",
            at = @At("HEAD"), cancellable = true)
    private void deserialize_HEAD_SkillTreeRPGs(JsonElement element, Type type, JsonDeserializationContext context,
                                                CallbackInfoReturnable<MutableText> cir) {
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject object = element.getAsJsonObject();
        var value = object.get(ResolvableTextContent.KEY);
        if (value == null || !value.isJsonPrimitive()) {
            return;
        }
        cir.setReturnValue(MutableText.of(new ResolvableTextContent(value.getAsString())));
    }

    @Inject(method = "serialize(Lnet/minecraft/text/Text;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;",
            at = @At("HEAD"), cancellable = true)
    private void serialize_HEAD_SkillTreeRPGs(Text text, Type type, JsonSerializationContext context,
                                              CallbackInfoReturnable<JsonElement> cir) {
        if (!(text.getContent() instanceof ResolvableTextContent resolvable)) {
            return;
        }
        JsonObject object = new JsonObject();
        object.addProperty(ResolvableTextContent.KEY, resolvable.id());
        cir.setReturnValue(object);
    }
}
