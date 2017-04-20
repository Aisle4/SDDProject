package sdd.aisle4android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by John on 3/26/2017.
 */

class DatabaseManager {
    public static final String baseLink = "http://www.carryncare.com/aisle4/server_copy.php" ;
    public static final String ITEM_TO_ITEM_MANUAL_COMMAND = "new_item_to_item";
    public static final String NEW_ITEM_COMMAND = "new_item";
    public static final String SUCCESS_MESSAGE = "Values have been inserted successfully";
    public static final String GET_DATA = "get_data";

    private Queue<Object[]> commandQueue;
    private Context context;

    //ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    //NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

    DatabaseManager(Context context)
    {
        this.context = context;
        commandQueue = new PriorityQueue<>();
    }



    // PUBLIC ACCESSORS

    public List<ItemToItemData> getData() {
        Log.d("Debug", "Attempting to get list from remote");
        List<ItemToItemData> results;
        String stringResults = "";
        try {
            stringResults = (String) (new RemoteCommsTask().execute(GET_DATA)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        results = parseData(stringResults);
        if(results != null) {
            Log.d("Debug", "Post Parse test " + results.get(0).item1Name + " " + results.get(0).item2Name + " " + results.get(0).steps + " " + results.get(0).timeMs);
            return results; // should return all data
        }else{
            return new ArrayList<>();
        }
    }


    // PUBLIC MODIFIERS

    public void executeQueue(){
        //boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(true) {
            for (Object command[] : commandQueue) {
                Log.d("Debug", "Running command " + command[0]);
                if(((String)command[0]).compareTo(NEW_ITEM_COMMAND)==0){
                    Log.d("Debug", command[0] + " " + command[1]);
                    new RemoteCommsTask().execute(command[0], command[1]);
                }else if(((String)command[0]).compareTo(ITEM_TO_ITEM_MANUAL_COMMAND)==0){
                    Log.d("Debug", command[0] + " " + command[1] + " " + command[2] + " " + command[3] + " " + command[4]);
                    new RemoteCommsTask().execute(command[0], command[1], command[2], command[3], command[4]);
                }
            }
            commandQueue.clear();
        }else{
            Log.d("Debug", "Queue could not execute due to not having a connection");
        }
    }

    public void addItemQueue(String name){
        Log.d("Debug", "Adding item " + name);
        Object queueCommand[] = {NEW_ITEM_COMMAND, name};
        commandQueue.add(queueCommand);
        executeQueue();
    }

    public void addItemToItemQueue(String item1Name, String item2Name, int steps, int travel_time){
        Log.d("Debug", "Adding item to item relation " + item1Name + " " + item2Name + " " + Integer.toString(steps) + " " + Integer.toString(travel_time));
        Object queueCommand[] = {ITEM_TO_ITEM_MANUAL_COMMAND, item1Name, item2Name, steps, travel_time};
        commandQueue.add(queueCommand);
        executeQueue();
    }

    private List<ItemToItemData> parseData(String data){
        if (data == null || data.compareTo("")==0) return null;
        ArrayList<ItemToItemData> results = new ArrayList<>();
        String rows[] = data.split("\\Q<br>\\E");
        for (String row: rows) {
            String elements[] = row.split("\\Q|\\E");
            Log.d("ParseDebug", elements[0] + " " + elements[1] + " " + elements[2] + " " + elements[3]);
            results.add(new ItemToItemData(elements[0], elements[1], Integer.parseInt(elements[3]), Integer.parseInt(elements[2])));
        }
        return results;
    }


}
