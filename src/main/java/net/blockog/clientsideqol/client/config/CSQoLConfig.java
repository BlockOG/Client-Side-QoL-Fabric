package net.blockog.clientsideqol.client.config;

public class CSQoLConfig {

    public boolean segmentedHotbarVisual = false;
    public boolean segmentedHotbarFunction = false;

    public boolean loyaltyTridentCheck = true;
    public boolean rocketBoostFromInventory = false;

    public void setSegmentedHotbarVisual(boolean segmentedHotbarVisual) {
        this.segmentedHotbarVisual = segmentedHotbarVisual;
    }
    public void setSegmentedHotbarFunction(boolean segmentedHotbarFunction) {
        this.segmentedHotbarFunction = segmentedHotbarFunction;
    }

    public void setLoyaltyTridentCheck(boolean loyaltyTridentCheck) {
        this.loyaltyTridentCheck = loyaltyTridentCheck;
    }
    public void setRocketBoostFromInventory(boolean rocketBoostFromInventory) {
        this.rocketBoostFromInventory = rocketBoostFromInventory;
    }

}
