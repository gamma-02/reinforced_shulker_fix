package net.gamma02.shulker_fix.mixin;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.gamma02.shulker_fix.ShulkerFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.api.event.StackItemOnItemCallback;
import svenhjol.charm.module.shulker_box_drag_drop.ShulkerBoxDragDrop;

@Mixin(ShulkerBoxDragDrop.class)
public class ShulkerBoxDragDropModuleMixin<T> {


    @Redirect(method = "runWhenEnabled", at = @At(value="INVOKE", target = "Lnet/fabricmc/fabric/api/event/Event;register(Ljava/lang/Object;)V"), remap = false)
    public void injectFix(Event<T> instance, T t){
        if(instance == StackItemOnItemCallback.EVENT){
            StackItemOnItemCallback.EVENT.register(ShulkerFix::handleInventoryInteraction);
            System.out.println("SHULKER BOX BEHAVIOR ADDED!");
        }else if(instance == ServerWorldEvents.LOAD){
            ServerWorldEvents.LOAD.register(ShulkerFix::handleWorldLoad);
            System.out.println("WORLD LOAD BEHAVIOR ADDED!");

        }


    }
}
