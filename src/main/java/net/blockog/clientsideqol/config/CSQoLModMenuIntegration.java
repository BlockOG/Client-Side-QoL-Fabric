package net.blockog.clientsideqol.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class CSQoLModMenuIntegration implements ModMenuApi {

    CSQoLConfigGUI configGUI = new CSQoLConfigGUI();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configGUI.getConfigScreen(parent, MinecraftClient.getInstance().world != null);
    }

}
