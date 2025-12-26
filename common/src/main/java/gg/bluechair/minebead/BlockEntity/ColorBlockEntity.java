package gg.bluechair.minebead.BlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import registry.ModBlockEntities;

public class ColorBlockEntity extends BlockEntity {

    private int color = 0xFFFFFF; // 默认白色更合理

    public ColorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COLOR_BLOCK_ENTITY.get(), pos, state);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color & 0xFFFFFF;
        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 11);
        }
    }

    /* ===================== */
    /* ====== NBT 存储 ===== */
    /* ===================== */

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Color", color);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        int old = this.color;
        if (tag.contains("Color")) {
            this.color = tag.getInt("Color");
        }

        // 关键：客户端收到更新后，强制刷新渲染
        if (level != null && level.isClientSide && old != this.color) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 8);
        }
    }

    /* ===================== */
    /* ====== 客户端同步 ===== */
    /* ===================== */

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
