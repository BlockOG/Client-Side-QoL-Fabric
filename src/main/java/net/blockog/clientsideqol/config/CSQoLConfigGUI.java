package net.blockog.clientsideqol.config;

import net.blockog.clientsideqol.ClientSideQoL;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class CSQoLConfigGUI {

    CSQoLConfig config = ClientSideQoL.getInstance().config;

    public Screen getConfigScreen(Screen parent, boolean isTransparent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(new TranslatableText("text.config.ClientSideQoL.title"));
        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/dirt.png"));
        builder.setSavingRunnable(() -> ClientSideQoL.getInstance().saveConfig());
        ConfigCategory general = builder.getOrCreateCategory(Text.of("general"));
        ConfigEntryBuilder configEntryBuilder = builder.entryBuilder();

        general.addEntry(
                configEntryBuilder
                        .startBooleanToggle(Text.of("Segmented Hotbar"), config.segmentedHotbar)
                        .setDefaultValue(false)
                        .setSaveConsumer(newValue -> config.setSegmentedHotbar(newValue))
                        .setTooltip(Text.of("Divide hotbar into 3 sections, making it easy to select a slot with just 1-3\nDefault = false"))
                        .build()
        );
        return builder.setTransparentBackground(isTransparent).build();

    }

}
