package sdd.aisle4android;

import android.app.Application;
import android.content.Context;
import android.database.Observable;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */


public class TheApp extends Application {
    private Shopper shopper;
    private DatabaseManager database;
    private DataCollector dataCollector;
    private ItemOrderer itemOrderer;

    @Override
    public void onCreate() {
        super.onCreate();

        // TEST DATA---
        List<ItemToItemData> testData = new ArrayList<>();
        ShopItem apple = new ShopItem("Apple");
        ShopItem orange = new ShopItem("Orange");
        testData.add(new ItemToItemData(null, orange, 5, 5));
        testData.add(new ItemToItemData(apple, orange, 10, 10));
        //-------------

        shopper = new Shopper(this);
        //database = new DatabaseManager(this);
        dataCollector = new DataCollector(this);
        itemOrderer = new ItemOrderer(shopper, null, testData);
    }

    public Shopper getShopper() {
        return shopper;
    }
}

