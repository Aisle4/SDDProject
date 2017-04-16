package sdd.aisle4android;

/**
 * Created by Robert Wild on 26/03/2017.
 */

class ItemToItemData {
    String item1Name;
    String item2Name;
    long time;
    int steps;

    ItemToItemData(String item1Name, String item2Name, long time, int steps) {
        this.item1Name = item1Name;
        this.item2Name = item2Name;
        this.time = time;
        this.steps = steps;
    }
}
