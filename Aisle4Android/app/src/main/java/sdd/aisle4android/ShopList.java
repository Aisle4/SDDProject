package sdd.aisle4android;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;
import java.util.UUID;


/**
 * Created by Robert Wild on 14/03/2017.
 */

// TODO: implement method stubs
class ShopList implements ShopItem.IEventListener {
    EventItemCollected eventItemCollected = new EventItemCollected();

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

    ShopList(String id, String name, Long creation, Context context) {
        this.name = name;
        this.created = creation;
        this.creationDate = creation;
        this.uniqueID = id;
        this.context = context;
        items = new ArrayList<>();
    }


    public ShopList(String[] data) {

    }

    // STATIC FUNCTIONS

    private static Calendar setMidnight(Calendar c) {
        // TODO: In hindsight this might be useful to reuse and worth moving somewhere else in the code along with getCreationDate
        // converts the time to midnight of the same day, allowing for comparison
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c;
    }

    // PUBLIC ACCESSORS

    String getName() {
        return name;
    }
    String getUniqueID() {
        return uniqueID;
    }
    String getNameDate() {//TODO: this should be formatted better
        return name + "    Created: " + getCreationDate();
    }
    ShopItem getItem(int index) {
        return items.get(index);
    }
    Long getCreated() {
        return created;
    }
    List<ShopItem> getItems() {
        return items; // TODO: make unmodifiable? and each item?
    }
    long getCreationDateMillis() { return creationDate; }
    String getCreationDate() {
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

    void rename(String name) {
        this.name = name;
        dbUpdateList();
    }
    ShopItem addItem(ShopItem item) {
        dbAddItem(item);
        items.add(item);
        item.eventCollected.attach(this);
        return item;
    }
    void addItemFromDB(ShopItem item) {
        items.add(item);
        item.eventCollected.attach(this);
    }
    void removeItem(ShopItem item) {
        items.remove(item);
        dbDeleteItem(item);
    }
    void removeItem(int index) {
        items.remove(index);
        dbDeleteItem(items.get(index));
    }


    // LOCAL DATABASE MODIFIERS

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


    void sortAlphabetical(){
        Collections.sort(items, new Comparator<ShopItem>() {
            @Override
            public int compare(ShopItem o1, ShopItem o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName());
            }
        });
    }

    void sortChronological(){
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
    }

    @Override
    public void onItemCollected(ShopItem item) {
        this.eventItemCollected.fire(item);
    }


    class EventItemCollected extends Event<IEventListener> {
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
