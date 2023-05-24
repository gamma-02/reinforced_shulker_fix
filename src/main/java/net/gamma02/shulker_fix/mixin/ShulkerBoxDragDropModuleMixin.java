package net.gamma02.shulker_fix.mixin;


import net.fabricmc.fabric.api.event.Event;
import net.gamma02.shulker_fix.ShulkerFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.api.event.StackItemOnItemCallback;
import svenhjol.charm.module.shulker_box_drag_drop.ShulkerBoxDragDrop;

@Mixin(ShulkerBoxDragDrop.class)
public class ShulkerBoxDragDropModuleMixin<T> {


    @Redirect(method = "runWhenEnabled", at = @At(value="INVOKE", target = "Lnet/fabricmc/fabric/api/event/Event;register(Ljava/lang/Object;)V", ordinal = 0), remap = false)
    public void injectFix(Event<T> instance, T t){
        if(instance != StackItemOnItemCallback.EVENT){
            System.out.println("whops! redirected a call to the wrong event! registering... name: " + instance.toString());
            instance.register(t);
            return;
        }
        StackItemOnItemCallback.EVENT.register(ShulkerFix::handleInventoryInteraction);


    }
}
