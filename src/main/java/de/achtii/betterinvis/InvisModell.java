package de.achtii.betterinvis;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.resources.Identifier;

public class InvisModell {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath("betterinvis", "invis_shell"), "main");

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = PlayerModel.createMesh(new CubeDeformation(0.2f), false);
        return LayerDefinition.create(mesh, 64, 64);
    }
}