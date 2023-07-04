package net.gamma02.shulker_fix;

import atonkish.reinfcore.util.ReinforcedStorageScreenModel;
import atonkish.reinfshulker.block.entity.ReinforcedShulkerBoxBlockEntity;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.gui.widget.ModListWidget;
import com.terraformersmc.modmenu.gui.widget.entries.ModListEntry;
import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.FabricLoader;
import net.gamma02.shulker_fix.CharmCompat.CharmCompatFix;
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

    public static boolean isCharmLoaded = false;



    public ShulkerFix(){
        if(FabricLoader.INSTANCE.isModLoaded("charm")) {

            try {
                ClassLoader.getPlatformClassLoader().loadClass("net.gamma02.shulker_fix.CharmCompat.CharmCompatFix");
            } catch (ClassNotFoundException e) {

                System.out.println("OOPS I FUCKED UP SOMEHOW :D");
                System.out.println(e + " " + e.getMessage());
            }
        }else{
            isCharmLoaded = false;
        }
    }



    @Override
    public void onInitialize() {

    }
}
