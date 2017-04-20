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

class DataCollector implements Shopper.IEarStartShopping, Shopper.IEarStopShopping,
        ShopList.IEarItemCollected, SensorEventListener {

    public static final String STORE_ENTRANCE_NAME = "_enter";

    EventDataRecorded eventDataRecorded = new EventDataRecorded();

    private ShopList listInUse;
    private ShopItem lastItem = null;
    private DatabaseManager database;

    // Local data for one shopping trip -- pushed to database when shopping is done
    private List<ItemToItemData> dataQueue;

    // Time
    private long lastCollectTime = 0;

    // Pedometer
    private int stepsSinceLast = 0;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;


    DataCollector(TheApp app) {
        Shopper shopper = app.getShopper();
        database = app.getDatabaseManager();

        shopper.eventStartShopping.attach(this);
        shopper.eventStopShopping.attach(this);

        // Local data -- pushed to the database once shopping is done
        dataQueue = new ArrayList<>();

        // Pedometer
        sensorManager = (SensorManager)app.getSystemService(Application.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }


    // PUBLIC MODIFIERS

    /**
     * Allow local shopping trip data to be pushed to the database
     */
    public void saveDataToDataBase() {
        for (ItemToItemData datum : dataQueue) {
            database.addItemQueue(datum.item1Name);
            database.addItemQueue(datum.item2Name);
            database.addItemToItemQueue(datum.item1Name, datum.item2Name, datum.steps, (int)datum.timeMs);
        }
    }

    @Override
    public void onStartShopping(Shopper shopper) {
        stepsSinceLast = 0;
        lastCollectTime = System.currentTimeMillis();
        lastItem = null;
        dataQueue.clear();

        // Events
        listInUse = shopper.getActiveList();
        listInUse.eventItemCollected.attach(this);

        // Start recording steps
        sensorManager.registerListener(this, stepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    public void onStopShopping(Shopper shopper) {
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
        ItemToItemData data = new ItemToItemData(lastItem, item, time, stepsSinceLast);
        dataQueue.add(data);

        // Reset for recording next item data
        lastCollectTime = System.currentTimeMillis();
        stepsSinceLast = 0;
        lastItem = item;

        // Event
        eventDataRecorded.fire(data);
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


    class EventDataRecorded extends Event<IEarDataRecorded> {
        void fire(ItemToItemData data) {
            for (IEarDataRecorded listener : listeners) {
                listener.onDataRecorded(data);
            }
        }
    }
    interface IEarDataRecorded {
        void onDataRecorded(ItemToItemData data);
    }
}
