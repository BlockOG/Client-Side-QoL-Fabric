package net.blockog.clientsideqol.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;

public class CSQoLModMenuIntegration implements ModMenuApi{

    CSQoLConfigGUI configGUI = new CSQoLConfigGUI();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configGUI.getConfigScreen(parent, MinecraftClient.getInstance().world != null);
    }

}
