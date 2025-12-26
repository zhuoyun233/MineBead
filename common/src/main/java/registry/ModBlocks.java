package registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import gg.bluechair.minebead.Block.ColorBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import gg.bluechair.minebead.ExampleMod;

public class ModBlocks {
    // 这两个是延迟注册的，分别对应世界中的方块和手中的物品ITEM
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ExampleMod.MOD_ID, Registries.BLOCK);

    // 预注册方块
    public static final RegistrySupplier<Block> COLOR_BLOCK = BLOCKS.register(
            "color_block",
            () -> new ColorBlock(BlockBehaviour.Properties.of().strength(50.0F, 3600000.0F))
    );


    public static void register() {
        BLOCKS.register();
    }
}
