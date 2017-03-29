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

    String getName() {
        return name;
    }

    void setCollected(boolean collected) {
        this.collected = collected;
        eventCollected.fire(this);
    }

    Boolean getCollected(){
        return collected;
    }

    class EventCollected extends Event<IEventListener> {
        void fire(ShopItem item) {
            for (IEventListener listener : listeners) {
                listener.onItemCollected(item);
            }
        }
    }
    interface IEventListener {
        public void onItemCollected(ShopItem item);
    }
}
