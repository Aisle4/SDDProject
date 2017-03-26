package sdd.aisle4android;

/**
 * Created by Robert Wild on 26/03/2017.
 */

public class ItemToItemData {
    public ShopItem item1;
    public ShopItem item2;
    public long time;
    public int steps;

    public ItemToItemData(ShopItem item1, ShopItem item2, long time, int steps) {
        this.item1 = item1;
        this.item2 = item2;
        this.time = time;
        this.steps = steps;
    }
}
