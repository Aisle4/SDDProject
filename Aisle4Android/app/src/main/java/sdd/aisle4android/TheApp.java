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
        String apple = new ShopItem("Apple", this).getName();
        String mango = new ShopItem("Mango", this).getName();
        String carrot = new ShopItem("Carrot", this).getName();
        String pepper = new ShopItem("Pepper", this).getName();
        String milk = new ShopItem("Milk", this).getName();
        String eggs = new ShopItem("Eggs", this).getName();
        String butter = new ShopItem("Butter", this).getName();
        String pasta = new ShopItem("Pasta", this).getName();
        String rice = new ShopItem("Rice", this).getName();
        testData.add(new ItemToItemData(null, apple, 10, 10));
        testData.add(new ItemToItemData(apple, mango, 10, 10));
        testData.add(new ItemToItemData(mango, carrot, 10, 10));
        testData.add(new ItemToItemData(carrot, pepper, 10, 10));
        testData.add(new ItemToItemData(pepper, milk, 30, 30));
        testData.add(new ItemToItemData(milk, eggs, 10, 10));
        testData.add(new ItemToItemData(eggs, butter, 10, 10));
        testData.add(new ItemToItemData(butter, pasta, 30, 30));
        testData.add(new ItemToItemData(pasta, rice, 10, 10));
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

