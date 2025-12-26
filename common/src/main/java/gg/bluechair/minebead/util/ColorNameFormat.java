package gg.bluechair.minebead.util;

public final class ColorNameFormat {
    private ColorNameFormat() {}

    public enum Mode {
        HEX,   // #RRGGBB
        CODE   // A1 / B4（你未来实现）
    }

    private static Mode mode = Mode.HEX;

    public static void setMode(Mode m) {
        mode = m;
    }

    public static String format(int rgb) {
        rgb &= 0xFFFFFF;

        return switch (mode) {
            case HEX -> String.format("#%06X", rgb);
            case CODE -> toCode(rgb); // 预留
        };
    }

    // 这里先占位；未来你实现“颜色->A1/B4”的映射规则
    private static String toCode(int rgb) {
        // 示例：先返回 HEX 简写占位，避免 null
        return String.format("C%06X", rgb);
    }
}
