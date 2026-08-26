package net.skill_tree_rpgs.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.puffish.skillsmod.api.SkillsAPI;
import net.skill_tree_rpgs.node.ConditionalAttributeReward;
import net.skill_tree_rpgs.node.SpellContainerReward;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init_TAIL_SkillTreeRPGs(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions, CallbackInfo ci) {
        var serverPlayer = (ServerPlayer) (Object) this;
        SkillsAPI.updateRewards(serverPlayer, SpellContainerReward.ID);
        SkillsAPI.updateRewards(serverPlayer, ConditionalAttributeReward.ID);
    }
}
