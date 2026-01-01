package com.ithurtz.magicx.block;

import com.ithurtz.magicx.item.ModItems;
import com.ithurtz.magicx.magicx;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class ExtractorBlockEntity extends BlockEntity implements Tickable {

    private static final int MAX_ESSENCE = 5;
    private int storedEssence = MAX_ESSENCE; // starts full
    private int tickCounter = 0;

    public ExtractorBlockEntity() {
        super(magicx.EXTRACTOR_BLOCK_ENTITY);
    }

    // Called each tick on server
    @Override
    public void tick() {
        if (world == null || world.isClient) return;
        tickCounter++;
        // every 1200 ticks (~1 minute) regen one essence up to MAX_ESSENCE
        if (tickCounter >= 1200) {
            tickCounter = 0;
            if (storedEssence < MAX_ESSENCE) {
                storedEssence++;
                markDirty();
            }
        }
    }

    // Try to give one essence to player, return true if success
    public boolean extractOneToPlayer(PlayerEntity player) {
        if (storedEssence <= 0) return false;
        ItemStack stack = new ItemStack(ModItems.MAGIC_ESSENCE);
        boolean added = player.inventory.insertStack(stack);
        if (!added) {
            // drop into world if inventory full
            player.dropItem(stack, false);
        }
        storedEssence--;
        markDirty();
        return true;
    }

    // NBT save/load
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.storedEssence = tag.getInt("storedEssence");
        this.tickCounter = tag.getInt("tickCounter");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("storedEssence", this.storedEssence);
        tag.putInt("tickCounter", this.tickCounter);
        return super.toTag(tag);
    }
}
