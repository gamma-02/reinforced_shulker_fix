package net.gamma02.shulker_fix;

import atonkish.reinfcore.util.ReinforcedStorageScreenModel;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.api.event.StackItemOnItemCallback.Direction;
import svenhjol.charm.helper.TagHelper;
import svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * THIS CODE IS BASED OFF OF <a href="https://github.com/svenhjol/Charm/">CHARM</a>, PROVIDED "AS-IS" BY <a href="https://github.com/svenhjol/">svenhjol</a>
 */

public class ShulkerFix implements ModInitializer {

    public static final List<ItemLike> BLACKLIST = new ArrayList();


    public ShulkerFix(){

    }

    public static void handleWorldLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            Iterator var3 = TagHelper.getBlockValues(BlockTags.SHULKER_BOXES).iterator();

            while(var3.hasNext()) {
                Block block = (Block)var3.next();
                if (!BLACKLIST.contains(block)) {
                    BLACKLIST.add(block);
                }
            }
        }

    }

    public static boolean handleInventoryInteraction(Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) return false;

        if (direction == Direction.STACKED_ON_OTHER && source.isEmpty()) {
            return false;
        }

        if (!(Block.byItem(dest.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock)) {
            return false;
        }

        // check if the item is not in the blacklist
        Item item = source.getItem();
        Block block = Block.byItem(item);
        if (BLACKLIST.contains(item) || BLACKLIST.contains(block)) {
            return false;
        }



        if(!item.canFitInsideContainerItems()){//extra check for things like shulker boxes
            return false;
        }

        CompoundTag shulkerBoxTag = BlockItem.getBlockEntityData(dest);
        BlockEntity blockEntity;

        if (shulkerBoxTag == null) {
            // generate a new empty blockentity
            blockEntity = shulkerBoxBlock.newBlockEntity(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState());
        } else {
            // instantiate existing shulkerbox blockentity from BlockEntityTag
            blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState(), shulkerBoxTag);
        }

        if (!(blockEntity instanceof ShulkerBoxBlockEntity shulkerBox)) {
            return false;
        }


        int size = ShulkerBoxBlockEntity.CONTAINER_SIZE;
        if(blockEntity instanceof ReinforcedShulkerBoxBlockEntity reifShulkerBox){
            size = ReinforcedStorageScreenModel.getContainerInventorySize(reifShulkerBox.getMaterial(), false/*double shulker boxes KJDSKFLSJFKLDJLFKJS*/);
        }

//        if((source.getCount() != 1){
//            return false;
//        }


        // populate the container
        SimpleContainer container = new SimpleContainer(size);
        for (int i = 0; i < size; i++) {
            container.setItem(i, shulkerBox.getItem(i));
        }

        if (source.isEmpty()) {
            // empty out one item from the container
            int index = 0;
            for (int i = size - 1; i >= 0; i--) {
                if (!container.getItem(i).isEmpty()) {
                    index = i;
                }
            }
            ItemStack stack = container.getItem(index);
            if (stack.isEmpty()) return false;

            ItemStack out = stack.copy();
            container.setItem(index, ItemStack.EMPTY);
            InventoryTidyingHandler.mergeStacks(container); // merge to remove empty slots
            if (slot.safeInsert(out).isEmpty()) {
                playRemoveOneSound(player);
            }

        } else {
            // add hovering item into the container
            ItemStack result = container.addItem(source);
            source.setCount(result.getCount());

            if (result.getCount() == 0) {
                playInsertSound(player);
            }
        }

        // write container back to shulkerbox
        for (int i = 0; i < size; i++) {
            ItemStack stackInSlot = container.getItem(i);
            shulkerBox.setItem(i, stackInSlot);
        }

        shulkerBox.saveToItem(dest);
        return true;


    }
    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.getLevel().getRandom().nextFloat() * 0.4f);
        entity.playSound(SoundEvents.SHULKER_BOX_OPEN, 0.1f + entity.getLevel().getRandom().nextFloat() * 0.3f, 0.65f + entity.getLevel().getRandom().nextFloat() * 0.4f);
    }

    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.getLevel().getRandom().nextFloat() * 0.4f);
        entity.playSound(SoundEvents.SHULKER_BOX_OPEN, 0.1f + entity.getLevel().getRandom().nextFloat() * 0.3f, 0.67f + entity.getLevel().getRandom().nextFloat() * 0.4f);
    }

    @Override
    public void onInitialize() {

    }
}
