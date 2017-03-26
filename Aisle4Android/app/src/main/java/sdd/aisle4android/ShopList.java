package sdd.aisle4android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */

// TODO: implement method stubs
public class ShopList implements Event.IListener<ShopItem> {
    public Event<ShopItem> msgItemCollected = new Event<ShopItem>();

    private String name;
    private List<ShopItem> items;


    public ShopList(String name) {
        this.name = name;
        items = new ArrayList<>();
    }
    public ShopList(String[] data) {

    }


    // PUBLIC ACCESSORS

    public String getName() {
        return name;
    }
    public ShopItem getItem(int index) {
        return items.get(index);
    }
    public List<ShopItem> getItems() {
        return items; // TODO: make unmodifiable? and each item?
    }


    // PUBLIC MODIFIERS

    public void rename(String name) {
        this.name = name;
    }
    public void addItem(ShopItem item) {
        items.add(item);
        item.eventCollected.attach(this);
    }
    public void removeItem(ShopItem item) {
        items.remove(item);
    }
    public void removeItem(int index) {
        items.remove(index);
    }
    public void save() {

    }

    @Override
    public void onEvent(Event e, ShopItem arg) {
        if (e == arg.eventCollected) {
            msgItemCollected.fire(arg);
        }
    }
}
