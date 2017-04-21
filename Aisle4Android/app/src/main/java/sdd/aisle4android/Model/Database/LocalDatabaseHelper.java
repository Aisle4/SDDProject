package sdd.aisle4android.Model.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sdd.aisle4android.Model.ShopItem;
import sdd.aisle4android.Model.ShopList;

/**
 * Created by cameron on 4/12/17.
 */

/**
 * Stores and reads information from the built in Android SQLite database to retain data
 * when application is restarted.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "listManager";

    private static final String TABLE_LISTS = "lists";

    private static final String TABLE_ITEMS = "items";


    // List Table Columns names
    private static final String KEY_ID = "key";
    private static final String KEY_NAME = "listname";
    private static final String KEY_DATE = "created";

    // Item Table Columns names
    private static final String ITEM_ID_KEY = "itemkey";
    private static final String ITEM_NAME = "itemname";
    private static final String LIST_KEY = "listkey";
    private static final String COLLECTED_KEY = "collected";
    private static final String DATE_KEY = "created";
    private Context context;


    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creates the database if it does not already exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LISTS_TABLE = "CREATE TABLE " + TABLE_LISTS + "("
                + KEY_ID + " TEXT KEY," + KEY_NAME + " TEXT," + KEY_DATE + " INTEGER"
                + ")";
        db.execSQL(CREATE_LISTS_TABLE);

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + ITEM_ID_KEY + " TEXT KEY," + ITEM_NAME + " TEXT," + LIST_KEY + " TEXT,"
                + COLLECTED_KEY + " INTEGER," + DATE_KEY + " INTEGER" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);

        // Create tables again
        onCreate(db);
    }


    // Add an empty list to the database when a list is created.
    public void addList(ShopList shopList){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, shopList.getUniqueID());
        values.put(KEY_NAME, shopList.getName());
        values.put(KEY_DATE, shopList.getCreated());

        db.insert(TABLE_LISTS, null, values);
        db.close();
    }

    // Adds an item to a list in the database
    public void addItem(String uniqueID, ShopItem shopItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_ID_KEY, shopItem.getName());
        values.put(ITEM_NAME, shopItem.getName());
        values.put(LIST_KEY, uniqueID);
        values.put(COLLECTED_KEY, shopItem.isCollected());
        values.put(KEY_DATE, shopItem.getAddedDate());

        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    // Removes an item from a list in the database
    public void deleteItem(ShopItem shopItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, ITEM_ID_KEY + " = ?",
                new String[] { shopItem.getUniqueID() });
        db.close();
    }

    // Updates the information for an item in the database when an item is modified
    public int updateItem(ShopItem shopItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        String list_key = "unknown";
        Cursor cursor = db.rawQuery("SELECT " + LIST_KEY + " FROM " + TABLE_ITEMS + " WHERE " +
                ITEM_ID_KEY + " = '" + shopItem.getUniqueID() + "'", null);
        if (cursor.moveToFirst()) {
            list_key = cursor.getString(0);
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(ITEM_ID_KEY, shopItem.getUniqueID());
        values.put(ITEM_NAME, shopItem.getName());
        values.put(LIST_KEY, list_key);
        values.put(COLLECTED_KEY, shopItem.isCollected());
        values.put(DATE_KEY, shopItem.getAddedDate());

        return db.update(TABLE_ITEMS, values, ITEM_ID_KEY + " = ?",
                new String[] { shopItem.getUniqueID() });

    }

    // Removes a list and all the items on it from the database
    public void deleteList(ShopList shopList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LISTS, KEY_ID + " = ?",
                new String[] { shopList.getUniqueID() });
        db.delete(TABLE_ITEMS, LIST_KEY + " = ?",
                new String[] { shopList.getUniqueID() });
        db.close();
    }

    // Updates a list in the database when it is modified
    public int updateList(ShopList shopList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, shopList.getUniqueID());
        values.put(KEY_NAME, shopList.getName());
        values.put(KEY_DATE, shopList.getCreated());

        return db.update(TABLE_LISTS, values, KEY_ID + " = ?",
                new String[] { shopList.getUniqueID() });
    }

    // Returns a list of all list in the database
    public List<ShopList> getAllLists() {
        Log.d("dbhelper","Fetching all lists");
        List<ShopList> shopListList = new ArrayList<ShopList>();
        String selectQuery = "SELECT * FROM " + TABLE_LISTS + " ORDER BY " + KEY_DATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                shopListList.add(getShopList(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return shopListList;
    }

    // Returns a shoplist of shopitems retreived from the database
    public ShopList getShopList(String listID) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LISTS + " WHERE " + KEY_ID +
                " = '" + listID + "'", null);
        ShopList shopList;
        if (cursor.moveToFirst()) {
            shopList = new ShopList(cursor.getString(0),
                            cursor.getString(1), Long.parseLong(cursor.getString(2)), context);
        }
        else {
            return null;
        }
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE " +
                LIST_KEY + " = '" + listID + "'";
        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                boolean collected = (1 == Integer.parseInt(cursor.getString(3)));
                long dateAdded = Long.parseLong(cursor.getString(4));
                String id = cursor.getString(0);
                ShopItem shopItem = new ShopItem(name, collected, dateAdded, id, context);
                shopList.addItemFromDB(shopItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return shopList;
    }

}
