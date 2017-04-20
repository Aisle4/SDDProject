package sdd.aisle4android.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;
import java.util.UUID;

import sdd.aisle4android.Model.Database.LocalDatabaseHelper;
import sdd.aisle4android.Util.Event;


/**
 * Created by Robert Wild on 14/03/2017.
 */

// TODO: implement method stubs
public class ShopList implements ShopItem.IEventListener, ShopItem.IEarStateChanged {
    EventItemCollected eventItemCollected = new EventItemCollected();
    EventItemsChanged eventItemsChanged = new EventItemsChanged();
    EventOrderChanged eventOrderChanged = new EventOrderChanged();

    private String name;
    private Long created;
    private List<ShopItem> items;
    private long creationDate;
    private String uniqueID;
    private Context context;

    ShopList(String name, Context context) {
        this.name = name;
        this.created = System.currentTimeMillis();
        this.context = context;
        items = new ArrayList<>();
        creationDate = System.currentTimeMillis();// - (5) * (1000 * 60 * 60 * 24);
        this.uniqueID = UUID.randomUUID().toString();
    }

    public ShopList(String id, String name, Long creation, Context context) {
        this.name = name;
        this.created = creation;
        this.creationDate = creation;
        this.uniqueID = id;
        this.context = context;
        items = new ArrayList<>();
    }


    public ShopList(String[] data) {

    }


    // PUBLIC ACCESSORS

    public String getName() {
        return name;
    }
    public String getUniqueID() {
        return uniqueID;
    }
    public String getNameDate() {//TODO: this should be formatted better
        return name + "    Created: " + getCreationDate();
    }
    public ShopItem getItem(int index) {
        return items.get(index);
    }
    public Long getCreated() {
        return created;
    }
    public List<ShopItem> getItems() {
        return items; // TODO: make unmodifiable? and each item?
    }
    public long getCreationDateMillis() { return creationDate; }
    public String getCreationDate() {
        //find each checkpoint (ie. today, yesterday) and compare
        Calendar temp = Calendar.getInstance();
        temp = setMidnight(temp);
        if(creationDate > temp.getTimeInMillis()) { return "Today"; }

        //yesterday
        temp=Calendar.getInstance();
        temp.add(Calendar.DAY_OF_YEAR,-1);//go back 1 day
        temp=setMidnight(temp);
        if(creationDate > temp.getTimeInMillis()) { return "Yesterday"; }

        //one week
        temp=Calendar.getInstance();
        temp.add(Calendar.DAY_OF_YEAR,-7);//go back 7 day
        temp=setMidnight(temp);
        if(creationDate > temp.getTimeInMillis()) {
            String[] days = new String[] { "The Day Before Time", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
            temp.setTimeInMillis(creationDate);
            return days[temp.get(Calendar.DAY_OF_WEEK)];
        }
        else {
            return "Over One Week Ago";//TODO: be more descriptive -b / format dd/mm/yy
        }
    }


    // PUBLIC MODIFIERS

    public void rename(String name) {
        this.name = name;
        dbUpdateList();
    }
    public ShopItem addItem(ShopItem item) {
        dbAddItem(item);
        items.add(item);
        item.eventCollected.attach(this);
        item.eventStateChanged.attach(this);
        eventItemsChanged.fire(this);
        return item;
    }
    public void addItemFromDB(ShopItem item) {
        items.add(item);
        item.eventCollected.attach(this);
        item.eventStateChanged.attach(this);
    }
    public void removeItem(ShopItem item) {
        item.eventCollected.dettach(this);
        item.eventStateChanged.dettach(this);

        items.remove(item);
        dbDeleteItem(item);
        eventItemsChanged.fire(this);
    }
    public void removeItem(int index) {
        ShopItem item = items.get(index);
        item.eventCollected.dettach(this);
        item.eventStateChanged.dettach(this);

        dbDeleteItem(item);
        items.remove(index);
        eventItemsChanged.fire(this);
    }
    public void notifyReordered() {
        // TODO: prevent sorting of list outside of this class... and order in place here somehow...?
        eventOrderChanged.fire(this);
    }
    public void unmarkAllItems() {
        for (ShopItem item : items) {
            item.setCollected(false);
        }
    }

    @Override
    public void onItemCollected(ShopItem item) {
        this.eventItemCollected.fire(item);
    }
    @Override
    public void onItemStateChanged(ShopItem item) {
        this.eventItemsChanged.fire(this);
    }

    // Sorting
    public void sortAlphabetical(){
        Collections.sort(items, new Comparator<ShopItem>() {
            @Override
            public int compare(ShopItem o1, ShopItem o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName());
            }
        });
        eventOrderChanged.fire(this);
    }
    public void sortChronological(){
        Collections.sort(items, new Comparator<ShopItem>() {
            @Override
            public int compare(ShopItem o1, ShopItem o2) {
                if(o1.getAddedDate() > o2.getAddedDate()){
                    return 1;
                }
                if(o2.getAddedDate() > o1.getAddedDate()){
                    return -1;
                }
                return 0;
            }
        });
        eventOrderChanged.fire(this);
    }


    // PRIVATE MODIFIERS

    // local Database
    void dbAddItem(ShopItem item) {
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.addItem(uniqueID, item);
        db.close();
    }
    void dbDeleteItem(ShopItem item) {
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.deleteItem(item);
        db.close();
    }
    void dbUpdateList() {
        LocalDatabaseHelper db = new LocalDatabaseHelper(context);
        db.updateList(this);
        db.close();
    }


    // PRIVATE HELPERS

    private static Calendar setMidnight(Calendar c) {
        // TODO: In hindsight this might be useful to reuse and worth moving somewhere else in the code along with getCreationDate
        // converts the time to midnight of the same day, allowing for comparison
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c;
    }


    // EVENTS

    class EventItemCollected extends Event<IEarItemCollected> {
        void fire(ShopItem item) {
            for (IEarItemCollected listener : listeners) {
                listener.onItemCollected(item);
            }
        }
    }
    interface IEarItemCollected {
        void onItemCollected(ShopItem item);
    }
    class EventItemsChanged extends Event<IEarItemsChanged> {
        void fire(ShopList list) {
            for (IEarItemsChanged listener : listeners) {
                listener.onItemsChanged(list);
            }
        }
    }
    interface IEarItemsChanged {
        void onItemsChanged(ShopList list);
    }
    class EventOrderChanged extends Event<IEarOrderChanged> {
        void fire(ShopList list) {
            for (IEarOrderChanged listener : listeners) {
                listener.onOrderChanged(list);
            }
        }
    }
    interface IEarOrderChanged {
        void onOrderChanged(ShopList list);
    }
}
