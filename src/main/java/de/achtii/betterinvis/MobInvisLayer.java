package de.achtii.betterinvis;

import com.mojang.blaze3d.vertex.PoseStack;
import de.achtii.betterinvis.config.InvisModConfig;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MobInvisLayer
        extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {

    private static final int FULL_BRIGHT = 0xF000F0;

    private static final Identifier SHELL_TEX =
            Identifier.fromNamespaceAndPath(
                    "betterinvis",
                    "textures/entity/invis_shell.png"
            );

    private final LivingEntityRenderer renderer;

    public MobInvisLayer(LivingEntityRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    private static float fade(double dist, float near, float far) {
        if (far <= near) {
            return dist <= near ? 1.0f : 0.0f;
        }

        float s = Mth.clamp(
                (float) ((far - dist) / (far - near)),
                0f,
                1f
        );

        return s * s * (3f - 2f * s);
    }

    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int light,
            LivingEntityRenderState state,
            float yRot,
            float xRot
    ) {
        if (!state.isInvisible) {
            return;
        }

        double dist = Math.sqrt(state.distanceToCameraSq);

        float skinNear =
                InvisModConfig.INSTANCE.skinDistanceMin.get().floatValue();

        float skinFar =
                InvisModConfig.INSTANCE.skinDistanceMax.get().floatValue();

        float skinStrength = fade(
                dist,
                skinNear,
                skinFar
        );

        if (skinStrength > 0.01f) {

            Identifier mobTexture =
                    renderer.getTextureLocation(state);

            float alpha = 0.18f * skinStrength;

            int color = ARGB.colorFromFloat(
                    alpha,
                    1.0f,
                    1.0f,
                    1.0f
            );

            RenderType mobType =
                    RenderTypes.entityTranslucent(mobTexture);

            collector.submitModel(
                    this.getParentModel(),
                    state,
                    poseStack,
                    mobType,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    color,
                    null,
                    0
            );
        }

        float shellNear =
                InvisModConfig.INSTANCE.effectDistanceMin.get().floatValue();

        float shellFar =
                InvisModConfig.INSTANCE.effectDistanceMax.get().floatValue();

        float shellStrength = fade(
                dist,
                shellNear,
                shellFar
        );

        if (shellStrength <= 0.01f) {
            return;
        }

        float t = state.ageInTicks;

        RenderType shellType =
                RenderTypes.energySwirl(
                        SHELL_TEX,
                        t * 0.004f % 1f,
                        t * 0.006f % 1f
                );

        float pulse =
                0.75f + 0.25f * Mth.sin(t * 0.1f);

        float k = pulse * shellStrength;

        int shellColor = ARGB.colorFromFloat(
                1f,
                1.0f * k,
                0.6f * k,
                1.0f * k
        );

        collector.submitModel(
                this.getParentModel(),
                state,
                poseStack,
                shellType,
                FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                shellColor,
                null,
                0
        );
    }
}