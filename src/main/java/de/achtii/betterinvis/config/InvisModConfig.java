package de.achtii.betterinvis.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class InvisModConfig {

    public static final InvisModConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        Pair<InvisModConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder()
                        .configure(InvisModConfig::new);

        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

    public final ModConfigSpec.DoubleValue skinDistanceMin;
    public final ModConfigSpec.DoubleValue skinDistanceMax;
    public final ModConfigSpec.DoubleValue effectDistanceMin;
    public final ModConfigSpec.DoubleValue effectDistanceMax;
    public final ModConfigSpec.DoubleValue mobDistance;

    public InvisModConfig(ModConfigSpec.Builder builder) {

        skinDistanceMin = builder
                .comment("From this distance on, the translucent Player begins to fade away")
                .defineInRange("skinNear", 3.5D, 0.0D, 1000.0D);

        skinDistanceMax = builder
                .comment("The translucent Player will be shown to this distance")
                .defineInRange("skinFar", 5.0D, 0.0D, 1000.0D);

        effectDistanceMin = builder
                .comment("From this distance on, the Player effect begins to fade away")
                .defineInRange("effectNear", 1.0D, 0.0D, 1000.0D);

        effectDistanceMax = builder
                .comment("The Player effect will be shown to this distance")
                .defineInRange("effectFar", 3.5D, 0.0D, 1000.0D);

        mobDistance = builder
                .comment("Mobs will see new Players from this distance on")
                .defineInRange("mobRange", 4.0D, 0.0D, 1000.0D);
    }
}