package de.achtii.betterinvis;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class ShellFlag {
    public static boolean isActive(EntityRenderState state) {
        return state.isInvisible;
    }
}