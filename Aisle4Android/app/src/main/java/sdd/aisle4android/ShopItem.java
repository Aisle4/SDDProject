package sdd.aisle4android;

/**
 * Created by Robert Wild on 14/03/2017.
 */

import java.util.UUID;

class ShopItem {
    EventCollected eventCollected = new EventCollected();

    private String name;
    private boolean collected = false;
    private long addedDate;
    private String uniqueID;



    ShopItem(String name) {
        this.name = name;
        this.addedDate = System.currentTimeMillis();
        this.uniqueID = UUID.randomUUID().toString();
    }

    ShopItem(String name, boolean collected, long addedDate, String uniqueID){
        this.name = name;
        this.collected = collected;
        this.addedDate = addedDate;
        this.uniqueID = uniqueID;
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


    // PRIVATE MODIFIERS

    void setCollected(boolean collected) {
        this.collected = collected;
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
