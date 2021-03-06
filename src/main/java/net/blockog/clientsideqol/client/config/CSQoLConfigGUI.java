package net.blockog.clientsideqol.client.config;

import net.blockog.clientsideqol.ClientSideQoL;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CSQoLConfigGUI {

    CSQoLConfig config = ClientSideQoL.getInstance().config;

    public Screen getConfigScreen(Screen parent, boolean isTransparent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.of("Client Side QoL"));
        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/dirt.png"));
        builder.setSavingRunnable(() -> ClientSideQoL.getInstance().saveConfig());
        ConfigEntryBuilder configEntryBuilder = builder.entryBuilder();

        // Segmented Hotbar
        ConfigCategory segmentedHotbar = builder.getOrCreateCategory(Text.of("Segmented Hotbar"));

        segmentedHotbar.addEntry(
            configEntryBuilder
                .startBooleanToggle(Text.of("Segmented Hotbar Visual"), config.segmentedHotbarVisual)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.setSegmentedHotbarVisual(newValue))
                .setTooltip(Text.of("Divide hotbar into 3 sections\n\nDefault: No"))
                .build()
        );

        segmentedHotbar.addEntry(
            configEntryBuilder
                .startBooleanToggle(Text.of("Segmented Hotbar Function"), config.segmentedHotbarFunction)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.setSegmentedHotbarFunction(newValue))
                .setTooltip(Text.of("Make the first press of number keys 1-3 choose section 1-3\nand second press choose the slot from that section\n\nDefault: No"))
                .build()
        );

        // Main hand tweaks
        ConfigCategory mainHandTweaks = builder.getOrCreateCategory(Text.of("Main Hand Tweaks"));

        mainHandTweaks.addEntry(
            configEntryBuilder
                .startBooleanToggle(Text.of("Loyalty Trident Check"), config.loyaltyTridentCheck)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.setLoyaltyTridentCheck(newValue))
                .setTooltip(Text.of("Doesn't allow throwing a trident without loyalty\n\nDefault: Yes"))
                .build()
        );
        mainHandTweaks.addEntry(
            configEntryBuilder
                .startBooleanToggle(Text.of("Rocket Boost From Inventory"), config.rocketBoostFromInventory)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.setRocketBoostFromInventory(newValue))
                .setTooltip(Text.of("When using elytra, jump to boost using rocket from inventory\n\nDefault: No"))
                .build()
        );

        return builder.setTransparentBackground(isTransparent).build();

    }

}
