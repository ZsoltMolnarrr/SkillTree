package net.class_skills.client;

import net.class_skills.skills.SkillDefinitions;
import net.class_skills.skills.Spells;
import net.class_skills.utils.TranslationUtil;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import net.spell_engine.client.gui.SpellTooltip;

public class ClassSkillsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (var spell: Spells.all) {
            if (spell.mutator() != null) {
                SpellTooltip.addDescriptionMutator(spell.id(), spell.mutator());
            }
        }
        for (var entry: SkillDefinitions.ENTRIES) {
            var skillId = entry.id();
            if (entry.spellReward() != null) {
                var container = entry.spellReward().get(0);
                var id = Identifier.of(container.spell_ids().getFirst());
                TranslationUtil.resolvers.put(skillId, () -> TranslationUtil.resolveSpellDetails(id));
            }
            else if (entry.attributeReward() != null) {
                var attribute = entry.attributeReward();
                TranslationUtil.resolvers.put(skillId, () -> TranslationUtil.resolveAttributeModifierTooltip(attribute));
            }
        }
    }
}
