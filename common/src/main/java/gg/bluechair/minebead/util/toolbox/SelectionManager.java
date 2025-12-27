package gg.bluechair.minebead.util.toolbox;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SelectionManager {

    public static final class Selection {
        public BlockPos pos1;
        public BlockPos pos2;
    }

    private static final Map<UUID, Selection> SEL = new ConcurrentHashMap<>();

    public static Selection get(Player player) {
        return SEL.computeIfAbsent(player.getUUID(), id -> new Selection());
    }

    public static void setPos1(Player player, BlockPos pos) {
        get(player).pos1 = pos.immutable();
        player.displayClientMessage(Component.literal("[BeadToolbox] pos1 = " + pos.toShortString()), true);
    }

    public static void setPos2(Player player, BlockPos pos) {
        get(player).pos2 = pos.immutable();
        player.displayClientMessage(Component.literal("[BeadToolbox] pos2 = " + pos.toShortString()), true);
    }

    public static boolean isInsideSelectedRegion(Player player, BlockPos pos) {
        Selection sel = get(player);
        if (sel.pos1 == null || sel.pos2 == null) return false;

        int minX = Math.min(sel.pos1.getX(), sel.pos2.getX());
        int maxX = Math.max(sel.pos1.getX(), sel.pos2.getX());
        int minY = Math.min(sel.pos1.getY(), sel.pos2.getY());
        int maxY = Math.max(sel.pos1.getY(), sel.pos2.getY());
        int minZ = Math.min(sel.pos1.getZ(), sel.pos2.getZ());
        int maxZ = Math.max(sel.pos1.getZ(), sel.pos2.getZ());

        return pos.getX() >= minX && pos.getX() <= maxX
                && pos.getY() >= minY && pos.getY() <= maxY
                && pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    private SelectionManager() {}
}