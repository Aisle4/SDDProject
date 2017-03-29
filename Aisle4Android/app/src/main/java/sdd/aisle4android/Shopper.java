package sdd.aisle4android;

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


    Shopper() {
        shopLists = new ArrayList<>();
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
    }
    void removeShopList(ShopList list) {
        shopLists.remove(list);
    }
    void removeShopList(int index) {
        shopLists.remove(index);
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
