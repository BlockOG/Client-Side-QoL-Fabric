package net.blockog.clientsideqol.mixin;

import net.blockog.clientsideqol.ClientSideQoL;
import net.blockog.clientsideqol.config.CSQoLConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class SegmentedHotbarFunction {

    @Shadow public ClientPlayerEntity player;
    @Shadow @Final public GameOptions options;
    @Shadow @Nullable public Screen currentScreen;

    private boolean handleSegmentedHotbarSlotSelection(PlayerInventory inventory, int slotToSelect) {
        ClientSideQoL csqol = ClientSideQoL.getInstance();
        CSQoLConfig config = csqol.config;
        if (!config.segmentedHotbarFunction)
            return false;
        if (slotToSelect > 2)
            return true;

        if (csqol.selectedHotbarSection == -1) {
            csqol.selectedHotbarSection = slotToSelect;
        } else {
            inventory.selectedSlot = slotToSelect + 3 * csqol.selectedHotbarSection;
            csqol.selectedHotbarSection = -1;
        }
        return true;
    }

    @Redirect(method = "handleInputEvents",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z",
            ordinal = 2))
    private boolean handleHotbarSlotSelection(KeyBinding keyBinding) {
        if (!keyBinding.wasPressed())
            return false;
        if (player.isSpectator())
            return true;

        if (!player.isCreative() || currentScreen != null || (!options.saveToolbarActivatorKey.isPressed() && !options.loadToolbarActivatorKey.isPressed()))
            for (int i = 0; i < 9; ++i) {
                if (keyBinding == options.hotbarKeys[i])
                    return !handleSegmentedHotbarSlotSelection(player.getInventory(), i);
            }
        return true;
    }
}