package net.blockog.clientsideqol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blockog.clientsideqol.config.CSQoLConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientSideQoL implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("ClientSideQoL");
    public CSQoLConfig config;
    private static ClientSideQoL instance;

    public static ClientSideQoL getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        loadConfig();
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