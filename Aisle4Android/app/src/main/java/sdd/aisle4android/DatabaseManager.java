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
import java.util.PriorityQueue;
import java.util.Queue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by John on 3/26/2017.
 */

class DatabaseManager extends AsyncTask {
    private String baseLink = "http://www.carryncare.com/aisle4/server_copy.php" ;
    public final String ITEM_TO_ITEM_MANUAL_COMMAND = "new_item_to_item";
    public final String NEW_ITEM_COMMAND = "new_item";
    public final String SUCCESS_MESSAGE = "Values have been inserted successfully";

    private Queue<Object[]> commandQueue;
    private Context context;

    //ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    //NetworkInfo activeNetwork = cm.getActiveNetworkInfo();



    DatabaseManager(Context context)
    {
        this.context = context;
        commandQueue = new PriorityQueue<>();
    }


    public void executeQueue(){
        //boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(true) {
            for (Object command[] : commandQueue) {
                if(((String)command[0]).compareTo(NEW_ITEM_COMMAND)==0){
                    Log.d("Debug", command[0] + " " + command[1]);
                    this.execute(command[0], command[1]);
                }else if(((String)command[1]).compareTo(ITEM_TO_ITEM_MANUAL_COMMAND)==0){
                    Log.d("Debug", command[0] + " " + command[1] + " " + command[2] + " " + command[3] + " " + command[4]);
                    this.execute(command[0], command[1], command[2], command[3], command[4]);
                }
            }
            commandQueue.clear();
        }else{
            Log.d("Debug", "Queue could not execute due to not having a connection");
        }
    }

    // PUBLIC MODIFIERS

    public void addItemQueue(String name){
        Log.d("Debug", "Adding item " + name);
        Object queueCommand[] = {NEW_ITEM_COMMAND, name};
        commandQueue.add(queueCommand);
        executeQueue();
    }

    public void addItemToItemQueue(int item1ID, int item2ID, int steps, int travel_time){
        Log.d("Debug", "Adding item to item relation");
        Object queueCommand[] = {ITEM_TO_ITEM_MANUAL_COMMAND, item1ID, item2ID, steps, travel_time};
        commandQueue.add(queueCommand);
        executeQueue();
    }

    private void asyncAddItem(String name){
        this.execute(NEW_ITEM_COMMAND, name);
    }

    private void asyncNewItemToItem(int item1ID, int item2ID, int steps, int travel_time){
        this.execute(ITEM_TO_ITEM_MANUAL_COMMAND, item1ID, item2ID, steps, travel_time);
    }

    private String addItem(String name) throws IOException {
        Log.d("Debug", "Add item launched");

        String command = NEW_ITEM_COMMAND;
        URL url = new URL(baseLink);
        String data  = URLEncoder.encode("itemName", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(name, StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("command", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(command, StandardCharsets.UTF_8.name());

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(data);
        writer.flush();
        Log.d("Debug", "Request has been sent");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String responseLine;

        while((responseLine = reader.readLine()) != null){
            Log.d("Debug", "Response has been received");
            builder.append(responseLine);
            break;
        }

        Log.d("Debug", builder.toString());
        return builder.toString();

    }
    private String addItemToItem(int item1ID, int item2ID, int steps, int travel_time) throws IOException {
        Log.d("Debug", "Add Item to Item launched");

        String command = ITEM_TO_ITEM_MANUAL_COMMAND;
        URL url = new URL(baseLink);
        String data  = URLEncoder.encode("command", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(command, StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("item1ID", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(Integer.toString(item1ID), StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("item2ID", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(Integer.toString(item2ID), StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("steps", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(Integer.toString(steps), StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("travel_time", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(Integer.toString(travel_time), StandardCharsets.UTF_8.name());

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(data);
        writer.flush();
        Log.d("Debug", "Request has been sent");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String responseLine;

        while((responseLine = reader.readLine()) != null){
            Log.d("Debug", "Response has been received");
            builder.append(responseLine);
            break;
        }

        Log.d("Debug", builder.toString());
        return builder.toString();

    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected Object doInBackground(Object[] params) {
        Log.d("Debug", "Thread Launched");
        String command = (String)params[0];

        if(command.compareTo(NEW_ITEM_COMMAND)==0 && params[1]!=null){
            Log.d("Debug", "New Item command reached");
            String itemName = (String)params[1];
            try {
                addItem(itemName);
                return SUCCESS_MESSAGE;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        if(command.compareTo(ITEM_TO_ITEM_MANUAL_COMMAND)==0 && params[1]!=null && params[2]!=null && params[3]!=null && params[4]!=null){
            Log.d("Debug", "New Item_to_Item command reached");
            int item1ID = (int)params[1];
            int item2ID = (int)params[2];
            int steps = (int)params[3];
            int travel_time = (int)params[4];

            try {
                addItemToItem(item1ID, item2ID, steps, travel_time);
                return SUCCESS_MESSAGE;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        return "Incorrect command structure";
    }
}
