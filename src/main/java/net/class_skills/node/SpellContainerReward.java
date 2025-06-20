package net.class_skills.node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.class_skills.ClassSkillsMod;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.reward.Reward;
import net.puffish.skillsmod.api.reward.RewardConfigContext;
import net.puffish.skillsmod.api.reward.RewardDisposeContext;
import net.puffish.skillsmod.api.reward.RewardUpdateContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.internals.container.SpellContainerSource;

import java.util.List;

public class SpellContainerReward implements Reward {
    public static final Identifier ID = Identifier.of(ClassSkillsMod.NAMESPACE, "spell");
    public static void register() {
        SkillsAPI.registerReward(ID, SpellContainerReward::parse);
    }
    private static final Gson gson = new GsonBuilder().create();
    public record DataStructure(List<SpellContainer> containers) { }
    private static Result<SpellContainerReward, Problem> parse(RewardConfigContext context) {
        var dataResult = context.getData();
        if (dataResult.getFailure().isPresent()) {
            return Result.failure(dataResult.getFailure().get());
        }
        var data = dataResult.getSuccess();
        var reward = new SpellContainerReward();
        try {
            var json = data.get().getJson();
            var parsedContainers = gson.fromJson(json, DataStructure.class);
            var idString = parsedContainers.containers().getFirst().spell_ids().getFirst();
            reward.id = Identifier.of(idString);
            reward.containers = parsedContainers.containers();
        } catch (Exception e) {
            return Result.failure(Problem.message(
                    "Failed to parse spell container reward" + e.getMessage()
            ));
        }
        return Result.success(reward);
    }

    private Identifier id;
    /// Using list of containers instead a single container with a list of spells
    /// to avoid Spell Sourcing logic having to sort multiple spells.
    private List<SpellContainer> containers = List.of();

    @Override
    public void update(RewardUpdateContext context) {
        int count = context.getCount();
        var player = context.getPlayer();
        var containers = ((SpellContainerSource.Owner)player).serverSideSpellContainers();
        containers.remove(id.toString());
        if (count > 0) {
            var index = Math.min(count - 1, this.containers.size() - 1);
            containers.put(id.toString(), this.containers.get(index));
        }
        SpellContainerSource.syncServerSideContainers(player);
    }

    @Override
    public void dispose(RewardDisposeContext context) {
    }
}
