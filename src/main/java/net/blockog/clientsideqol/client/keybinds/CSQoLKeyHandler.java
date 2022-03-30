package net.blockog.clientsideqol.client.keybinds;

import net.blockog.clientsideqol.ClientSideQoL;
import net.blockog.clientsideqol.client.config.CSQoLConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

public class CSQoLKeyHandler {
    private static boolean spaceHeld = false;

    private static ClientPlayerInteractionManager man;
    private static final CSQoLKeyHandler instance = new CSQoLKeyHandler();

    public static CSQoLKeyHandler getInstance() {
        return instance;
    }

    public boolean handleSegmentedHotbarSlotSelection(PlayerInventory inventory, int slotToSelect) {
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

    public static void handleRocketBoostKey(ClientPlayerEntity player) {
        if (ClientSideQoL.getInstance().config.rocketBoostFromInventory) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                if (tryFireRocket(i, player)) {
                    return;
                }
            }
        }
    }

    private static boolean tryFireRocket(int stackSlot, @NotNull ClientPlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.getStack(stackSlot).getItem() == Items.FIREWORK_ROCKET) {
            swap(player, stackSlot, inventory.selectedSlot + 36);
            man.interactItem(player, player.getWorld(), Hand.MAIN_HAND);
            swap(player, stackSlot, inventory.selectedSlot + 36);
            return true;
        }
        return false;
    }

    public static void swap(@NotNull ClientPlayerEntity player, int slot1, int slot2) {
        man.clickSlot(0, slot2, slot1, SlotActionType.SWAP, player);
    }

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        man = client.interactionManager;
        if (player != null) {
            if (player.isFallFlying()) {
                if (client.options.jumpKey.isPressed() && !spaceHeld) {
                    spaceHeld = true;
                    handleRocketBoostKey(player);
                } else if (!client.options.jumpKey.isPressed()) {
                    spaceHeld = false;
                }
            } else {
                spaceHeld = true;
            }
        }
    }
}
