package sdd.aisle4android;

/**
 * Created by Robert Wild on 14/03/2017.
 */

import android.content.Context;

import java.util.UUID;

class ShopItem {
    EventCollected eventCollected = new EventCollected();

    private String name;
    private boolean collected = false;
    private long addedDate;
    private String uniqueID;
    private Context context;



    ShopItem(String name, Context context) {
        this.name = name;
        this.addedDate = System.currentTimeMillis();
        this.uniqueID = UUID.randomUUID().toString();
        this.context = context;
    }

    ShopItem(String name, boolean collected, long addedDate, String uniqueID, Context context){
        this.name = name;
        this.collected = collected;
        this.addedDate = addedDate;
        this.uniqueID = uniqueID;
        this.context = context;
    }


    // PUBLIC ACCESSORS

    String getName() {
        return name;
    }
    String getUniqueID() { return uniqueID; }
    Boolean getCollected(){
        return collected;
    }
    long getAddedDate(){return addedDate;}

    // LOCAL DATABASE MODIFIERS

    void updateItemDB(){
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.updateItem(this);
        db.close();
    }


    // PRIVATE MODIFIERS

    void setCollected(boolean collected) {
        this.collected = collected;
        updateItemDB();
        eventCollected.fire(this);
    }

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


}
