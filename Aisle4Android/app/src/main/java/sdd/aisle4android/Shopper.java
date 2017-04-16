package sdd.aisle4android;

import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 29/03/2017.
 */

class Shopper {
    EventStartShopping eventStartShopping = new EventStartShopping();
    EventStopShopping eventStopShopping = new EventStopShopping();
    EventLocationUpdated eventLocationUpdated = new EventLocationUpdated(); // TODO: fire this event on item collected

    // Shopper State
    private List<ShopList> shopLists;
    private boolean isShopping = false;
    private ShopList activeShopList;
    private ShopItem nearestItem = null;
    private long shopStartTimeMs;
    private Context context;

    Shopper(Context context) {
        this.context = context;
        LocalDatabaseHelper db = new LocalDatabaseHelper(this.context);
        shopLists = db.getAllLists();
        db.close();
    }


    // PUBLIC ACCESSORS

    boolean isShopping() {
        return isShopping;
    }
    ShopList getActiveList() {
        return activeShopList;
    }
    ShopItem getNearestItem() { return nearestItem; }
    /** @return time since started shopping in ms */
    long getShoppingTime() {
        long currentMs = SystemClock.elapsedRealtime();
        return currentMs - shopStartTimeMs;
    }
    ShopList getShopList(int index) {
        return shopLists.get(index);
    }
    List<ShopList> getShopLists() {
        return shopLists; // TODO: make unmodifiable? and each item?
    }


    // PUBLIC MODIFIERS

    void startShopping(ShopList list) {
        isShopping = true;
        activeShopList = list;
        shopStartTimeMs = SystemClock.elapsedRealtime();
        eventStartShopping.fire(this);
    }
    void endShopping() {
        isShopping = false;
        activeShopList = null;
        eventStopShopping.fire(this);
    }
    void addShopList(ShopList list) {
        shopLists.add(list);
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.addList(list);
        db.close();
    }
    void removeShopList(ShopList list) {
        shopLists.remove(list);
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.deleteList(list);
        db.close();
    }
    void removeShopList(int index) {
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.deleteList(shopLists.get(index));
        db.close();
        shopLists.remove(index);
    }



    /*void shopListRename(ShopList list, String name) {
        list.rename(name);
    }
    void addShopItem(ShopList list, ShopItem item) {
        list.addItem(item);
    }
    void removeItem(ShopList list, int position) {
        ShopItem item = list.getItem(position);
        list.removeItem(item);
    }
    void setCollected(ShopItem item, boolean collected) {
        item.setCollected(collected);
    }*/



    // TODO: Improve event system
    class EventStartShopping extends Event<IEventStartShopListener> {
        void fire(Shopper shopper) {
            for (IEventStartShopListener listener : listeners) {
                listener.onStartShopping(shopper);
            }
        }
    }
    class EventStopShopping extends Event<IEventStopShopListener> {
        void fire(Shopper shopper) {
            for (IEventStopShopListener listener : listeners) {
                listener.onStopShopping(shopper);
            }
        }
    }
    class EventLocationUpdated extends Event<IEventLocationUpdatedListener> {
        void fire(Shopper shopper, ShopItem nearestItem) {
            for (IEventLocationUpdatedListener listener : listeners) {
                listener.onLocationUpdated(shopper, nearestItem);
            }
        }
    }
    interface IEventStartShopListener {
        void onStartShopping(Shopper shopper);
    }
    interface IEventStopShopListener {
        void onStopShopping(Shopper shopper);
    }
    interface IEventLocationUpdatedListener {
        void onLocationUpdated(Shopper shopper, ShopItem nearestItem);
    }
}
