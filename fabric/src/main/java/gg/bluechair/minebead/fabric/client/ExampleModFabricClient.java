package gg.bluechair.minebead.fabric.client;

import gg.bluechair.minebead.BlockEntity.ColorBlockEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import registry.ModBlocks;
import registry.ModItems;

public final class ExampleModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        System.out.println("[MineBead] Fabric client entrypoint called!");
        FabricSelectionPreviewRenderer.init();
        // 方块（世界中）
        ColorProviderRegistry.BLOCK.register(
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

        // 物品（手里/背包/掉落物）
        ColorProviderRegistry.ITEM.register(
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
