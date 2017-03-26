package sdd.aisle4android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */

// TODO: implement method stubs
public class ShopList {
    public Event<ShopList> eventNameChanged = new Event<ShopList>();
    public Event<ShopList> eventItemsChanged = new Event<ShopList>();

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
        eventNameChanged.fire(this);
    }
    public void addItem(ShopItem item) {
        items.add(item);
        eventItemsChanged.fire(this);
    }
    public void removeItem(ShopItem item) {
        items.remove(item);
        eventItemsChanged.fire(this);
    }
    public void removeItem(int index) {
        items.remove(index);
        eventItemsChanged.fire(this);
    }
    public void save() {

    }
}
