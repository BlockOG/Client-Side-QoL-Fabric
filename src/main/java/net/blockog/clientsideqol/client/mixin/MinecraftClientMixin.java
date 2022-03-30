package net.blockog.clientsideqol.client.mixin;

import net.blockog.clientsideqol.client.RandomStuff;
import net.blockog.clientsideqol.client.keybinds.CSQoLKeyHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow public ClientPlayerEntity player;
    @Shadow @Final public GameOptions options;
    @Shadow @Nullable public Screen currentScreen;

    @Redirect(
        method = "handleInputEvents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z",
            ordinal = 2
        )
    )
    private boolean handleHotbarSlotSelection(KeyBinding keyBinding) {
        if (!keyBinding.wasPressed())
            return false;
        if (player.isSpectator())
            return true;

        if (!player.isCreative() || currentScreen != null || (!options.saveToolbarActivatorKey.isPressed() && !options.loadToolbarActivatorKey.isPressed()))
            for (int i = 0; i < 9; ++i) {
                if (keyBinding == options.hotbarKeys[i]) {
                    return !CSQoLKeyHandler.getInstance().handleSegmentedHotbarSlotSelection(player.getInventory(), i);
                }
            }
        return true;
    }

    @Redirect(
        method = "doItemUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Hand;values()[Lnet/minecraft/util/Hand;"
        )
    )
    private Hand[] doItemUse() {
        if (player == null)
            return new Hand[]{};
        return RandomStuff.handleItemUsage(player);
    }
}