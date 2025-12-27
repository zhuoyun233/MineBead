package gg.bluechair.minebead.forge;

import gg.bluechair.minebead.util.toolbox.ToolboxNbt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import registry.ModItems;

public final class ForgeToolboxCallbacks {

    public static void init() {
        // 用 HIGHEST 确保我们先拿到事件（避免被别的 mod/逻辑提前处理）
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ForgeToolboxCallbacks::onLeftClickBlock);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ForgeToolboxCallbacks::onRightClickBlock);
        System.out.println("[MineBead] ForgeToolboxCallbacks registered");
    }

    private static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Level level = event.getLevel();
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        ItemStack stack = event.getItemStack();
        if (!isHoldingToolbox(stack)) return;

        // ✅ 先：客户端也要立刻拦截，打断破坏
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        // （部分版本有这个，能更明确地拒绝对方块的使用）
        // event.setUseBlock(Event.Result.DENY);

        // ✅ 后：只在服务端写 NBT
        if (!level.isClientSide) {
            var pos = event.getPos();
            ToolboxNbt.setPos1(stack, pos);
            ToolboxNbt.setDimension(stack, level.dimension());
            event.getEntity().displayClientMessage(
                    Component.literal("[BeadToolbox] pos1 = " + pos.toShortString()),
                    true
            );
        }
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        ItemStack stack = event.getItemStack();
        if (!isHoldingToolbox(stack)) return;

        // 潜行右键：放置/正常交互，放行
        if (event.getEntity().isShiftKeyDown()) return;

        // ✅ 客户端也要立刻拦截，打断方块交互预测
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        // ✅ 更强硬：禁止方块被 use、禁止物品被 use（避免开门/开箱/按钮等）
        event.setUseBlock(Event.Result.DENY);
        event.setUseItem(Event.Result.DENY);

        // ✅ 只在服务端写 NBT
        if (!level.isClientSide) {
            var pos = event.getPos();
            ToolboxNbt.setPos2(stack, pos);
            ToolboxNbt.setDimension(stack, level.dimension());
            event.getEntity().displayClientMessage(
                    Component.literal("[BeadToolbox] pos2 = " + pos.toShortString()),
                    true
            );
        }
    }


    private static boolean isHoldingToolbox(ItemStack stack) {
        return stack != null && stack.is(ModItems.BEAD_TOOLBOX_ITEM.get());
    }

    private ForgeToolboxCallbacks() {}
}
