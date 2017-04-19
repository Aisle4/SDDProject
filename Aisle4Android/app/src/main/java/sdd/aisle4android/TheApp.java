package sdd.aisle4android;

import android.app.Application;
import android.content.Context;
import android.database.Observable;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */


public class TheApp extends Application {

    // Single instance classes (not true Singletons) are stored in the Application singleton
    private Shopper shopper;
    private DatabaseManager database;
    private DataCollector dataCollector;
    private ItemOrderer itemOrderer;
    private FoodNameManager foodNameManager;

    @Override
    public void onCreate() {
        super.onCreate();


        // TEST DATA---
        List<ItemToItemData> testData = new ArrayList<>();
        String apple = "Apple";
        String mango = "Mango";
        String carrot = "Carrot";
        String potato = "Potato";
        String pepper = "Pepper";
        String milk = "Milk";
        String eggs = "Eggs";
        String butter = "Butter";
        String pasta = "Pasta";
        String rice = "Rice";
        testData.add(new ItemToItemData("", apple, 10, 10));
        testData.add(new ItemToItemData(apple, mango, 10, 10));
        testData.add(new ItemToItemData(mango, carrot, 10, 10));
        testData.add(new ItemToItemData(carrot, pepper, 10, 10));
        testData.add(new ItemToItemData(pepper, milk, 30, 30));
        testData.add(new ItemToItemData(milk, eggs, 10, 10));
        testData.add(new ItemToItemData(eggs, butter, 10, 10));
        testData.add(new ItemToItemData(butter, pasta, 30, 30));
        testData.add(new ItemToItemData(pasta, rice, 10, 10));
        //-------------

        database = new DatabaseManager(this);
        database.addItemQueue("UnitTest");
        foodNameManager = new FoodNameManager();
        shopper = new Shopper(this);
        dataCollector = new DataCollector(this);
        itemOrderer = new ItemOrderer(shopper, null, testData, foodNameManager);
    }

    public Shopper getShopper() {
        return shopper;
    }
}

