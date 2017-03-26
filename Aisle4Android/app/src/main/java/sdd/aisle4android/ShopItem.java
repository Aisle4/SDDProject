package sdd.aisle4android;

/**
 * Created by Robert Wild on 14/03/2017.
 */

public class ShopItem {
    public EventCollected eventCollected = new EventCollected();

    private String name;
    private boolean collected = false;


    public ShopItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
        eventCollected.fire(this);
    }

    class EventCollected extends Event<IEventListener> {
        public void fire(ShopItem item) {
            for (IEventListener listener : listeners) {
                listener.onItemCollected(item);
            }
        }
    }
    interface IEventListener {
        public void onItemCollected(ShopItem item);
    }
}
