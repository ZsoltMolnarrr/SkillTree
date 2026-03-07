package net.skill_tree_rpgs.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.puffish.skillsmod.api.SkillsAPI;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.skill_tree_rpgs.node.SpellContainerReward;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init_TAIL_SkillTreeRPGs(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions, CallbackInfo ci) {
        var serverPlayer = (ServerPlayerEntity) (Object) this;
        SkillsAPI.updateRewards(serverPlayer, SpellContainerReward.ID);
        SkillsAPI.updateRewards(serverPlayer, ConditionalAttributeReward.ID);
    }
}
