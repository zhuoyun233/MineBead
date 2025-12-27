package gg.bluechair.minebead.Block;

import gg.bluechair.minebead.BlockEntity.BeadToolboxEntity;
import gg.bluechair.minebead.util.toolbox.ToolboxNbt;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeadToolbox extends BaseEntityBlock {

    public BeadToolbox(Properties props) {
        super(props);
    }

    // === 关键 1：告诉 MC 这个 Block 有自己的 BlockEntity ===
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeadToolboxEntity(pos, state);
    }

    // === 右键交互：显示绑定选区 ===
    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {

        // 只处理主手，避免副手重复
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BeadToolboxEntity toolboxBE) {

                ItemStack bound = toolboxBE.getBoundStack();
                if (bound == null || !bound.hasTag()) {
                    player.displayClientMessage(
                            Component.literal("[BeadToolbox] 尚未绑定选区"),
                            true
                    );
                } else {
                    var pos1 = ToolboxNbt.getPos1(bound);
                    var pos2 = ToolboxNbt.getPos2(bound);
                    var dim  = ToolboxNbt.getDimensionId(bound);

                    if (pos1.isEmpty() || pos2.isEmpty()) {
                        player.displayClientMessage(
                                Component.literal("[BeadToolbox] 选区不完整"),
                                true
                        );
                    } else {
                        Component msg = Component.literal(
                                "[BeadToolbox] 绑定选区:\n" +
                                        "pos1 = " + pos1.get().toShortString() + "\n" +
                                        "pos2 = " + pos2.get().toShortString() +
                                        (dim.isPresent() ? "\n维度 = " + dim.get() : "")
                        );
                        player.displayClientMessage(msg, false);
                    }
                }
            }
        }

        // === 关键 2：1.20 推荐返回值 ===
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // === 放置时：Item → BlockEntity 绑定 ===
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && stack.hasTag()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BeadToolboxEntity toolboxBE) {
                toolboxBE.bindFromItem(stack);
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    // 渲染
    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
