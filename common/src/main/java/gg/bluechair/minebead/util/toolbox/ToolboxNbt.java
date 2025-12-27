package gg.bluechair.minebead.util.toolbox;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public final class ToolboxNbt {
    private static final String ROOT = "minebead";
    private static final String POS1 = "pos1";
    private static final String POS2 = "pos2";
    private static final String DIM  = "dim";

    public static CompoundTag root(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ROOT, CompoundTag.TAG_COMPOUND)) {
            tag.put(ROOT, new CompoundTag());
        }
        return tag.getCompound(ROOT);
    }

    public static void setPos1(ItemStack stack, BlockPos pos) {
        root(stack).putLong(POS1, pos.asLong());
    }

    public static void setPos2(ItemStack stack, BlockPos pos) {
        root(stack).putLong(POS2, pos.asLong());
    }

    public static Optional<BlockPos> getPos1(ItemStack stack) {
        CompoundTag r = root(stack);
        return r.contains(POS1, CompoundTag.TAG_LONG) ? Optional.of(BlockPos.of(r.getLong(POS1))) : Optional.empty();
    }

    public static Optional<BlockPos> getPos2(ItemStack stack) {
        CompoundTag r = root(stack);
        return r.contains(POS2, CompoundTag.TAG_LONG) ? Optional.of(BlockPos.of(r.getLong(POS2))) : Optional.empty();
    }

    public static boolean hasCompleteSelection(ItemStack stack) {
        return getPos1(stack).isPresent() && getPos2(stack).isPresent();
    }

    public static void setDimension(ItemStack stack, ResourceKey<Level> dim) {
        root(stack).putString(DIM, dim.location().toString());
    }

    public static Optional<String> getDimensionId(ItemStack stack) {
        CompoundTag r = root(stack);
        return r.contains(DIM, CompoundTag.TAG_STRING) ? Optional.of(r.getString(DIM)) : Optional.empty();
    }

    public static boolean isInsideSelection(ItemStack stack, BlockPos pos) {
        var p1 = getPos1(stack);
        var p2 = getPos2(stack);
        if (p1.isEmpty() || p2.isEmpty()) return false;

        BlockPos a = p1.get();
        BlockPos b = p2.get();

        int minX = Math.min(a.getX(), b.getX());
        int maxX = Math.max(a.getX(), b.getX());
        int minY = Math.min(a.getY(), b.getY());
        int maxY = Math.max(a.getY(), b.getY());
        int minZ = Math.min(a.getZ(), b.getZ());
        int maxZ = Math.max(a.getZ(), b.getZ());

        return pos.getX() >= minX && pos.getX() <= maxX
                && pos.getY() >= minY && pos.getY() <= maxY
                && pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    private ToolboxNbt() {}
}

