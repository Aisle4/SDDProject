package sdd.aisle4android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements AddItemDialog.Listener {
    private TheApp app;
    private List<String> shopItemNames = new ArrayList<>();
    private ShopList shopList;

    private Toolbar toolbar;

    private ArrayAdapter<String> arrayAdapter;

    private boolean isShopping = false;
    private long shopStartTimeMs;
    private Handler timerHandler;
    private Runnable timerUpdater = new Runnable() {
        @Override
        public void run() {
            long currentMs = System.currentTimeMillis();
            long ms = currentMs - shopStartTimeMs;
            String timeStr = secondsToString((int)(ms / 1000));
            toolbar.setTitle(shopList.getName() + "  " + timeStr);
            timerHandler.postDelayed(this, 1000);
        }
    };


    // PUBLIC MODIFIERS

    public void onClickBtnAddItem(View v) {
        DialogFragment dialog = new AddItemDialog();
        dialog.show(getSupportFragmentManager(), "Add Item"); // TODO: should this tag be in string res?

    }
    public void onClickBtnShop(View v) {
        isShopping = !isShopping;
        Button btn = (Button)v;
        btn.setText(isShopping ? R.string.btn_stop_shop : R.string.btn_shop);

        if (isShopping) {
            // Start Timer
            shopStartTimeMs = System.currentTimeMillis();
            timerHandler.post(timerUpdater);
        }
        else {
            // Stop Timer
            timerHandler.removeCallbacks(timerUpdater);
            toolbar.setTitle(shopList.getName());
        }
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override public void onAddItemDialogConfirm(String itemName) {
        shopItemNames.add(itemName);
        shopList.addItem(new ShopItem(itemName));
        arrayAdapter.notifyDataSetChanged();
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override protected void onCreate(Bundle savedInstanceState) {
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

        // Get Item Names
        for (ShopItem item : shopList.getItems()) {
            shopItemNames.add(item.getName());
        }

        // List
        ListView list = (ListView)findViewById(R.id.list_list);
        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, shopItemNames);
        list.setAdapter(arrayAdapter);


        // Timer
        timerHandler = new Handler();
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60); // TODO: locale
    }
}






