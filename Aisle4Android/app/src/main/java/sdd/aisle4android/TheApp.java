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

    @Override
    public void onCreate() {
        super.onCreate();
        shopper = new Shopper(this);
        //database = new DatabaseManager(this);
        dataCollector = new DataCollector(this);
    }

    public Shopper getShopper() {
        return shopper;
    }
}

