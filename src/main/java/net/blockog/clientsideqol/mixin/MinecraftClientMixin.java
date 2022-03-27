package net.blockog.clientsideqol.mixin;

import net.blockog.clientsideqol.ClientSideQoL;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.util.Hand;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow public ClientPlayerEntity player;
    @Shadow @Final public GameOptions options;
    @Shadow @Nullable public Screen currentScreen;

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
                    return !ClientSideQoL.getInstance().handleSegmentedHotbarSlotSelection(player.getInventory(), i);
            }
        return true;
    }

    @Redirect(method = "doItemUse",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Hand;values()[Lnet/minecraft/util/Hand;"))
    private Hand[] inventorioDoItemUse()
    {
        if (player == null)
            return new Hand[]{};
        return ClientSideQoL.getInstance().handleItemUsage(player);
    }
}