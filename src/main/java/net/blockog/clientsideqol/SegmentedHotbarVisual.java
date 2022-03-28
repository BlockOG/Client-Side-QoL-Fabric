package net.blockog.clientsideqol;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blockog.clientsideqol.client.config.CSQoLConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class SegmentedHotbarVisual {
    private static final Identifier WIDGETS_TEXTURE = new Identifier(ClientSideQoL.MOD_ID, "widgets.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean renderSegmentedHotbar(MatrixStack matrices) {
        CSQoLConfig config = ClientSideQoL.getInstance().config;

        if (!config.segmentedHotbarVisual || isHidden()) return false;

        PlayerEntity playerEntity = (PlayerEntity) client.cameraEntity;
        assert playerEntity != null;
        PlayerInventory inventory = playerEntity.getInventory();
        int scaledWidthHalved = client.getWindow().getScaledWidth() / 2 - 30;
        int scaledHeight = client.getWindow().getScaledHeight();

        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        // Draw the hotbar itself
        DrawableHelper.drawTexture(matrices,
            scaledWidthHalved - 65,
            scaledHeight - 22,
            0,
            24,
            190,
            22,
            256,
            64);

        int selectedSection = ClientSideQoL.getInstance().selectedHotbarSection;
        if (selectedSection == -1) { // Draw the regular vanilla selection box
            DrawableHelper.drawTexture(matrices,
                scaledWidthHalved - 66 + (inventory.selectedSlot * 20) + (4 * (inventory.selectedSlot / 3)),
                scaledHeight - 23,
                64,
                0,
                24,
                24,
                256,
                64);
        } else { // Draw the section-wide selection box
            DrawableHelper.drawTexture(matrices,
                scaledWidthHalved - 66 + (64 * selectedSection),
                scaledHeight - 23,
                0,
                0,
                64,
                24,
                256,
                64);
        }

        // Draw offhand slot
        ItemStack offHandStack = playerEntity.getOffHandStack();
        Arm arm = playerEntity.getMainArm().getOpposite();
        if (!offHandStack.isEmpty()) {
            if (arm == Arm.LEFT) {
                DrawableHelper.drawTexture(matrices, scaledWidthHalved - 91 + 1, scaledHeight - 23, 88, 0, 29, 24, 256, 64);
            } else {
                DrawableHelper.drawTexture(matrices, scaledWidthHalved + 91 + 30, scaledHeight - 23, 117, 0, 29, 24, 256, 64);
            }
        }

        // Draw hotbar items
        for (int slotNum = 0; slotNum < 9; slotNum++) {
            int x = scaledWidthHalved - 62 + (slotNum * 20) + (4 * (slotNum / 3));
            int y = scaledHeight - 19;
            ItemStack itemStack = inventory.getStack(slotNum);
            client.getItemRenderer().renderInGuiWithOverrides(itemStack, x, y);
            client.getItemRenderer().renderGuiItemOverlay(client.textRenderer, itemStack, x, y);
        }

        // Draw offhand item
        if (!offHandStack.isEmpty()) {
            int n = scaledHeight - 16 - 3;
            if (arm == Arm.LEFT) {
                client.getItemRenderer().renderInGuiWithOverrides(offHandStack, scaledWidthHalved - 91 + 4, n);
                client.getItemRenderer().renderGuiItemOverlay(client.textRenderer, offHandStack, scaledWidthHalved - 91 + 4, n);
            } else {
                client.getItemRenderer().renderInGuiWithOverrides(offHandStack, scaledWidthHalved + 91 + 40, n);
                client.getItemRenderer().renderGuiItemOverlay(client.textRenderer, offHandStack, scaledWidthHalved + 91 + 40, n);
            }
        }

        return true;
    }

    private static boolean isHidden() {
        if (
            client.interactionManager == null ||
                Objects.requireNonNull(client.player).isSpectator() ||
                client.options.hudHidden
        ) return true;

        PlayerEntity player = (PlayerEntity) client.cameraEntity;
        return player == null || !player.isAlive() || player.playerScreenHandler == null;
    }
}