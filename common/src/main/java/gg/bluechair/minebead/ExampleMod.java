package gg.bluechair.minebead;
import registry.ModBlockEntities;
import registry.ModBlocks;
import registry.ModItems;

public final class ExampleMod {
    public static final String MOD_ID = "minebead";

    public static void init() {
        // Write common init code here.
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
    }
}
