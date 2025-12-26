package gg.bluechair.minebead.util;

import net.minecraft.world.item.DyeColor;

public final class DyeApprox {
    private DyeApprox() {}

    public static DyeColor nearestDye(int rgb) {
        rgb &= 0xFFFFFF;
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        DyeColor best = DyeColor.WHITE;
        int bestDist = Integer.MAX_VALUE;

        for (DyeColor d : DyeColor.values()) {
            int crgb = d.getMapColor().col; // 1.20.1 Mojang mappings 通常是 col
            int cr = (crgb >> 16) & 0xFF;
            int cg = (crgb >> 8) & 0xFF;
            int cb = crgb & 0xFF;

            int dr = r - cr, dg = g - cg, db = b - cb;
            int dist = dr * dr + dg * dg + db * db;

            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        return best;
    }
}
