package net.skill_tree_rpgs.client.effect;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.render.OrbitingEffectRenderer;

import java.util.List;

public class HolyChargeEffectRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId = Identifier.fromNamespaceAndPath(SkillTreeMod.NAMESPACE, "spell_effect/holy_charge");
    private static final RenderType GLOWING_RENDER_LAYER =
            CustomLayers.spellEffect(LightEmission.GLOW, false);

    public HolyChargeEffectRenderer() {
        super(List.of(new Model(GLOWING_RENDER_LAYER, modelId)), 0.8F, 0.7F);
    }
}