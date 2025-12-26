package gg.bluechair.minebead.forge.Client;

import gg.bluechair.minebead.ExampleMod;
import gg.bluechair.minebead.ExampleModClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = ExampleMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ExampleModForgeClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ExampleModClient.init();
    }
}

