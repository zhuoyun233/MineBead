package gg.bluechair.minebead;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import gg.bluechair.minebead.BlockEntity.ColorBlockEntity;
import gg.bluechair.minebead.util.ColorNbt;
import net.minecraft.world.level.block.entity.BlockEntity;
import registry.ModBlocks;
import registry.ModItems;

public class ExampleModClient {

    public static void init() {
        System.out.println("[MineBead] ExampleModClient.init() called");
        registerBlockColors();
        registerItemColors();
    }

    private static void registerBlockColors() {
        // 注意：不同 Architectury 版本方法名可能是 registerBlockColors / registerBlockColor
        // 你用 IDE 自动补全选择存在的那个即可（参数基本一致）。
        ColorHandlerRegistry.registerBlockColors(
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

    private static void registerItemColors() {
        // 让“手里/背包/掉落物”也能根据 ItemStack NBT 上的 Color 上色
        ColorHandlerRegistry.registerItemColors(
                (stack, tintIndex) -> {
                    // 我们只给 tintIndex==0 的那层上色
                    if (tintIndex != 0) return 0xFFFFFF;
                    return ColorNbt.getColor(stack);
                },
                ModItems.COLOR_BLOCK_ITEM.get()
        );
    }
}
