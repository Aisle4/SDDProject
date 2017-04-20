package sdd.aisle4android.Model.Database;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This class is meant to represent any single command that is sent to the database server.
 * In addition to this representation, this class execute these commands asynchronously.
 * Created by John on 4/19/2017.
 */

public class RemoteCommsTask extends AsyncTask {

    private String baseLink = DatabaseManager.baseLink;
    private String NEW_ITEM_COMMAND = DatabaseManager.NEW_ITEM_COMMAND;
    private String ITEM_TO_ITEM_MANUAL_COMMAND = DatabaseManager.ITEM_TO_ITEM_MANUAL_COMMAND;
    private String SUCCESS_MESSAGE = DatabaseManager.SUCCESS_MESSAGE;
    private String GET_DATA = DatabaseManager.GET_DATA;

    /**
     * This function is responsible for communicating with the database server and giving it the command to add an item to the database
     * @param name The name of the item to be added to the database
     * @return The response from the server
     * @throws IOException For when communication with the server fails.
     */
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

    /**
     * This function is responsible for communicating with the database server and giving it the command to add an itemToItem relationship to the database
     * @param item1Name The name of the first item in the relationship.
     * @param item2Name The name of the second item in the relationship.
     * @param steps The number of step taken in between checking off both items.
     * @param travel_time The amount of time elapsed in between checking off both items.
     * @return The response of the database
     * @throws IOException For when communication with the server fails.
     */
    private String addItemToItem(String item1Name, String item2Name, int steps, int travel_time) throws IOException {
        Log.d("Debug", "Add Item to Item launched");

        String command = ITEM_TO_ITEM_MANUAL_COMMAND;
        URL url = new URL(baseLink);
        String data  = URLEncoder.encode("command", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(command, StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("item1Name", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(item1Name, StandardCharsets.UTF_8.name());
        data += "&" + URLEncoder.encode("item2Name", StandardCharsets.UTF_8.name()) + "=" +
                URLEncoder.encode(item2Name, StandardCharsets.UTF_8.name());
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

    /**
     * This is the function responsible for communicating with the database server and submitting a query for all ItemToitem data.
     * @return This function returns the raw string response from the database server.
     * @throws IOException For when communication with the server fails.
     */
    private String getData() throws IOException {
        Log.d("Debug", "Get data launched");

        String command = GET_DATA;
        URL url = new URL(baseLink);
        String data = URLEncoder.encode("command", StandardCharsets.UTF_8.name()) + "=" +
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

    /**
     * This function is responsible for launching functions to communicate with the database server in their won threads.
     * @param params These parameters are whatever commands and attributes are given to be executed.
     * @return A string response from the database server.
     */
    @Override
    protected Object doInBackground(Object[] params) {
        Log.d("Debug", "Thread Launched");
        String command = (String) params[0];

        if (command.compareTo(NEW_ITEM_COMMAND) == 0 && params[1] != null) {
            Log.d("Debug", "New Item command reached");
            String itemName = (String) params[1];
            try {
                addItem(itemName);
                return SUCCESS_MESSAGE;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        if (command.compareTo(ITEM_TO_ITEM_MANUAL_COMMAND) == 0 && params[1] != null && params[2] != null && params[3] != null && params[4] != null) {
            Log.d("Debug", "New Item_to_Item command reached");
            String item1Name = (String) params[1];
            String item2Name = (String) params[2];
            int steps = (int) params[3];
            int travel_time = (int) params[4];

            try {
                addItemToItem(item1Name, item2Name, steps, travel_time);
                return SUCCESS_MESSAGE;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        if (command.compareTo(GET_DATA) == 0) {
            Log.d("Debug", "Get data command reached");
            try {
                String data = getData();
                Log.d("Debug", "Data collected: " + data);
                return data;

            } catch (IOException e) {
                return e.getMessage();
            }
        }

        return "Incorrect command structure";
    }

}
