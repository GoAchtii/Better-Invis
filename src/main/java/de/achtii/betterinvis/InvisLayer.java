package de.achtii.betterinvis;

import com.mojang.blaze3d.vertex.PoseStack;
import de.achtii.betterinvis.config.InvisModConfig;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class InvisLayer<S extends AvatarRenderState, M extends PlayerModel> extends RenderLayer<S, M> {

    private static final int FULL_BRIGHT = 0xF000F0;

    private static final Identifier SHELL_TEX =
            Identifier.fromNamespaceAndPath("betterinvis", "textures/entity/invis_shell.png");

    private final PlayerModel shellModel;

    public InvisLayer(RenderLayerParent<S, M> parent, EntityModelSet models) {
        super(parent);
        this.shellModel = new PlayerModel(models.bakeLayer(InvisModell.LAYER), false);
    }

    private static float fade(double dist, float near, float far) {
        float s = Mth.clamp((float) ((far - dist) / (far - near)), 0f, 1f);
        return s * s * (3f - 2f * s);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                       S state, float yRot, float xRot) {
        if (!ShellFlag.isActive(state)) return;

        float t = state.ageInTicks;
        double dist = Math.sqrt(state.distanceToCameraSq);

        float skinNear = InvisModConfig.INSTANCE.skinDistanceMin.get().floatValue();
        float skinFar = InvisModConfig.INSTANCE.skinDistanceMax.get().floatValue();
        float skinStrength = fade(dist, skinNear, skinFar);

        float shellNear = InvisModConfig.INSTANCE.effectDistanceMin.get().floatValue();
        float shellFar = InvisModConfig.INSTANCE.effectDistanceMax.get().floatValue();
        float shellStrength = fade(dist, shellNear, shellFar);

        if (skinStrength <= 0.01f && shellStrength <= 0.01f) return;

        if (skinStrength > 0.01f) {
            Identifier skinTex = state.skin.body().texturePath();
            float ghostAlpha = 0.18f * skinStrength;
            int ghostColor = ARGB.colorFromFloat(ghostAlpha, 1f, 1f, 1f);
            collector.submitModel(this.getParentModel(), state, poseStack,
                    RenderTypes.entityTranslucent(skinTex),
                    light, OverlayTexture.NO_OVERLAY, ghostColor, null, 0);
        }
        if (shellStrength > 0.01f) {
            RenderType type = RenderTypes.energySwirl(SHELL_TEX, t * 0.004f % 1f, t * 0.006f % 1f);

            float pulse = 0.75f + 0.25f * Mth.sin(t * 0.1f);
            float k = pulse * shellStrength;
            int color = ARGB.colorFromFloat(1f, 1.0f * k, 0.6f * k, 1.0f * k);

            shellModel.setupAnim(state);
            collector.submitModel(shellModel, state, poseStack, type,
                    FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color, null, 0);
        }
    }
}