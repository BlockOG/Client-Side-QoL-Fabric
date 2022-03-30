package net.blockog.clientsideqol.client;

import net.blockog.clientsideqol.ClientSideQoL;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

public class RandomStuff {
    public static boolean canRMBItem(ItemStack item) {
        if (ClientSideQoL.getInstance().config.loyaltyTridentCheck && item.getItem() == Items.TRIDENT) {
            if (EnchantmentHelper.getRiptide(item) > 0) {
                return true;
            }
            return EnchantmentHelper.getLoyalty(item) > 0;
        }
        return true;
    }

    public static Hand[] handleItemUsage(@NotNull PlayerEntity player) {
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
}
