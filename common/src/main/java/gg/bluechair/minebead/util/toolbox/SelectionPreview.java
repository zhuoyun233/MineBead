package gg.bluechair.minebead.util.toolbox;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import registry.ModItems;

import java.util.Optional;

public final class SelectionPreview {

    /** 返回应该渲染的选区盒子（世界坐标）；不满足条件则 empty */
    public static Optional<AABB> getSelectionBox(Player player, Level level) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.BEAD_TOOLBOX_ITEM.get())) return Optional.empty();

        var pos1Opt = ToolboxNbt.getPos1(stack);
        var pos2Opt = ToolboxNbt.getPos2(stack);
        if (pos1Opt.isEmpty() || pos2Opt.isEmpty()) return Optional.empty();

        var dimOpt = ToolboxNbt.getDimensionId(stack);
        if (dimOpt.isPresent()) {
            // 你 ToolboxNbt.getDimensionId 返回的是 String（看你之前用法）
            String cur = level.dimension().location().toString();
            if (!dimOpt.get().equals(cur)) return Optional.empty();
        }

        BlockPos p1 = pos1Opt.get();
        BlockPos p2 = pos2Opt.get();

        // 注意：AABB 的上界要 +1 才能把方块完整框起来
        int minX = Math.min(p1.getX(), p2.getX());
        int minY = Math.min(p1.getY(), p2.getY());
        int minZ = Math.min(p1.getZ(), p2.getZ());
        int maxX = Math.max(p1.getX(), p2.getX()) + 1;
        int maxY = Math.max(p1.getY(), p2.getY()) + 1;
        int maxZ = Math.max(p1.getZ(), p2.getZ()) + 1;

        return Optional.of(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    private SelectionPreview() {}
}

