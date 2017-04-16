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
        ShopItem mango = new ShopItem("Mango");
        ShopItem carrot = new ShopItem("Carrot");
        ShopItem pepper = new ShopItem("Pepper");
        ShopItem milk = new ShopItem("Milk");
        ShopItem eggs = new ShopItem("Eggs");
        ShopItem butter = new ShopItem("Butter");
        ShopItem pasta = new ShopItem("Pasta");
        ShopItem rice = new ShopItem("Rice");
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

