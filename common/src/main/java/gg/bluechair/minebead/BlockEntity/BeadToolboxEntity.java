package gg.bluechair.minebead.BlockEntity;


import gg.bluechair.minebead.util.toolbox.ToolboxNbt;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import registry.ModBlockEntities;

public class BeadToolboxEntity extends BlockEntity {
    private ItemStack boundStack = ItemStack.EMPTY; // 用一个 ItemStack 容器存 NBT 最省事

    public BeadToolboxEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEAD_TOOLBOX_ENTITY.get(), pos, state);
    }

    public void bindFromItem(ItemStack stack) {
        // 拷贝一份 NBT（避免引用同一个对象）
        this.boundStack = stack.copy();
        this.boundStack.setCount(1);
        setChanged();
    }

    public ItemStack toBoundItem(ItemStack base) {
        ItemStack out = base.copy();
        out.setCount(1);
        if (!boundStack.isEmpty() && boundStack.hasTag()) {
            out.setTag(boundStack.getTag().copy());
        }
        return out;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!boundStack.isEmpty()) {
            tag.put("BoundStack", boundStack.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("BoundStack", CompoundTag.TAG_COMPOUND)) {
            boundStack = ItemStack.of(tag.getCompound("BoundStack"));
        } else {
            boundStack = ItemStack.EMPTY;
        }
    }
}