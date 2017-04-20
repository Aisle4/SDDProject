package sdd.aisle4android;

/**
 * Created by Robert Wild on 26/03/2017.
 */

class ItemToItemData {
    String item1Name;
    String item2Name;
    long timeMs;
    int steps;

    ItemToItemData(String item1Name, String item2Name, long timeMs, int steps) {
        this.item1Name = item1Name;
        this.item2Name = item2Name;
        this.timeMs = timeMs;
        this.steps = steps;
    }
    ItemToItemData(ShopItem item1, ShopItem item2, long timeMs, int steps) {
        this.item1Name = item1 == null ? "" : item1.getName();
        this.item2Name = item2 == null ? "" : item2.getName();
        this.timeMs = timeMs;
        this.steps = steps;
    }
}
