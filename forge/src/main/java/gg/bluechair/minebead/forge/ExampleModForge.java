package gg.bluechair.minebead.forge;

import dev.architectury.platform.forge.EventBuses;
import gg.bluechair.minebead.ExampleMod;
import gg.bluechair.minebead.forge.Client.ExampleModForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MOD_ID)
public class ExampleModForge {

    @SuppressWarnings("removal") // 只压掉你看到的“待删除”警告
    public ExampleModForge() {
        // ✅ Architectury 需要把 ModEventBus 注册进去
        EventBuses.registerModEventBus(
                ExampleMod.MOD_ID,
                FMLJavaModLoadingContext.get().getModEventBus()
        );

        // ✅ common 初始化（注册方块/物品/BE 等）
        ExampleMod.init();
        ForgeToolboxCallbacks.init();

        // ✅ Forge 客户端初始化（颜色、渲染器等）
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ExampleModForgeClient::init);
    }
}
