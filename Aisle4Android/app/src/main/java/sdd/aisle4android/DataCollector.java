package sdd.aisle4android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 26/03/2017.
 */

public class DataCollector implements TheApp.IEventListener, ShopList.IEventListener {
    private ShopItem lastItem = null;
    private long lastCollectTime = 0;
    private int stepsSinceLast = 0;
    private List<ItemToItemData> data = new ArrayList<ItemToItemData>();

    private ShopList listInUse;


    public DataCollector(TheApp app) {
        app.eventStartShopping.attach(this);
    }

    @Override
    public void onStartShopping(ShopList list) {
        lastCollectTime = System.currentTimeMillis();
        stepsSinceLast = 0;
        lastItem = null;

        // Events
        if (listInUse != null) {
            list.eventItemCollected.dettach(this);
        }
        list.eventItemCollected.attach(this);
        listInUse = list;
    }
    @Override
    public void onItemCollected(ShopItem item) {
        long time = System.currentTimeMillis() - lastCollectTime;
        data.add(new ItemToItemData(lastItem, item, time, stepsSinceLast));
        lastItem = item;
    }
}
