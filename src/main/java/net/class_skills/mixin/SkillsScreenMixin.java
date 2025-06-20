package net.class_skills.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.class_skills.utils.TranslationUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.client.config.ClientBackgroundConfig;
import net.puffish.skillsmod.client.config.ClientCategoryConfig;
import net.puffish.skillsmod.client.config.ClientFrameConfig;
import net.puffish.skillsmod.client.config.ClientIconConfig;
import net.puffish.skillsmod.client.config.skill.ClientSkillConfig;
import net.puffish.skillsmod.client.config.skill.ClientSkillConnectionConfig;
import net.puffish.skillsmod.client.config.skill.ClientSkillDefinitionConfig;
import net.puffish.skillsmod.client.data.ClientCategoryData;
import net.puffish.skillsmod.client.gui.SkillsScreen;
import net.puffish.skillsmod.client.rendering.ConnectionBatchedRenderer;
import net.puffish.skillsmod.client.rendering.ItemBatchedRenderer;
import net.puffish.skillsmod.client.rendering.TextureBatchedRenderer;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(SkillsScreen.class)
public abstract class SkillsScreenMixin extends Screen{
    protected SkillsScreenMixin(Text title) {
        super(title);
    }

    @Shadow protected abstract Vector2i getMousePos(double mouseX, double mouseY);
    @Shadow protected abstract Vector2i getTransformedMousePos(double mouseX, double mouseY, ClientCategoryData activeCategoryData);
    @Shadow protected abstract void drawBackground(DrawContext context, ClientBackgroundConfig background);
    @Shadow protected abstract boolean isInsideContent(Vector2i mouse);
    @Shadow protected abstract boolean isInsideSkill(Vector2i transformedMouse, ClientSkillConfig skill, ClientSkillDefinitionConfig definition);
    @Shadow protected abstract void drawFrame(DrawContext context, TextureBatchedRenderer textureRenderer, ClientFrameConfig frame, float sizeScale, int x, int y, Skill.State state);
    @Shadow protected abstract void drawIcon(DrawContext context, TextureBatchedRenderer textureRenderer, ItemBatchedRenderer itemRenderer, ClientIconConfig icon, float sizeScale, int x, int y);

    @Inject(method = "drawContentWithCategory", at = @At("HEAD"), cancellable = true)
    private void drawContentWithCategory_HEAD(DrawContext context, double mouseX, double mouseY, ClientCategoryData activeCategoryData, CallbackInfo ci) {
        ci.cancel();

        if (this.client != null) {
            Vector2i mouse = this.getMousePos(mouseX, mouseY);
            Vector2i transformedMouse = this.getTransformedMousePos(mouseX, mouseY, activeCategoryData);
            ClientCategoryConfig activeCategory = activeCategoryData.getConfig();
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate((float)activeCategoryData.getX() + (float)this.width / 2.0F, (float)activeCategoryData.getY() + (float)this.height / 2.0F, 0.0F);
            matrices.scale(activeCategoryData.getScale(), activeCategoryData.getScale(), 1.0F);
            this.drawBackground(context, activeCategory.background());
            ConnectionBatchedRenderer connectionRenderer = new ConnectionBatchedRenderer();
            Iterator var12 = activeCategory.normalConnections().iterator();

            while(var12.hasNext()) {
                ClientSkillConnectionConfig connection = (ClientSkillConnectionConfig)var12.next();
                activeCategoryData.getConnection(connection).ifPresent((relation) -> {
                    connectionRenderer.emitConnection(context, (float)relation.getSkillA().x(), (float)relation.getSkillA().y(), (float)relation.getSkillB().x(), (float)relation.getSkillB().y(), connection.bidirectional(), relation.getColor().fill().argb(), relation.getColor().stroke().argb());
                });
            }

            if (this.isInsideContent(mouse)) {
                Optional<ClientSkillConfig> optHoveredSkill = activeCategory.skills().values().stream().filter((skillx) -> {
                    return (Boolean)activeCategory.getDefinitionById(skillx.definitionId()).map((definition) -> {
                        return this.isInsideSkill(transformedMouse, skillx, definition);
                    }).orElse(false);
                }).findFirst();
                optHoveredSkill.ifPresent((hoveredSkill) -> {
                    ClientSkillDefinitionConfig definition = (ClientSkillDefinitionConfig)activeCategory.definitions().get(hoveredSkill.definitionId());
                    if (definition != null) {
                        ArrayList<OrderedText> lines = new ArrayList();
                        lines.add(definition.title().asOrderedText());

                        var descriptionOverride = TranslationUtil.resolve(definition.id());
                        if (descriptionOverride.isEmpty()) {
                            lines.addAll(Tooltip.wrapLines(this.client, Texts.setStyleIfAbsent(definition.description().copy(), Style.EMPTY.withFormatting(Formatting.GRAY))));
                        } else {
                            lines.addAll(descriptionOverride.stream().map(Text::asOrderedText).toList());
                        }

                        if (Screen.hasShiftDown()) {
                            lines.addAll(Tooltip.wrapLines(this.client, Texts.setStyleIfAbsent(definition.extraDescription().copy(), Style.EMPTY.withFormatting(Formatting.GRAY))));
                        }

                        if (this.client.options.advancedItemTooltips) {
                            lines.add(Text.literal(hoveredSkill.id()).formatted(Formatting.DARK_GRAY).asOrderedText());
                        }

                        this.setTooltip(lines);
                        Collection<ClientSkillConnectionConfig> connections = (Collection)activeCategory.skillExclusiveConnections().get(hoveredSkill.id());
                        if (connections != null) {
                            Iterator var9 = connections.iterator();

                            while(var9.hasNext()) {
                                ClientSkillConnectionConfig connection = (ClientSkillConnectionConfig)var9.next();
                                activeCategoryData.getConnection(connection).ifPresent((relation) -> {
                                    connectionRenderer.emitConnection(context, (float)relation.getSkillA().x(), (float)relation.getSkillA().y(), (float)relation.getSkillB().x(), (float)relation.getSkillB().y(), connection.bidirectional(), relation.getColor().fill().argb(), relation.getColor().stroke().argb());
                                });
                            }
                        }

                    }
                });
            }

            context.draw();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            connectionRenderer.draw();
            TextureBatchedRenderer textureRenderer = new TextureBatchedRenderer();
            ItemBatchedRenderer itemRenderer = new ItemBatchedRenderer();
            Iterator var14 = activeCategory.skills().values().iterator();

            while(var14.hasNext()) {
                ClientSkillConfig skill = (ClientSkillConfig)var14.next();
                activeCategory.getDefinitionById(skill.definitionId()).ifPresent((definition) -> {
                    this.drawFrame(context, textureRenderer, definition.frame(), definition.size(), skill.x(), skill.y(), activeCategoryData.getSkillState(skill));
                    this.drawIcon(context, textureRenderer, itemRenderer, definition.icon(), definition.size(), skill.x(), skill.y());
                });
            }

            textureRenderer.draw();
            itemRenderer.draw();
            matrices.pop();
        }
    }

}
