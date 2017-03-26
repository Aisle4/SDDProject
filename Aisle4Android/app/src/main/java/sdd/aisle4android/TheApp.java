package sdd.aisle4android;

import android.app.Application;
import android.database.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */


public class TheApp extends Application {
    public EventStartShopping eventStartShopping = new EventStartShopping();

    private List<ShopList> shopLists;
    private boolean isShopping = false;
    private long shopStartTimeMs;

    @Override
    public void onCreate() {
        super.onCreate();

        // Load data
        shopLists = new ArrayList<>();
    }


    // PUBLIC ACCESSORS

    public boolean isShopping() {
        return isShopping;
    }
    // Returns time since started shopping in ms
    public long getShoppingTime() {
        long currentMs = System.currentTimeMillis();
        return currentMs - shopStartTimeMs;
    }
    public ShopList getShopList(int index) {
        return shopLists.get(index);
    }
    public List<ShopList> getShopLists() {
        return shopLists; // TODO: make unmodifiable? and each item?
    }


    // PUBLIC MODIFIERS

    public void startShopping(ShopList list) {
        isShopping = true;
        shopStartTimeMs = System.currentTimeMillis();
        eventStartShopping.fire(list);
    }
    public void endShopping() {
        isShopping = false;
    }
    public void addShopList(ShopList list) {
        shopLists.add(list);
    }
    public void removeShopList(ShopList list) {
        shopLists.remove(list);
    }
    public void removeShopList(int index) {
        shopLists.remove(index);
    }


    class EventStartShopping extends Event<IEventListener> {
        public void fire(ShopList list) {
            for (IEventListener listener : listeners) {
                listener.onStartShopping(list);
            }
        }
    }
    interface IEventListener {
        public void onStartShopping(ShopList list);
    }
}

