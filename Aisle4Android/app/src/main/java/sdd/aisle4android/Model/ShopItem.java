package sdd.aisle4android.Model;

/**
 * Created by Robert Wild on 14/03/2017.
 */



import android.content.Context;

import java.util.UUID;

import sdd.aisle4android.Model.Database.LocalDatabaseHelper;
import sdd.aisle4android.Util.Event;


/**
 * Stores items on a shopping list
 */
public class ShopItem {
    EventCollected eventCollected = new EventCollected();
    EventStateChanged eventStateChanged = new EventStateChanged();

    private String name;
    private boolean collected = false;
    private long addedDate;
    private String uniqueID;
    private Context context;

    // initializes shopitem created in applicate
    ShopItem(String name, Context context) {
        this.name = name;
        this.addedDate = System.currentTimeMillis();
        this.uniqueID = UUID.randomUUID().toString();
        this.context = context;
    }

    // initializes shopitem when being read from the SQLite database
    public ShopItem(String name, boolean collected, long addedDate, String uniqueID, Context context){
        this.name = name;
        this.collected = collected;
        this.addedDate = addedDate;
        this.uniqueID = uniqueID;
        this.context = context;
    }


    // PUBLIC ACCESSORS

    public String getName() {
        return name;
    }
    public String getUniqueID() { return uniqueID; }
    public Boolean isCollected(){
        return collected;
    }
    public long getAddedDate(){return addedDate;}

    // LOCAL DATABASE MODIFIERS

    void updateItemDB(){
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.updateItem(this);
        db.close();
    }


    // PUBLIC MODIFIERS

    public void setCollected(boolean collected) {
        if (this.collected == collected) return;

        this.collected = collected;
        updateItemDB();
        if (this.collected) {
            eventCollected.fire(this);
        }
        eventStateChanged.fire(this);
    }


    // EVENTS

    class EventCollected extends Event<IEventListener> {
        void fire(ShopItem item) {
            for (IEventListener listener : listeners) {
                listener.onItemCollected(item);
            }
        }
    }
    interface IEventListener {
        void onItemCollected(ShopItem item);
    }
    class EventStateChanged extends Event<IEarStateChanged> {
        void fire(ShopItem item) {
            for (IEarStateChanged listener : listeners) {
                listener.onItemStateChanged(item);
            }
        }
    }
    interface IEarStateChanged {
        void onItemStateChanged(ShopItem item);
    }
}
