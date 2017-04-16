package sdd.aisle4android;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Robert Wild on 05/04/2017.
 */

class ItemOrderer {
    private List<ItemToItemData> data;
    private DatabaseManager dbManager;

    public ItemOrderer(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        data = new ArrayList<>();
    }
    public ItemOrderer(DatabaseManager dbManager, List<ItemToItemData> data) {
        this.dbManager = dbManager;
        this.data = data;
    }

    public void onStartShopping(ShopList list) {

    }

    // TODO: make private - how to unit test?
    public void orderList(ShopList list) {
        ShopItem closestItem = null; // Store entrance
        Collections.sort(list.getItems(), new ComparatorItemDist(closestItem));
    }

    private float getItemDistance(ShopItem item1, ShopItem item2) {
        float dist = 0;
        int count = 0;
        for (ItemToItemData dat : data) {
            if ((dat.item1 == item1 && dat.item2 == item2) ||
                (dat.item1 == item2 && dat.item2 == item1)) {

                // Found data for these items
                dist += dat.time;
                ++count;
            }
        }
        if (count == 0) {
            return 500; // TODO: no data case
        }

        dist /= count;
        return dist;
    }
    private void debugLogList(List<ShopItem> list) {
        for (ShopItem item : list) {
            Log.d("debug", item.getName());
        }
    }

    private class ComparatorItemDist implements Comparator<ShopItem> {
        private ShopItem closestItem;

        ComparatorItemDist(ShopItem closestItem) {
            this.closestItem = closestItem;
        }

        @Override
        public int compare(ShopItem item1, ShopItem item2) {
            return java.lang.Float.compare(getItemDistance(item1, closestItem), getItemDistance(item2, closestItem));
        }
    }
}
