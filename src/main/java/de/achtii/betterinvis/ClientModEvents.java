package de.achtii.betterinvis;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static de.achtii.betterinvis.betterinvis.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerLayerDefs(
            EntityRenderersEvent.RegisterLayerDefinitions e
    ) {
        e.registerLayerDefinition(
                InvisModell.LAYER,
                InvisModell::createLayer
        );
    }

    @SubscribeEvent
    public static void addLayers(
            EntityRenderersEvent.AddLayers e
    ) {

        for (var skin : e.getSkins()) {

            if (e.getPlayerRenderer(skin)
                    instanceof AvatarRenderer<?> renderer) {

                renderer.addLayer(
                        new InvisLayer<AvatarRenderState, PlayerModel>(
                                renderer,
                                e.getEntityModels()
                        )
                );
            }
        }

        for (var entityType : e.getEntityTypes()) {

            EntityRenderer<?, ?> renderer =
                    e.getRenderer(entityType);

            if (renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer) {
                addMobLayer(livingRenderer);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addMobLayer(
            LivingEntityRenderer renderer
    ) {
        renderer.addLayer(
                new MobInvisLayer(renderer)
        );
    }
}