package sdd.aisle4android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements AddItemDialog.Listener {
    private TheApp app;
    private ShopList shopList;

    private Toolbar toolbar;
    private MenuItem timerItem;
    private ArrayAdapter<String> listArrayAdapter;

    private Handler timerHandler;
    private Runnable timerUpdater = new Runnable() {
        @Override public void run() {
            String timeStr = secondsToString((int)(app.getShoppingTime() / 1000));
            timerItem.setTitle(timeStr);
            timerHandler.postDelayed(this, 1000);
        }
    };


    // PUBLIC MODIFIERS

    public void onClickBtnAddItem(View v) {
        DialogFragment dialog = new AddItemDialog();
        dialog.show(getSupportFragmentManager(), "Add Item"); // TODO: should this tag be in string res?
    }
    public void onClickBtnShop(View v) {
        if (!app.isShopping()) app.startShopping();
        else app.endShopping();

        Button btn = (Button)v;
        btn.setText(app.isShopping() ? R.string.btn_stop_shop : R.string.btn_shop);

        if (app.isShopping()) {
            // Update timer every second
            timerHandler.post(timerUpdater);
        }
        else {
            timerHandler.removeCallbacks(timerUpdater);
            toolbar.setTitle(shopList.getName());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);

        // Timer
        timerItem = menu.findItem(R.id.list_timer);
        timerHandler = new Handler();

        return true;
    }
    @Override
    public void onAddItemDialogConfirm(String itemName) {
        shopList.addItem(new ShopItem(itemName));
        populateList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.list_delete:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        app = (TheApp)getApplicationContext();

        // Get list title from HomeActivity
        Intent intent = getIntent();
        int listIndex = intent.getIntExtra(HomeActivity.MSG_LIST_INDEX, -1);
        if (listIndex < 0 || listIndex > app.getShopLists().size()) {
            // TODO: error handling
            Log.e("error", "Invalid ShopList index on ListActivity Create");
        }
        shopList = app.getShopList(listIndex);

        // Toolbar
        toolbar = (Toolbar)findViewById(R.id.list_toolbar);
        toolbar.setTitle(shopList.getName());
        setSupportActionBar(toolbar);

        // List View
        listArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1);
        populateList();
        ListView list = (ListView)findViewById(R.id.list_list);
        list.setAdapter(listArrayAdapter);
    }
    private void populateList() {
        listArrayAdapter.clear();
        for (ShopItem item : shopList.getItems()) {
            listArrayAdapter.add(item.getName());
        }
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60); // TODO: locale
    }
}






