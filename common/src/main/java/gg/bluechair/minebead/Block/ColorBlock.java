package gg.bluechair.minebead.Block;

import gg.bluechair.minebead.BlockEntity.ColorBlockEntity;
import gg.bluechair.minebead.util.ColorNbt;
import gg.bluechair.minebead.util.DyeApprox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorBlock extends Block implements EntityBlock {

    public static final EnumProperty<DyeColor> MAP_DYE = EnumProperty.create("map_dye", DyeColor.class);

    public ColorBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.defaultBlockState().setValue(MAP_DYE, DyeColor.WHITE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MAP_DYE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ColorBlockEntity(pos, state);
    }

    /**
     * 当玩家放置方块时，把 ItemStack 上的 Color NBT 写入已放置方块的 BlockEntity。
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        int rgb = ColorNbt.getColor(stack);

        // 1) 写 BE（世界真彩）
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ColorBlockEntity colorBE) {
            // 客户端：立即写入用于“本地即时渲染”
            // 服务端：写入并同步给所有玩家
            colorBE.setColor(rgb);

            // 客户端需要立刻重渲染（只要 8）
            // 服务端需要同步 + 邻居 + 重渲染（11）
            int flags = level.isClientSide ? 8 : 11;
            level.sendBlockUpdated(pos, state, state, flags);
        }

        // 2) 写 BlockState（地图近似色）
        DyeColor dye = DyeApprox.nearestDye(rgb);
        BlockState newState = state.setValue(MAP_DYE, dye);

        // 只有真的变化才 setBlock，避免多余更新
        if (newState != state) {
            level.setBlock(pos, newState, level.isClientSide ? 2 : 3);
        }
    }

    /**
     * 实现手持色块或工具箱时快速挖掘且不掉落
     */

    // 定义“钥匙物品”的 TagKey（只定义一次）
    public static final TagKey<Item> BREAKERS =
            TagKey.create(Registries.ITEM,
                    new ResourceLocation("minebead", "color_block_breakers"));

    @Override
    public float getDestroyProgress(BlockState state,
                                    Player player,
                                    BlockGetter level,
                                    BlockPos pos) {

        // 创造模式始终瞬破（不影响）
        if (player.isCreative()) {
            return 1.0F;
        }

        ItemStack held = player.getMainHandItem();

        // ✅ 如果手持“钥匙物品”
        if (held.is(BREAKERS)) {
            return 1000.0F; // 近似瞬破
        }

        // ❌ 其他任何情况：极慢（≈黑曜石）
        return 0.002F; // 非常慢
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);

        var be = level.getBlockEntity(pos);
        if (be instanceof ColorBlockEntity colorBE) {
            ColorNbt.setColor(stack, colorBE.getColor());
        }

        return stack;
    }


}
