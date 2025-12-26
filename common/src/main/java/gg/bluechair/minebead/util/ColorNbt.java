package gg.bluechair.minebead.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class ColorNbt {
    private ColorNbt() {}

    public static final String KEY = "Color";
    public static final int DEFAULT_COLOR = 0xFFFFFF;

    public static int getColor(ItemStack stack) {
        if (stack == null) return DEFAULT_COLOR;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(KEY)) return DEFAULT_COLOR;
        return tag.getInt(KEY);
    }

    public static void setColor(ItemStack stack, int rgb) {
        stack.getOrCreateTag().putInt(KEY, rgb & 0xFFFFFF);
    }

    public static boolean hasColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(KEY);
    }
}
