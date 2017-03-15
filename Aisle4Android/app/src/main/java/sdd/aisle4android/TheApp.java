package sdd.aisle4android;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Wild on 14/03/2017.
 */

public class TheApp extends Application {
    private List<ShopList> shopLists;

    @Override
    public void onCreate() {
        super.onCreate();

        // Load data
        shopLists = new ArrayList<>();
    }


    // PUBLIC ACCESSORS

    public ShopList getShopList(int index) {
        return shopLists.get(index);
    }
    public List<ShopList> getShopLists() {
        return shopLists; // TODO: make unmodifiable? and each item?
    }


    // PUBLIC MODIFIERS

    public void addShopList(ShopList list) {
        shopLists.add(list);
    }
    public void removeShopList(ShopList list) {

    }
    public void removeShopList(int index) {

    }
}
