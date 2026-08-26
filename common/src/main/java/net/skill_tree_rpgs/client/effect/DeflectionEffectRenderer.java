package net.skill_tree_rpgs.client.effect;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.render.OrbitingEffectRenderer;

import java.util.List;

public class DeflectionEffectRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId = Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "spell_effect/deflection_charge");
    // private static final RenderLayer GLOWING_RENDER_LAYER = CustomLayers.spellEffect(LightEmission.GLOW, false);
    // 1.21.11: `RenderLayer.getEntityTranslucent(Identifier)` is gone; SpellEngine's block-atlas
    // spell-effect layer is the equivalent (entity-translucent pipeline, lightmap + overlay).
    private static final RenderType BASE_RENDER_LAYER = CustomLayers.spellEffect(LightEmission.NONE, true);

    public DeflectionEffectRenderer() {
        super(List.of(new Model(BASE_RENDER_LAYER, modelId)), 0.75F, 0.9F);
        this.orbitingSpeed *= 2F;
    }
}
