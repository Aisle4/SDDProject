package sdd.aisle4android;

import android.content.Context;
import android.os.SystemClock;

import java.util.List;

/**
 * Created by Robert Wild on 29/03/2017.
 */

class Shopper implements ShopList.IEarItemCollected, ShopList.IEarItemsChanged {
    EventStartShopping eventStartShopping = new EventStartShopping();
    EventStopShopping eventStopShopping = new EventStopShopping();
    EventLocationUpdated eventLocationUpdated = new EventLocationUpdated();
    EventShopperListItemsChanged eventListItemsChanged = new EventShopperListItemsChanged();

    // Shopper State
    private List<ShopList> shopLists;
    private boolean isShopping = false;
    private ShopList activeShopList;
    private ShopItem nearestItem = null;
    private long shopStartTimeMs;
    private Context context;

    Shopper(Context context) {
        this.context = context;

        // Load local data
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
        list.eventItemCollected.attach(this);
        list.eventItemsChanged.attach(this);

        shopStartTimeMs = SystemClock.elapsedRealtime();
        eventStartShopping.fire(this);

        nearestItem = null;
        eventLocationUpdated.fire(this, nearestItem);
    }
    void endShopping() {
        isShopping = false;
        activeShopList.eventItemCollected.dettach(this);
        activeShopList.eventItemsChanged.dettach(this);
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

        shopLists.get(index).eventItemCollected.attach(this);
        shopLists.remove(index);
    }

    @Override
    public void onItemCollected(ShopItem item) {
        if (isShopping()) {
            nearestItem = item;
            eventLocationUpdated.fire(this, item);
        }
    }
    @Override
    public void onItemsChanged(ShopList list) {
        eventListItemsChanged.fire(this);
    }


    // EVENTS

    class EventStartShopping extends Event<IEarStartShopping> {
        void fire(Shopper shopper) {
            for (IEarStartShopping listener : listeners) {
                listener.onStartShopping(shopper);
            }
        }
    }
    interface IEarStartShopping {
        void onStartShopping(Shopper shopper);
    }
    class EventStopShopping extends Event<IEarStopShopping> {
        void fire(Shopper shopper) {
            for (IEarStopShopping listener : listeners) {
                listener.onStopShopping(shopper);
            }
        }
    }
    interface IEarStopShopping {
        void onStopShopping(Shopper shopper);
    }
    class EventLocationUpdated extends Event<IEarLocationUpdated> {
        void fire(Shopper shopper, ShopItem nearestItem) {
            for (IEarLocationUpdated listener : listeners) {
                listener.onLocationUpdated(shopper, nearestItem);
            }
        }
    }
    interface IEarLocationUpdated {
        void onLocationUpdated(Shopper shopper, ShopItem nearestItem);
    }
    class EventShopperListItemsChanged extends Event<IEarListItemsChanged> {
        void fire(Shopper shopper) {
            for (IEarListItemsChanged listener : listeners) {
                listener.onShopperListItemsChanged(shopper);
            }
        }
    }
    interface IEarListItemsChanged {
        void onShopperListItemsChanged(Shopper shopper);
    }
}
