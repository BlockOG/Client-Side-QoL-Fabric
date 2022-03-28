package net.blockog.clientsideqol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blockog.clientsideqol.client.config.CSQoLConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
        instance = this;
    }

    public boolean canRMBItem(ItemStack item) {
        if (config.loyaltyTridentCheck && item.getItem() == Items.TRIDENT) {
            return EnchantmentHelper.getLoyalty(item) > 0;
        }
        return true;
    }

    public boolean handleSegmentedHotbarSlotSelection(PlayerInventory inventory, int slotToSelect) {
        CSQoLConfig config = this.config;
        if (!config.segmentedHotbarFunction)
            return false;
        if (slotToSelect > 2)
            return true;

        if (this.selectedHotbarSection == -1) {
            this.selectedHotbarSection = slotToSelect;
        } else {
            inventory.selectedSlot = slotToSelect + 3 * this.selectedHotbarSection;
            this.selectedHotbarSection = -1;
        }
        return true;
    }

    public Hand[] handleItemUsage(@NotNull PlayerEntity player) {
        boolean useMainHand = canRMBItem(player.getStackInHand(Hand.MAIN_HAND));
        boolean useOffHand = canRMBItem(player.getStackInHand(Hand.OFF_HAND));

        Hand[] ret;

        if (useMainHand && useOffHand) {
            ret = new Hand[2];
            ret[0] = Hand.MAIN_HAND;
            ret[1] = Hand.OFF_HAND;
        } else if (useMainHand) {
            ret = new Hand[1];
            ret[0] = Hand.MAIN_HAND;
        } else if (useOffHand) {
            ret = new Hand[1];
            ret[0] = Hand.OFF_HAND;
        } else {
            ret = new Hand[0];
        }

        return ret;
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