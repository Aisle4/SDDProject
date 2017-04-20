package sdd.aisle4android.Model;

/**
 * Created by Robert Wild on 26/03/2017.
 */

public class ItemToItemData {
    public String item1Name;
    public String item2Name;
    public long timeMs;
    public int steps;

    public ItemToItemData(String item1Name, String item2Name, long timeMs, int steps) {
        this.item1Name = item1Name;
        this.item2Name = item2Name;
        this.timeMs = timeMs;
        this.steps = steps;
    }
    ItemToItemData(ShopItem item1, ShopItem item2, long timeMs, int steps) {

        this.item1Name = item1 == null ? DataCollector.STORE_ENTRANCE_NAME : item1.getName();
        this.item2Name = item2 == null ? DataCollector.STORE_ENTRANCE_NAME : item2.getName();
        this.timeMs = timeMs;
        this.steps = steps;
    }
}
