package gg.bluechair.minebead.item;

import gg.bluechair.minebead.util.ColorNameFormat;
import gg.bluechair.minebead.util.ColorNbt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ColorBlockItem extends BlockItem {

    public ColorBlockItem(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public Component getName(ItemStack stack) {
        int rgb = ColorNbt.getColor(stack);
        String label = ColorNameFormat.format(rgb); // 现在是 #RRGGBB，以后换 A1/B4

        Component baseName = Component.translatable(this.getDescriptionId(stack));
        return Component.translatable("item.minebead.color_block.named", baseName, label);
    }


}
