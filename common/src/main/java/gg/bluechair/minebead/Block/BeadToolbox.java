package gg.bluechair.minebead.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BeadToolbox extends Block {

    public BeadToolbox(Properties props) {
        super(props);
    }

    // 以后这里换成打开菜单（MenuProvider / BlockEntity 等）
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            player.displayClientMessage(Component.literal("[BeadToolbox] TODO: open menu"), true);
        }
        return InteractionResult.SUCCESS;
    }
}