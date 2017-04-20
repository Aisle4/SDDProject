package sdd.aisle4android.Model;

import android.app.Application;

import sdd.aisle4android.Model.Database.DatabaseManager;

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

        database = new DatabaseManager(this);
        foodNameManager = new FoodNameManager();
        shopper = new Shopper(this);
        dataCollector = new DataCollector(this);
        itemOrderer = new ItemOrderer(shopper, database, foodNameManager);
    }

    public DatabaseManager getDatabaseManager(){return database;}
    public DataCollector getDataCollector() {
        return dataCollector;
    }
    public Shopper getShopper() {
        return shopper;
    }
    public FoodNameManager getFoodNameManager() { return foodNameManager; }
}

