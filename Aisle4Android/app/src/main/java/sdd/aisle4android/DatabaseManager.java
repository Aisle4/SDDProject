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
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by John on 3/26/2017.
 */

class DatabaseManager extends AsyncTask {
    private String baseLink = "http://www.carryncare.com/aisle4/server_copy.php" ;


    DatabaseManager() {
    }

    // PUBLIC MODIFIERS

    String addItem(String name) throws IOException {
        //Log.d("DEBUG", "Add item launched");

        String command = "new_item";
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
        //Log.d("DEBUG", "Request has been sent");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String responseLine;

        while((responseLine = reader.readLine()) != null){
            //Log.d("DEBUG", "Response has been received");
            builder.append(responseLine);
            break;
        }

        //Log.d("DEBUG", builder.toString());
        return builder.toString();

    }
    String addItemToItem(int item1ID, int item2ID, int steps, int travel_time) throws IOException {
        //Log.d("DEBUG", "Add Item to Item launched");

        String command = "new_item_to_item";
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
        //Log.d("DEBUG", "Request has been sent");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String responseLine;

        while((responseLine = reader.readLine()) != null){
            //Log.d("DEBUG", "Response has been received");
            builder.append(responseLine);
            break;
        }

        //Log.d("DEBUG", builder.toString());
        return builder.toString();

    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected Object doInBackground(Object[] params) {
        //Log.d("DEBUG", "Thread Launched");
        String command = (String)params[0];

        if(command.compareTo("new_item")==0 && params[1]!=null){
            //Log.d("DEBUG", "New Item command reached");
            String itemName = (String)params[1];
            try {
                addItem(itemName);
                return "Values have been inserted successfully";
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        if(command.compareTo("new_item_to_item")==0 && params[1]!=null && params[2]!=null && params[3]!=null && params[4]!=null){
            //Log.d("DEBUG", "New Item_to_Item command reached");
            int item1ID = (int)params[1];
            int item2ID = (int)params[2];
            int steps = (int)params[3];
            int travel_time = (int)params[4];

            try {
                addItemToItem(item1ID, item2ID, steps, travel_time);
                return "Values have been inserted successfully";
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        return "Incorrect command structure";
    }
}
