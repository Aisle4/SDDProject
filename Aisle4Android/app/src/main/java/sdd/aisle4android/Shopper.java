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

    // Shopper State
    private List<ShopList> shopLists;
    private boolean isShopping = false;
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
    // Returns time since started shopping in ms
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
        shopStartTimeMs = SystemClock.elapsedRealtime();
        eventStartShopping.fire(list);
    }
    void endShopping() {
        isShopping = false;
        eventStopShopping.fire();
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
    void shopListRename(ShopList list, String name) {
        list.rename(name);
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.updateList(list);
        db.close();
    }
    void addShopItem(ShopList list, ShopItem item) {
        list.addItem(item);
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.addItem(list, item);
        db.close();
    }
    void removeItem(ShopList list, int position) {
        ShopItem item = list.getItem(position);
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.deleteItem(item);
        db.close();
        list.removeItem(item);
    }
    void setCollected(ShopItem item, boolean collected) {
        item.setCollected(collected);
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.updateItem(item);
        db.close();
    }



    // TODO: Improve event system
    class EventStartShopping extends Event<IEventListener> {
        void fire(ShopList list) {
            for (IEventListener listener : listeners) {
                listener.onStartShopping(list);
            }
        }
    }
    class EventStopShopping extends Event<IEventListener> {
        void fire() {
            for (IEventListener listener : listeners) {
                listener.onStopShopping();
            }
        }
    }
    interface IEventListener {
        void onStartShopping(ShopList list);
        void onStopShopping();
    }
}
