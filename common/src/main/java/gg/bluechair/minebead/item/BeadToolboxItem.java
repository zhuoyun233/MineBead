package gg.bluechair.minebead.item;

import gg.bluechair.minebead.util.toolbox.ToolboxNbt;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BeadToolboxItem extends BlockItem {

    public BeadToolboxItem(Block block, Properties props) {
        super(block, props);
    }


    @Override
    public @NotNull InteractionResult place(BlockPlaceContext ctx) {
        Player player = ctx.getPlayer();
        Level level = ctx.getLevel();
        ItemStack stack = ctx.getItemInHand();

        // 推导放置落点（两端一致）
        BlockPos clicked = ctx.getClickedPos();
        boolean replaceClicked = level.getBlockState(clicked).canBeReplaced(ctx);
        BlockPos placePos = replaceClicked ? clicked : clicked.relative(ctx.getClickedFace());

        // ✅ 两端都阻止：这样客户端不会先“假装放下去”，物品就不会短暂消失
        if (ToolboxNbt.isInsideSelection(stack, placePos)) {
            // 提示只在服务端发，避免双重提示/刷屏
            if (player != null && !level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("§c[BeadToolbox] 不能在工具箱绑定的选区内放置！"),
                        true
                );
            }
            return InteractionResult.FAIL;
        }

        // 允许放置走原版
        InteractionResult res = super.place(ctx);

        return res;
    }

    /*
    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        Player player = ctx.getPlayer();
        Level level = ctx.getLevel();

        // 没玩家（比如发射器）就按正常放置
        if (player != null && !level.isClientSide) {
            BlockPos placePos = ctx.getClickedPos();

            if (SelectionManager.isInsideSelectedRegion(player, placePos)) {
                player.displayClientMessage(
                        Component.literal("§c[BeadToolbox] 不能在已选定区域内放置工具箱！"),
                        true
                );
                return InteractionResult.FAIL;
            }
        }

        return super.place(ctx);
    }
    */
    /**
     * 估算本次将要放置到的坐标（足够用于“禁止放在选区内”这种规则）
     * 笑死这个根本没用！！！！！！！！！！！！画蛇添足！！！！！ ！！！！！
     */
    private static BlockPos getIntendedPlacePos(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        // 如果点击的方块不可替换，放到相邻方块
        if (!ctx.replacingClickedOnBlock()) {
            Direction face = ctx.getClickedFace();
            pos = pos.relative(ctx.getClickedFace());
            //player.displayClientMessage(Component.literal("[BeadToolbox] pos = " + pos.toShortString()), true);
        }
        return pos;
    }
}