package de.achtii.betterinvis.mixin;

import de.achtii.betterinvis.InvisConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getVisibilityPercent", at = @At("HEAD"), cancellable = true)
    private void betterinvis$monsterVisibility(ServerLevel serverLevel, Entity targetingEntity, CallbackInfoReturnable<Double> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof Player) || !self.isInvisible()) return;
        if (!(targetingEntity instanceof Monster monster)) return;

        double followRange = monster.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (followRange <= 0) return;

        cir.setReturnValue(Math.min(1.0, InvisConfig.VISIBLE_RANGE / followRange));
    }
}