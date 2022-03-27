package net.blockog.clientsideqol.mixin;

import net.blockog.clientsideqol.SegmentedHotbarVisual;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 99)
@Environment(EnvType.CLIENT)
public class SHVMixin {
    @Inject(method = "renderHotbar", at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void inventorioRenderSegmentedHotbar(float tickDelta, MatrixStack matrixStack, CallbackInfo ci)
    {
        if (SegmentedHotbarVisual.renderSegmentedHotbar(matrixStack))
            ci.cancel();
    }
}
