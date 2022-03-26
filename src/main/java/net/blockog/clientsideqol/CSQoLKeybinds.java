package net.blockog.clientsideqol;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class CSQoLKeybinds implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBinding binding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.category.first.test"));
        KeyBinding binding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.category.second.test"));
        KeyBinding stickyBinding = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_sticky", GLFW.GLFW_KEY_R, "key.category.first.test", () -> true));
        KeyBinding duplicateBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_duplicate", GLFW.GLFW_KEY_LEFT_SHIFT, "key.category.first.test"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (binding1.wasPressed()) {
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("Key 1 was pressed!"), false);
                }
            }

            while (binding2.wasPressed()) {
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("Key 2 was pressed!"), false);
                }
            }

            if (stickyBinding.isPressed()) {
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("Sticky Key was pressed!"), false);
                }
            }

            while (duplicateBinding.wasPressed()) {
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("Duplicate Key was pressed!"), false);
                }
            }
        });
    }
}