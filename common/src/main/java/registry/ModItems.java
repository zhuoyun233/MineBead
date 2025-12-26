package registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import gg.bluechair.minebead.item.ColorBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import dev.architectury.registry.CreativeTabRegistry;

import gg.bluechair.minebead.ExampleMod;

public class ModItems {
    // 这个是延迟注册的，对应手中的物品ITEM
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ExampleMod.MOD_ID, Registries.ITEM);


    public static final RegistrySupplier<Item> COLOR_BLOCK_ITEM = ITEMS.register(
            "color_block",
            () -> new ColorBlockItem(ModBlocks.COLOR_BLOCK.get(), new Item.Properties())
    );

    // 你的第一个物品：example_item
    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register(
            "example_item",
            () -> new Item(new Item.Properties())
    );

    public static void register() {
        ITEMS.register();

        // 把物品塞进创造模式栏（示例：工具与实用）
        CreativeTabRegistry.append(CreativeModeTabs.TOOLS_AND_UTILITIES,
                COLOR_BLOCK_ITEM.get(),
                EXAMPLE_ITEM.get()
        );
    }
}
