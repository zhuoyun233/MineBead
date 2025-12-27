package registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import gg.bluechair.minebead.BlockEntity.BeadToolboxEntity;
import gg.bluechair.minebead.BlockEntity.ColorBlockEntity;
import gg.bluechair.minebead.ExampleMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ExampleMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<ColorBlockEntity>> COLOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("color_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ColorBlockEntity::new,
                            ModBlocks.COLOR_BLOCK.get()
                    ).build(null)
            );

    // ===== 新增：Bead Toolbox =====
    public static final RegistrySupplier<BlockEntityType<BeadToolboxEntity>> BEAD_TOOLBOX_ENTITY =
            BLOCK_ENTITIES.register("bead_toolbox_entity",
                    () -> BlockEntityType.Builder.of(
                            BeadToolboxEntity::new,
                            ModBlocks.BEAD_TOOLBOX.get()
                    ).build(null)
            );

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
