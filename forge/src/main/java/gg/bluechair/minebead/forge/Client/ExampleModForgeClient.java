package gg.bluechair.minebead.forge.Client;

import gg.bluechair.minebead.BlockEntity.ColorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import registry.ModBlocks;
import registry.ModItems;

import static gg.bluechair.minebead.ExampleMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ExampleModForgeClient {

    private ExampleModForgeClient() {}

    /** 可选：如果你想在 Forge 主类里显式调用一下，保留这个方法 */
    public static void init() {
        System.out.println("[MineBead] Forge client entrypoint called!");
        ForgeSelectionPreviewRenderer.init(); // 如果你用的是“注解订阅渲染事件”，这行可省
    }

    // === 方块颜色（世界中） ===
    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, world, pos, tintIndex) -> {
                    if (world != null && pos != null) {
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof ColorBlockEntity colorBE) {
                            return colorBE.getColor();
                        }
                    }
                    return 0xFFFFFF;
                },
                ModBlocks.COLOR_BLOCK.get()
        );
    }

    // === 物品颜色（手里/背包/掉落物） ===
    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> {
                    if (tintIndex != 0) return 0xFFFFFF;
                    return stack.hasTag() && stack.getTag().contains("Color")
                            ? stack.getTag().getInt("Color")
                            : 0xFFFFFF;
                },
                ModItems.COLOR_BLOCK_ITEM.get()
        );
    }
}
