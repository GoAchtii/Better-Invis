package de.achtii.betterinvis.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.achtii.betterinvis.ShellFlag;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private void betterinvis$hideArmor(PoseStack poseStack, SubmitNodeCollector collector,
                                       int packedLight, HumanoidRenderState state,
                                       float yRot, float xRot, CallbackInfo ci) {
        if (ShellFlag.isActive(state)) {
            ci.cancel();
        }
    }
}