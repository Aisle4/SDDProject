package sdd.aisle4android;

/**
 * Created by Robert Wild on 14/03/2017.
 */

public class ShopItem {
    public Event<ShopItem> eventCollected = new Event<ShopItem>();

    private String name;
    private boolean collected = false;


    public ShopItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
        eventCollected.fire(this);
    }
}
