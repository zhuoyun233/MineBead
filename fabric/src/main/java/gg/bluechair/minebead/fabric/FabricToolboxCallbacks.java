package gg.bluechair.minebead.fabric;

import gg.bluechair.minebead.util.toolbox.SelectionManager;
import gg.bluechair.minebead.util.toolbox.ToolboxNbt;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

import registry.ModItems;

public final class FabricToolboxCallbacks {

    public static void init() {

        // 左键点方块：pos1
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (!isHoldingToolbox(stack)) return InteractionResult.PASS;

            if (!world.isClientSide) {
                ToolboxNbt.setPos1(stack, pos);
                ToolboxNbt.setDimension(stack, world.dimension());
                player.displayClientMessage(Component.literal("[BeadToolbox] pos1 = " + pos.toShortString()), true);
            }

            // SUCCESS：阻止继续挖掘（像 WorldEdit 斧头）
            return InteractionResult.SUCCESS;
        });

        // 右键点方块：pos2
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (!isHoldingToolbox(stack)) return InteractionResult.PASS;

            // 潜行右键：放置（交给 BlockItem.place）
            if (player.isShiftKeyDown()) {
                return InteractionResult.PASS;
            }

            // 非潜行：选 pos2
            if (!world.isClientSide) {
                //SelectionManager.setPos2(player, hitResult.getBlockPos());
                ToolboxNbt.setPos2(stack, hitResult.getBlockPos());
                ToolboxNbt.setDimension(stack, world.dimension());
                player.displayClientMessage(Component.literal("[BeadToolbox] pos2 = " + hitResult.getBlockPos().toShortString()), true);
            }

            // SUCCESS：阻止原方块交互（开门/开箱/按钮等）
            return InteractionResult.SUCCESS;
        });
    }

    private static boolean isHoldingToolbox(ItemStack stack) {
        return stack != null && stack.is(ModItems.BEAD_TOOLBOX_ITEM.get());
    }

    private FabricToolboxCallbacks() {}
}
