package net.blockog.clientsideqol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blockog.clientsideqol.client.config.CSQoLConfig;
import net.blockog.clientsideqol.client.keybinds.CSQoLKeyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ClientSideQoL implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ClientSideQoL");
    public static final String MOD_ID = "clientsideqol";
    public CSQoLConfig config;
    private static ClientSideQoL instance;
    public int selectedHotbarSection = -1;

    public static ClientSideQoL getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        loadConfig();
        ClientTickEvents.START_CLIENT_TICK.register(client -> CSQoLKeyHandler.tick());

        instance = this;
    }

    public void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ClientSideQoL.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (configFile.exists()) {
            try {
                FileReader fileReader = new FileReader(configFile);
                config = gson.fromJson(fileReader, CSQoLConfig.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load Client Side QoL config options: " + e.getLocalizedMessage());
            }
        } else {
            config = new CSQoLConfig();
            saveConfig();
        }
    }

    public void saveConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ClientSideQoL.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(gson.toJson(config));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save Client Side QoL config options: " + e.getLocalizedMessage());
        }
    }
}