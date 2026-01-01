
why does this file have 65 errors: package com.ithurtz.magicx.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EnderPearlEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WindCharge;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WandItem extends Item {

    public enum Type { WIND, BLAZE, ENDER }

    private final Type type;

    public WandItem(Settings settings, Type type) {
        super(settings);
        this.type = type;
    }

    // Right-click use
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // require one magic essence in inventory
        if (!consumeEssence(user)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            switch (type) {
                case WIND:
                    // spawn a fast snowball to simulate a wind charge (no cooldown)
                    WindCharge wind = new WindCharge(world, user);
                    wind.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.0F, 1.0F);
                    world.spawnEntity(wind);
                    break;
                case BLAZE:
                    // spawn a small fireball (ghast-like) and apply cooldown
                    SmallFireballEntity fire = new SmallFireballEntity(world, user,
                            user.getRotationVec(1.0F).x, user.getRotationVec(1.0F).y, user.getRotationVec(1.0F).z);
                    fire.setPos(user.getX() + user.getRotationVec(1.0F).x,
                            user.getEyeY() + user.getRotationVec(1.0F).y,
                            user.getZ() + user.getRotationVec(1.0F).z);
                    world.spawnEntity(fire);
                    user.getItemCooldownManager().set(this, 40); // 2s cooldown
                    break;
                case ENDER:
                    // throw an ender pearl-like entity (teleport on hit)
                    EnderPearlEntity pearl = new EnderPearlEntity(world, user);
                    pearl.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                    world.spawnEntity(pearl);
                    user.getItemCooldownManager().set(this, 60); // 3s cooldown
                    break;
            }
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1f);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    // Small helper: consume one magic essence from player's inventory
    private boolean consumeEssence(PlayerEntity player) {
        for (int i = 0; i < player.inventory.size(); i++) {
            ItemStack s = player.inventory.getStack(i);
            if (s.getItem() == ModItems.MAGIC_ESSENCE) {
                s.decrement(1);
                return true;
            }
        }
        return false;
    }

    // Support right-click on blocks as well (convenience for some use cases)
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }
}

Show more
9:35 AM
The errors in your file are primarily due to incorrect import statements and type mismatches. Here are the main issues:

Primary Issues
Wrong Settings import (Line 8):
java
   import net.minecraft.server.dedicated.Settings;
Should be:

java
   import net.minecraft.item.Item.Settings;
WindCharge constructor issue (Line 42):
java
WindCharge wind = new WindCharge(world, user);
WindCharge likely doesn't have a constructor that takes (World, PlayerEntity). You may need to use WindChargeEntity or check the correct constructor signature for your Minecraft version.

SmallFireballEntity constructor issue (Line 47): The constructor parameters don't match the expected signature. Modern Minecraft versions typically require an EntityType parameter.
EnderPearlEntity constructor issue (Line 56): Similar to above - the constructor signature is likely incorrect for your Minecraft version.
        Quick Fix
Here's the corrected version with proper imports and constructor calls:

java
package com.ithurtz.magicx.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WandItem extends Item {

    public enum Type { WIND, BLAZE, ENDER }

    private final Type type;

    public WandItem(Settings settings, Type type) {
        super(settings);
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!consumeEssence(user)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            switch (type) {
                case WIND:
                    WindChargeEntity wind = new WindChargeEntity(world, user,
                            user.getRotationVec(1.0F).x,
                            user.getRotationVec(1.0F).y,
                            user.getRotationVec(1.0F).z);
                    wind.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.0F, 1.0F);
                    world.spawnEntity(wind);
                    break;
                case BLAZE:
                    SmallFireballEntity fire = new SmallFireballEntity(world, user,
                            user.getRotationVec(1.0F).x * 2,
                            user.getRotationVec(1.0F).y * 2,
                            user.getRotationVec(1.0F).z * 2);
                    world.spawnEntity(fire);
                    user.getItemCooldownManager().set(this, 40);
                    break;
                case ENDER:
                    EnderPearlEntity pearl = new EnderPearlEntity(world, user);
                    pearl.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                    world.spawnEntity(pearl);
                    user.getItemCooldownManager().set(this, 60);
                    break;
            }
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.PLAYERS, 0.5f, 1f);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    private boolean consumeEssence(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.getItem() == ModItems.MAGIC_ESSENCE) {
                s.decrement(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }
}
