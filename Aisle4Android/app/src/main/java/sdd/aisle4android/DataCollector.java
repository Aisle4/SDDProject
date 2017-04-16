package sdd.aisle4android;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 26/03/2017.
 */

class DataCollector implements Shopper.IEventStartShopListener, Shopper.IEventStopShopListener,
        ShopList.IEventListener, SensorEventListener {
    private ShopList listInUse;
    private ShopItem lastItem = null;
    private List<ItemToItemData> data = new ArrayList<ItemToItemData>();

    // Time
    private long lastCollectTime = 0;

    // Pedometer
    private int stepsSinceLast = 0;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;


    DataCollector(TheApp app) {
        Shopper shopper = app.getShopper();

        shopper.eventStartShopping.attach(this);
        shopper.eventStopShopping.attach(this);

        // Pedometer
        sensorManager = (SensorManager)app.getSystemService(Application.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }


    // PUBLIC MODIFIERS

    @Override
    public void onStartShopping(Shopper shopper) {
        stepsSinceLast = 0;
        lastCollectTime = System.currentTimeMillis();
        lastItem = null;

        Log.d("debug", "START SHOPPING");

        // Events
        listInUse = shopper.getActiveList();
        listInUse.eventItemCollected.attach(this);

        // Start recording steps
        sensorManager.registerListener(this, stepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    public void onStopShopping(Shopper shopper) {
        Log.d("debug", "STOP SHOPPING");

        // Events
        if (listInUse != null) {
            listInUse.eventItemCollected.dettach(this);
        }

        // Stop recording steps
        sensorManager.unregisterListener(this, stepDetectorSensor);
    }
    @Override
    public void onItemCollected(ShopItem item) {
        if (item == lastItem) return;

        long time = System.currentTimeMillis() - lastCollectTime;
        data.add(new ItemToItemData(lastItem.getName(), item.getName(), time, stepsSinceLast));

        Log.d("debug", "Saved item to item data: ");
        if (lastItem == null) {
            Log.d("debug", "item1: null");
        }
        else {
            Log.d("debug", "item1: " +  lastItem.getName());
        }

        Log.d("debug", "item2: " + item.getName());
        Log.d("debug", "time: " + time);
        Log.d("debug", "steps: " + stepsSinceLast);

        lastCollectTime = System.currentTimeMillis();
        stepsSinceLast = 0;
        lastItem = item;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            Log.d("debug", "STEP");
            ++stepsSinceLast;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
