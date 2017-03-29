package sdd.aisle4android;

/**
 * Created by Robert Wild on 14/03/2017.
 */

class ShopItem {
    EventCollected eventCollected = new EventCollected();

    private String name;
    private boolean collected = false;


    ShopItem(String name) {
        this.name = name;
    }


    // PUBLIC ACCESSORS

    String getName() {
        return name;
    }
    Boolean getCollected(){
        return collected;
    }


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
