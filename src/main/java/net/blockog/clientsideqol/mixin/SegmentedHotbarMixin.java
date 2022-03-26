package net.blockog.clientsideqol.mixin;

// import net.blockog.clientsideqol.ClientSideQoL;
// import net.blockog.clientsideqol.config.CSQoLConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MinecraftClient.class)
public class SegmentedHotbarMixin {

   // CSQoLConfig config = ClientSideQoL.getInstance().config;
    int section = 0;
    int slot = 0;

    @Nullable
    public Screen currentScreen;
    @Nullable
    public ClientPlayerEntity player;

    @ModifyVariable(
        method = "handleInputEvents()V",
        at = @At(value = "LOAD"),
        name = "i",
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z", ordinal = 0),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;onHotbarKeyPress(Lnet/minecraft/client/MinecraftClient;IZZ)V")
        )
    )
    private int changeI(int i) {
      //  if (config.segmentedHotbarFunction) {
            if (i < 3) {
                if (section == 0) {
                    section = i;
                    if (player != null) {
                        if (!this.player.isCreative() || this.currentScreen != null) {
                            return player.getInventory().selectedSlot;
                        }
                    }
                } else {
                    slot = (section - 1) * i;
                    section = 0;
                    return slot;
                }
            } else {
                if (player != null) {
                    if (!this.player.isCreative() || this.currentScreen != null) {
                        return player.getInventory().selectedSlot;
                    }
                }
            }
     //   } else {
     //       return i;
     //   }
        return 0;
    }

}
