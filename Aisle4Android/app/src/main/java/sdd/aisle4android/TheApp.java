package sdd.aisle4android;

import android.app.Application;
import android.database.Observable;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */


public class TheApp extends Application {
    private DataCollector dataCollector;
    private Shopper shopper;
    private DatabaseManager database;

    @Override
    public void onCreate() {
        super.onCreate();

        // Load data
        dataCollector = new DataCollector(this);
        database = new DatabaseManager();
    }

    public Shopper getShopper() {
        return shopper;
    }
}

